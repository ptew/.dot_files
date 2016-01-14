package arcadia;

import static arcadia.Cirrix.IP_Host;
import static arcadia.Cirrix.getIP;
import static arcadia.Cirrix.getTimeBlock;
import static arcadia.Cirrix.log;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.log4j.Logger;

public class Cirrix2aMR extends Cirrix {
	
    public static void main(String[] args) throws InterruptedException {
        //grab ordering parameters from configuration file, if it exists
        String configFile = "/home/master/fidoconfig/Cirrix 2a Order Entry Controls.xlsx";
        String account = "C2a_";
        Config params = new Config (configFile, account);
        params.readFile();

        /////////////////////////////////////////////////////////////////////
        /////////               CONTROL PARAMETERS                  /////////
        /////////////////////////////////////////////////////////////////////
        double purchaseTarget = params.orderMax;
        double availableCash = purchaseTarget;
        boolean watchForNew;
        if (params.watch.equals("YES")){ //"Do you want this ordering session to wait for the new listings?"
            watchForNew = true;
        }else if (params.watch.equals("NO")){
            watchForNew = false;
        }
        boolean isWholeValid = true, isFractionalValid = false;
        boolean is36Valid, is60Valid;
        if (params.termAllowance.equals("YES")){ //"Do you want to restrict your oders to 36M loans?"
            is36Valid=true;
            is60Valid=false;
        } else {                     
            is36Valid=true;
            is60Valid=true;
        }

        /////////////////////////////////////////////////////////////////////
        ////////			     SETUP			    /////////
        /////////////////////////////////////////////////////////////////////
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.Jdk14Logger");
        String pathToLogs = Consts.PATH_TO_LOGS+Cirrix2.class.getName()+"/";
        Calendar cal = Calendar.getInstance();
        System.setProperty("logfile.name", pathToLogs+cal.get(Calendar.YEAR)+"/"+Consts.MONTHS[cal.get(Calendar.MONTH)]+"/"+cal.get(Calendar.DATE)+"-"+getTimeBlock(cal)+".txt");
        log = Logger.getLogger(Cirrix2.class.getName());
        
        try {
            IP_Host = getIP();
            log.info("IP_Host: " + IP_Host);
        } catch (UnknownHostException e) {
            log.info("Error (UnknownHostException): " + e);
        }

        APIConnection api = new APIConnection(Consts.SCHEME, Consts.HOST, Consts.LISTING_PATH, Consts.C2a_REAL_TOKEN, Consts.C2a_REAL_AID, log);
        Filter filt = new Filter(Consts.C2_MIN_INCOME, Consts.C2_DTI_INQ_FICO, Consts.C2_FICO_INQUIRIES_CHECK, Consts.C2_STATE_LOAN_AMOUNT_CHECK, Consts.C2_FICO_LOAN_CHECK, params.TERM_SUBGRADE_CHECK, Consts.C2_INVALID_GRADES, Consts.C2_INVALID_PURPOSES, Consts.C2_EMP_LENGTH_MIN, Consts.C2_DTI_MAX, Consts.C2_DTI_TERM_CHECK, Consts.C2_FICO_LOW_THRESHHOLD, isWholeValid, isFractionalValid, is36Valid, is60Valid, params.creditModel);
	Scorer scorer = new Scorer(Consts.C2_INQUIRIES_BONUSES, Consts.C2_FICO_BONUSES, Consts.C2_USAGE_BONUSES, Consts.C2_STATE_BONUSES, Consts.C2_GRADE_BONUSES, Consts.C2_TERM_BONUSES, Consts.C2_WILL_COMPETE_BONUS);

        /////////////////////////////////////////////////////////////////////
        /////////		  RETRIEVAL			    /////////
        /////////////////////////////////////////////////////////////////////
        log.info("Purchase Limit: " + purchaseTarget);
        log.info("Wait for new listings?: " + params.watch);
        log.info("Restrict order to 36M loans?: " + params.termAllowance);
        log.info("Credit models: " + params.creditModel);
        log.info("Excluded 36M subgrades: " + params.INVALID_SUBGRADES_36);
        log.info("Excluded 60M subgrades: " + params.INVALID_SUBGRADES_60);
        log.info("Invalid purposes: " + Consts.C1_INVALID_PURPOSES);
        log.info("Loans retrieved using contentType: " + contentType);	
        
        LoanList alreadyOrdered = new LoanList(new LinkedList<Loan>());
        LoanList initialPool = api.retrieveAllLoanList();

        int investedNotes = 0;
        log.info("Retreived " + initialPool.getLoanCount() + " old loans");
        if (initialPool.getLoanCount()>0) {            
            initialPool.filter(filt);
            log.info("Filtered. "+initialPool.getLoanCount()+" loans left");
            log.info("No scoring or sorting. We have developed the following prioritized list of "+ initialPool.getLoanCount()+" desirable loans.");
            if (initialPool.getLoanCount()>0) {
                initialPool.setRequestAmounts(availableCash);
                String orderRequest = api.formatOrderRequest(initialPool);
                log.info("Selected "+initialPool.getLoanCount()+" loans for pre-order");
                if (orderRequest.contains("loanId")){
                    log.info("Loans selected for order: " + initialPool);
                    log.info("Order Request: " + orderRequest);
                    String confirmationString = api.placeOrderCSV(orderRequest);
                    List<OrderConfirmation> ocList = api.parseOrderConfirmation(confirmationString, contentType);
                    for (OrderConfirmation oc : ocList){
                        log.info("Order Confirmation: " + oc.toString());
                        if (oc.getInvestedAmount()>0) {
                            availableCash-=oc.getInvestedAmount();
                            investedNotes++;
                            alreadyOrdered.addLoan(initialPool.getLoanFromID(oc.getLoanID()));
                        }
                    }                  
                }
            }
        }
        log.info("Completed Ordering " + alreadyOrdered.getLoanCount() + " old loans");
        log.info("Remaining Avalible Cash: " + availableCash);
        
        //set up multithreading system
        List<OrderConfirmation> confirmations = new ArrayList<>();
        BlockingQueue<LoanList> retrievalQueue = new LinkedBlockingQueue<>();
        AtomicBoolean atomicFlag = new AtomicBoolean(false);
        AtomicInteger atomicLoanCount = new AtomicInteger(0);

        LoanOrderer orderer = new LoanOrderer(api, retrievalQueue, filt, scorer, alreadyOrdered, confirmations, log, contentType, availableCash);
        Thread retrieverThreadOne = new Thread(new LoanRetriever(api, retrievalQueue, Consts.C1_WATCH_ITERATIONS, log, 1, atomicFlag, atomicLoanCount));
        Thread retrieverThreadTwo = new Thread(new LoanRetriever(api, retrievalQueue, Consts.C1_WATCH_ITERATIONS, log, 2, atomicFlag, atomicLoanCount));
        Thread retrieverThreadThree = new Thread(new LoanRetriever(api, retrievalQueue, Consts.C1_WATCH_ITERATIONS, log, 3, atomicFlag, atomicLoanCount));
        Thread retrieverThreadFour = new Thread(new LoanRetriever(api, retrievalQueue, Consts.C1_WATCH_ITERATIONS, log, 4, atomicFlag, atomicLoanCount));
        Thread retrieverThreadFive = new Thread(new LoanRetriever(api, retrievalQueue, Consts.C1_WATCH_ITERATIONS, log, 5, atomicFlag, atomicLoanCount));
        Thread ordererThread = new Thread(orderer);

        //start threads
        ordererThread.start();
        retrieverThreadOne.start();
        Thread.sleep(100);
        retrieverThreadTwo.start();
        Thread.sleep(100);
        retrieverThreadThree.start();
        Thread.sleep(100);
        retrieverThreadFour.start();
        Thread.sleep(100);
        retrieverThreadFive.start();

        //wait for threads to close
        try {
            retrieverThreadOne.join();
            retrieverThreadTwo.join();
            retrieverThreadThree.join();
            retrieverThreadFour.join();
            retrieverThreadFive.join();
            ordererThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("Back to main thread!");
        log.info("Checking results from the watching threads...");
        
        for (OrderConfirmation oc : confirmations){
            if (oc.getInvestedAmount()>0){
                availableCash-=oc.getInvestedAmount();
                investedNotes++;
            }
        }
        
        log.info("Run completed. $"+(purchaseTarget-availableCash)+" spent in "+investedNotes+" notes.");
    }
}
