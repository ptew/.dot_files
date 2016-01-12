package arcadia;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Calendar;
import java.util.logging.Level;
import org.apache.log4j.Logger;

public class LoanRetriever implements Runnable{
    private BlockingQueue<LoanList> queue = null;
    final private APIConnection api;
    final private Logger log;
    final private int initialPoolSize, watchIterations;
    private AtomicBoolean atomicFlag;

    public LoanRetriever(APIConnection api, int initialPoolSize, BlockingQueue<LoanList> queue, int watchIterations, Logger log, AtomicBoolean atomicFlag){
        this.initialPoolSize = initialPoolSize;
        this.queue = queue;
        this.api = api;
        this.log = log;
        this.watchIterations = watchIterations;
        this.atomicFlag = atomicFlag;
        
    }

    @Override
    public void run() {
        api.startWatching();
        log.info("New producer thread spawned! Retrieving loans now...");
        int previousPoolSize = initialPoolSize;
        boolean retreiver = false;
        
        for (int t=0; t<watchIterations && !atomicFlag.get(); t++){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(LoanRetriever.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (api.isWatching()) {
                log.info("t="+t);
                Calendar calendar = Calendar.getInstance();
                LoanList ll = new LoanList(new LinkedList<Loan>());
                
                if (calendar.get(Calendar.SECOND) > 55) {
                    ll = api.retrieveNewLoanList();
                }
                
                if (ll.getLoanCount() > 0 && atomicFlag.compareAndSet(false, true)) {
                    log.info("New loans published");
                    queue.offer(ll);
                    retreiver = true;
                }
                
//                int currentSize = ll.getLoanCount();
//                if (currentSize <= initialPoolSize) {
//                        log.info("Still only found "+currentSize+" loans, nothing new yet...");
//                        continue;
//                } else {
//                    if (currentSize <= previousPoolSize){
//                        log.info("Found all "+currentSize+" new loans! Closing retriever thread...");
//                        break;
//                    } else {
//                        log.info("Found "+currentSize+" new loans, looking for more...");
//                        previousPoolSize = currentSize;
//                        if (atomicFlag.compareAndSet(false, true)) {
//                            log.info("New loans published");
//                            queue.offer(ll);
//                            retreiver = true;
//                        }                        
//                    }
//                }
            } else {
                log.info("Retriever loop broken, API no longer watching?");
                break;
            }
        }
        if (retreiver) {
            LoanList poisonPill = new LoanList(new LinkedList<Loan>());
            poisonPill.makePoisonous();
            queue.offer(poisonPill);
            api.stopWatching();
            log.info("Retriever thread closed.");
        }
    }	
}
