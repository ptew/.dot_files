package arcadia;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.lang3.StringUtils;

import org.apache.log4j.Logger;
import org.perf4j.log4j.Log4JStopWatch;

public class Cirrix1MR {
    private static Logger log = null;
    private static String contentType = Consts.JSON;

    /**
     * 
     * @param cal
     * @return Returns time period string for saving the log
     */
    private static String getTimeBlock(Calendar cal){
        int hours = cal.get(Calendar.HOUR);
        for (int i = 0; i<Consts.LISTING_TIMES.length; i++){
            if (hours > Consts.LISTING_TIMES[i]-Consts.TIME_BUFFER && hours < Consts.LISTING_TIMES[i]+Consts.TIME_BUFFER){
                return Consts.LISTING_STRINGS[i];
            }
        }
        DateFormat df = new SimpleDateFormat("HH.mm");
        return df.format(cal.getTime());
    }
	
    public static void main(String[] args) throws InterruptedException{
        //grab ordering parameters from configuration file, if it exists
        String configFile = "/Users/ParkerTew/Dropbox (MIT)/Code/Fido_Performance/fidoconfig/Cirrix 1 Order Entry Controls.xlsx";
//        String configFile = "/home/master/fidoconfig/Cirrix 1 Order Entry Controls.xlsx";
        String account = "C1_";
        Config params = new Config (configFile, account);
        params.readFile();

        /////////////////////////////////////////////////////////////////////
        /////////             CONTROL PARAMETERS	        	/////////
        /////////////////////////////////////////////////////////////////////
        double purchaseTarget = params.orderMax;
        double availableCash = purchaseTarget;
        boolean watchForNew = true;
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
        /////////		       SETUP				/////////
        /////////////////////////////////////////////////////////////////////
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.Jdk14Logger");
        String pathToLogs = Consts.PATH_TO_LOGS+Cirrix1MR.class.getName()+"/";
        Calendar cal = Calendar.getInstance();
        System.setProperty("logfile.name", pathToLogs+cal.get(Calendar.YEAR)+"/"+Consts.MONTHS[cal.get(Calendar.MONTH)]+"/"+cal.get(Calendar.DATE)+"-"+getTimeBlock(cal)+".txt");
        log = Logger.getLogger(Cirrix1MR.class.getName());
        APIConnection api = new APIConnection(Consts.SCHEME, Consts.HOST, Consts.LISTING_PATH, Consts.C1_REAL_TOKEN, Consts.C1_REAL_AID, log);
//		APIConnection api = new APIConnection(Consts.SCHEME, Consts.HOST, Consts.LISTING_PATH, Consts.C1_ORDER_PATH, Consts.C1_REAL_TOKEN, Consts.C1_REAL_AID, log);
        Filter filt = new Filter(Consts.C1_MIN_INCOME, Consts.C1_DTI_INQ_FICO, Consts.C1_FICO_INQUIRIES_CHECK, Consts.C1_STATE_LOAN_AMOUNT_CHECK, Consts.C1_FICO_LOAN_CHECK, params.TERM_SUBGRADE_CHECK, Consts.C1_INVALID_GRADES, Consts.C1_INVALID_PURPOSES, Consts.C1_EMP_LENGTH_MIN, Consts.C1_DTI_MAX, Consts.C1_DTI_TERM_CHECK, Consts.C1_FICO_LOW_THRESHHOLD, isWholeValid, isFractionalValid, is36Valid, is60Valid, params.creditModel);
        Scorer scorer = new Scorer(Consts.C2_INQUIRIES_BONUSES, Consts.C2_FICO_BONUSES, Consts.C2_USAGE_BONUSES, Consts.C2_STATE_BONUSES, Consts.C2_GRADE_BONUSES, Consts.C2_TERM_BONUSES, Consts.C1_WILL_COMPETE_BONUS);
        Log4JStopWatch stopwatch = new Log4JStopWatch(log);

        /////////////////////////////////////////////////////////////////////
        /////////		  RETRIEVAL			    /////////
        /////////////////////////////////////////////////////////////////////
        log.info("Purchase Limit: " + purchaseTarget);
        log.info("Wait for new listings?: " + params.watch);
        log.info("Restrict order to 36M loans?: " + params.termAllowance);
        log.info("Credit models: " + params.creditModel);
        log.info("Excluded 36M subgrades: " + params.INVALID_SUBGRADES_36);
        log.info("Excluded 60M subgrades: " + params.INVALID_SUBGRADES_60);
  //          log.info("Excluded grades: " + Consts.C1_INVALID_GRADES);
        log.info("Invalid purposes: " + Consts.C1_INVALID_PURPOSES);
        log.info("Loans retrieved using contentType: " + contentType);	
        LoanList initialPool = api.retrieveLoanList(contentType, true);
        log.info("Before new loans load, initial pool size = "+initialPool.getLoanCount());
        LoanList completePool = new LoanList(new LinkedList<Loan>());
        int investedNotes=0;

        if (watchForNew == true) {	
            //set up multithreading system
            LoanList alreadyOrdered = new LoanList(new LinkedList<Loan>());
            List<OrderConfirmation> confirmations = new ArrayList<OrderConfirmation>();
            BlockingQueue<LoanList> retrievalQueue = new LinkedBlockingQueue<LoanList>();
            BlockingQueue<LoanList> recycleQueue = new LinkedBlockingQueue<LoanList>();
            AtomicBoolean atomicFlag = new AtomicBoolean(false);
            LoanRetriever retriever = new LoanRetriever(api, initialPool.getLoanCount(), retrievalQueue, Consts.C1_WATCH_ITERATIONS, log, atomicFlag);
            LoanOrderer orderer = new LoanOrderer(api, retrievalQueue, recycleQueue, filt, scorer, alreadyOrdered, confirmations, availableCash, Consts.PARTIAL_FRACTION, Consts.C1_PREORDER_THRESHHOLD, log, contentType);
            Thread retrieverThreadOne = new Thread(retriever);
            Thread retrieverThreadTwo = new Thread(retriever);
            Thread retrieverThreadThree = new Thread(retriever);
            Thread retrieverThreadFour = new Thread(retriever);
            Thread retrieverThreadFive = new Thread(retriever);
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
            double cashSpent = 0;
            for (OrderConfirmation oc : confirmations){
                if (oc.getInvestedAmount()>0){
                    availableCash-=oc.getInvestedAmount();
                    cashSpent+=oc.getInvestedAmount();
                    investedNotes++;
                }
            }
            log.info("Total pre-order summary: $"+cashSpent+" on "+investedNotes+" notes.");
            log.info("$"+availableCash+" remaining...");

            //grab all loans on the recycle queue
            List<LoanList> allLoanLists = new ArrayList<LoanList>();
            recycleQueue.drainTo(allLoanLists);
            for (LoanList ll : allLoanLists){
                completePool.merge(ll);
            }
            completePool.removeLoanList(alreadyOrdered);
        } else {
            completePool = initialPool;
        }

        //order the loans
        log.info("Compiled complete listing; "+completePool.getLoanCount()+" loans retrieved");
        completePool.filter(filt);
        log.info("Filtered. "+completePool.getLoanCount()+" loans left");
//        completePool.scoreAllLoans(scorer, Consts.C1_MIN_SCORE_CUTOFF);
//        log.info("Scored. "+completePool.getLoanCount()+" loans left");
//        completePool.sortLoans();
//        log.info("Sorted. After sorting, we have developed the following prioritized list of "+completePool.getLoanCount()+" desirable loans.");
//        if (completePool.getLoanCount()>0){
//            log.info(completePool);
//            completePool.setRequestAmounts(Consts.PARTIAL_FRACTION, availableCash);
//            String orderRequest = api.formatOrderRequest(completePool);
////            log.info("Order request: "+orderRequest);
//            log.info("Will now call api.placeOrder(). Number of requested loans = " + StringUtils.countMatches(orderRequest, "loanId"));
//            String confirmation = api.placeOrder(orderRequest, contentType);
//            List<OrderConfirmation> ocList = api.parseOrderConfirmation(confirmation, contentType);
//            for (OrderConfirmation oc : ocList){
//                log.info(oc.toString());
//                  if (oc.getInvestedAmount()>0){
//                          availableCash-=oc.getInvestedAmount();
//                          investedNotes++;
//                  }
//            }   
//        }
        log.info("Run completed. $"+(purchaseTarget-availableCash)+" spent in "+investedNotes+" notes.");
    }
}
