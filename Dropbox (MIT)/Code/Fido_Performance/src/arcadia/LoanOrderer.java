package arcadia;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

public class LoanOrderer implements Runnable{
	private BlockingQueue<LoanList> retrievalQueue, recycleQueue;
	private APIConnection api;
	private Logger log;
	private LoanList alreadyOrdered;
	private Filter filter;
	private Scorer scorer;
	private double preorderThreshhold, availableCash, fractionalMax;
	private String contentType;
	private List<OrderConfirmation> confirmations;
	
	
	public LoanOrderer(APIConnection api, BlockingQueue<LoanList> retrievalQueue, BlockingQueue<LoanList> recycleQueue, Filter filter, Scorer scorer, LoanList alreadyOrdered, List<OrderConfirmation> confirmations, double availableCash, double fractionalMax, double preorderThreshhold, Logger log, String contentType){
		this.retrievalQueue = retrievalQueue;
		this.recycleQueue = recycleQueue;
		this.api = api;
		this.log = log;
		this.filter = filter;
		this.scorer = scorer;
		this.alreadyOrdered = alreadyOrdered;
		this.availableCash = availableCash;
		this.fractionalMax = fractionalMax;
		this.contentType = contentType;
		this.preorderThreshhold = preorderThreshhold;
		this.confirmations = confirmations;
	}

	@Override
	public void run() {
		log.info("New consumer thread spawned! Looking for loans to order now...");
		while (api.isWatching()){
			try {
				LoanList currentOrder = retrievalQueue.take();
				if (!currentOrder.isPoison()){
					recycleQueue.offer(currentOrder);
					log.info("Found "+currentOrder.getLoanCount()+" new loans, ready to filter, sort, and order...");
					currentOrder.removeLoanList(alreadyOrdered);
					currentOrder.filter(filter);
                                        log.info("Skip score and sort.");
//					currentOrder.scoreAllLoans(scorer, preorderThreshhold);
//					currentOrder.sortLoans();
					if (currentOrder.getLoanCount() > 0){
						currentOrder.setRequestAmounts(fractionalMax, availableCash);
						String orderRequest = api.formatOrderRequest(currentOrder);
                                                System.out.println("Would be placeing order");
//						log.info("Consumer has selected "+currentOrder.getLoanCount()+" loans for pre-order");
//                                                log.info("Loans selected for order: " + currentOrder);
////						log.info("Order request: "+orderRequest);
///////////////////////////////////////////////////////////////////////////////////////////////////////                                                
////This block had been commented out by DZ.  I am making it active 8/24 at 11:14am
//                                                String confirmationString = api.placeOrder(orderRequest, contentType);
////						log.info("Confirmation: "+confirmationString);
//						List<OrderConfirmation> ocList = api.parseOrderConfirmation(confirmationString, contentType);
//						for (OrderConfirmation oc : ocList){
//                                                    log.info(oc.toString());
//							if (oc.getInvestedAmount()>0){
//								availableCash-=oc.getInvestedAmount();
//								alreadyOrdered.addLoan(currentOrder.getLoanFromID(oc.getLoanID()));
//								confirmations.add(oc);
//							}
//						}
//end of this pre-order block that was commented out by DZ.
/////////////////////////////////////////////////////////////////////////////////////////////////////                                                                                               
					}else{
						log.info("No loans selected for pre-order, looking for more loans...");
					}
				}else{
					
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		log.info("Orderer loop broken, API is no longer watching!");
		log.info("Orderer thread closed.");
	}
	
	
}
