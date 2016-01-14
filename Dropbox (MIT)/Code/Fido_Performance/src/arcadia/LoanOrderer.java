package arcadia;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;

import org.apache.log4j.Logger;

public class LoanOrderer implements Runnable{
	private BlockingQueue<LoanList> retrievalQueue;
	private final APIConnection api;
	private final Logger log;
	private LoanList alreadyOrdered;
	private final Filter filter;
	private final Scorer scorer;
	private double  availableCash;
        private final double fractionalMax;
	private final String contentType;
	private final List<OrderConfirmation> confirmations;
	private final double WHOLE = 1.0;
	
	public LoanOrderer(APIConnection api, BlockingQueue<LoanList> retrievalQueue, Filter filter, Scorer scorer, LoanList alreadyOrdered, List<OrderConfirmation> confirmations, Logger log, String contentType, double availableCash, double fractionalMax){
		this.retrievalQueue = retrievalQueue;
		this.api = api;
		this.log = log;
		this.filter = filter;
		this.scorer = scorer;
		this.alreadyOrdered = alreadyOrdered;
		this.availableCash = availableCash;
		this.fractionalMax = fractionalMax;
		this.contentType = contentType;
		this.confirmations = confirmations;
	}
        
        public LoanOrderer(APIConnection api, BlockingQueue<LoanList> retrievalQueue, Filter filter, Scorer scorer, LoanList alreadyOrdered, List<OrderConfirmation> confirmations, Logger log, String contentType, double availableCash){
		this.retrievalQueue = retrievalQueue;
		this.api = api;
		this.log = log;
		this.filter = filter;
		this.scorer = scorer;
		this.alreadyOrdered = alreadyOrdered;
		this.availableCash = availableCash;
		this.fractionalMax = WHOLE;
		this.contentType = contentType;
		this.confirmations = confirmations;
	}

	@Override
	public void run() {
            log.info("New Orderer thread spawned! Looking for loans to order now...");
            while (api.isWatching()) {
                try {
                    LoanList currentOrder = retrievalQueue.take();
                    currentOrder.removeLoanList(alreadyOrdered);
                    log.info("New Loans received by Orderer: " + currentOrder.getLoanCount());
                    if (currentOrder.getLoanCount()>0) {            
                        currentOrder.filter(filter);
                        log.info("Filtered. "+currentOrder.getLoanCount()+" loans left");
                        log.info("No scoring or sorting. We have developed the following prioritized list of "+ currentOrder.getLoanCount()+" desirable loans.");
                        if (currentOrder.getLoanCount()>0) {
                            if (fractionalMax == WHOLE) {
                                currentOrder.setRequestAmounts(availableCash);    
                            } else {
                                currentOrder.setFractionalRequestAmounts(availableCash, fractionalMax);    
                            }
                            
                            String orderRequest = api.formatOrderRequest(currentOrder);
                            log.info("Selected "+currentOrder.getLoanCount()+" loans for pre-order");
                            if (orderRequest.contains("loanId")){
                                log.info("Loans selected for order: " + currentOrder);
                                log.info("Order Request: " + orderRequest);
                                String confirmationString = api.placeOrderCSV(orderRequest);
                                List<OrderConfirmation> ocList = api.parseOrderConfirmation(confirmationString, contentType);
                                for (OrderConfirmation oc : ocList){
                                    log.info("Order Confirmation: " + oc.toString());
                                    if (oc.getInvestedAmount()>0) {
                                        availableCash-=oc.getInvestedAmount();
                                        alreadyOrdered.addLoan(currentOrder.getLoanFromID(oc.getLoanID()));
                                        confirmations.add(oc);
                                    }
                                }                  
                            }
                        }
                    }
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(LoanOrderer.class.getName()).log(Level.SEVERE, null, ex);
                }     
            }
            log.info("Orderer loop broken, API is no longer watching!");
            log.info("Orderer thread closed.");
    }	
}
