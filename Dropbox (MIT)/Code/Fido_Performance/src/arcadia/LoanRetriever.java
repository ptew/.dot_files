package arcadia;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

public class LoanRetriever implements Runnable{
	private BlockingQueue<LoanList> queue = null;
	private APIConnection api;
	private Logger log;
	private int initialPoolSize, watchIterations;
	
	
	
	public LoanRetriever(APIConnection api, int initialPoolSize, BlockingQueue<LoanList> queue, int watchIterations, Logger log){
		this.initialPoolSize = initialPoolSize;
		this.queue = queue;
		this.api = api;
		this.log = log;
		this.watchIterations = watchIterations;
	}

	@Override
	public void run() {
		api.startWatching();
		log.info("New producer thread spawned! Retrieving loans now...");
		int previousPoolSize = initialPoolSize;
		for (int t=0; t<watchIterations; t++){
			if (api.isWatching()){
				log.info("t="+t);
				LoanList ll = api.retrieveLoanList(Consts.JSON, true);
				int currentSize = ll.getLoanCount();
				if (currentSize <= initialPoolSize){
					log.info("Still only found "+currentSize+" loans, nothing new yet...");
					continue;
				}else{
					if (currentSize <= previousPoolSize){
						log.info("Found all "+currentSize+" new loans! Closing retriever thread...");
						break;
					}else{
						log.info("Found "+currentSize+" new loans, looking for more...");
						previousPoolSize = currentSize;
						queue.offer(ll);
					}
				}
			}else{
				log.info("Retriever loop broken, API no longer watching?");
				break;
			}
		}
		LoanList poisonPill = new LoanList(new LinkedList<Loan>());
		poisonPill.makePoisonous();
		queue.offer(poisonPill);
		api.stopWatching();
		log.info("Retriever thread closed.");
	}
	
	
	
}
