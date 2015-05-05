package edu.nyu.mpgarate.dropsearch.algorithm.pagerank;

import edu.nyu.mpgarate.dropsearch.storage.SynchronizedKeywordIndex;

import java.net.URI;
import java.util.logging.Logger;

/**
 * Created by mike on 5/3/15.
 */
public class PageRankerManager {
    private final static Logger LOGGER = Logger.getLogger(PageRankerManager
            .class.getName());
    private PageRanker externalRanker;
    private Object lock = new Object();

    private SynchronizedKeywordIndex index;
    private URI startUrl;
    private Runnable rankerPrepRunnable;
    private Thread prepThread;
    private volatile Boolean pendingPrepareNext;

    public PageRankerManager(SynchronizedKeywordIndex index, URI startUrl){
        this.pendingPrepareNext = false;
        this.externalRanker = new PageRanker(index, startUrl);
        this.rankerPrepRunnable = new Runnable(){
            @Override
            public void run() {
                while(pendingPrepareNext) {
                    synchronized (pendingPrepareNext){
                        pendingPrepareNext = false;
                    }

                    PageRanker nextRanker = new PageRanker(index, startUrl);

                    LOGGER.info("inside runnable");
                    nextRanker.update();
                    nextRanker.evaluate();

                    synchronized (lock) {
                        externalRanker = nextRanker;
                    }

                    LOGGER.info("leaving runnable");

                    synchronized (pendingPrepareNext){};
                }
            }
        };
    }

    public PageRanker current(){
        synchronized (lock) {
            return externalRanker;
        }
    }

    public void terminate(){
        synchronized (lock) {
            synchronized (pendingPrepareNext) {
                if (null != prepThread) {
                    prepThread.interrupt();
                }
                pendingPrepareNext = false;
                return;
            }
        }
    }

    public void asyncPrepareNext(){
        synchronized (lock) {
            synchronized (pendingPrepareNext){
                pendingPrepareNext = true;
            }

            if (null == prepThread || ! prepThread.isAlive()){
                LOGGER.info("creating new thread...");

                prepThread = new Thread(rankerPrepRunnable);
                prepThread.start();

                LOGGER.info("started new thread");
            }
        }
    }
}

