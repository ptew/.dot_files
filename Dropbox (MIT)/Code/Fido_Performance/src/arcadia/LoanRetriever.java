package arcadia;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import org.apache.log4j.Logger;

public class LoanRetriever implements Runnable{
    private BlockingQueue<LoanList> queue = null;
    final private APIConnection api;
    final private Logger log;
    final private int watchIterations;
    final private int threadID;
    private AtomicBoolean atomicFlag;
    private AtomicInteger atomicLoanCount;

    public LoanRetriever(APIConnection api, BlockingQueue<LoanList> queue, int watchIterations, Logger log, final int threadID, AtomicBoolean atomicFlag, AtomicInteger atomicLoanCount){
        this.queue = queue;
        this.api = api;
        this.log = log;
        this.watchIterations = watchIterations;
        this.threadID = threadID;
        this.atomicFlag = atomicFlag;
        this.atomicLoanCount = atomicLoanCount;
    }

    @Override
    public void run() {
        api.startWatching();
        log.info("Started Retreiver Thread: " + threadID);
        
        for (int t=0; t < watchIterations; t++){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(LoanRetriever.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (api.isWatching()) {
                log.info("t= " + t + " for Thread " + threadID);
                Calendar calendar = Calendar.getInstance();
                LoanList ll = new LoanList(new LinkedList<Loan>());
                
                if (calendar.get(Calendar.SECOND) > 55) {
                    ll = api.retrieveNewLoanList();
                }
                
                int atomicLoanCountValue = atomicLoanCount.get();
                if ((ll.getLoanCount() > 0 && atomicFlag.compareAndSet(false, true) && atomicLoanCount.compareAndSet(0, ll.getLoanCount()))
                        || (ll.getLoanCount() > atomicLoanCountValue && atomicLoanCount.compareAndSet(atomicLoanCountValue, ll.getLoanCount()))) {
                    log.info("New Loans Published: " + ll.getLoanCount() + " from Thread " + threadID);
                    queue.offer(ll);
                }
            } else {
                log.info("Retriever loop broken, API no longer watching?");
                break;
            }
        }
        
        api.stopWatching();
    }	
}
