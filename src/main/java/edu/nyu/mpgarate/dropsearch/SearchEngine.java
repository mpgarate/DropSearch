package edu.nyu.mpgarate.dropsearch;

import edu.nyu.mpgarate.dropsearch.algorithm.pagerank.PageRankerManager;
import edu.nyu.mpgarate.dropsearch.crawl.Crawler;
import edu.nyu.mpgarate.dropsearch.document.SearchQuery;
import edu.nyu.mpgarate.dropsearch.document.SearchResult;
import edu.nyu.mpgarate.dropsearch.retrieve.RetrievalEngine;
import edu.nyu.mpgarate.dropsearch.storage.SynchronizedKeywordIndex;
import edu.nyu.mpgarate.dropsearch.storage.SynchronizedUriMap;
import edu.nyu.mpgarate.dropsearch.storage.WebPageStore;
import edu.nyu.mpgarate.dropsearch.util.listener.DropSearchListener;

import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

public class SearchEngine {
    private final static Logger LOGGER = Logger.getLogger(SearchEngine.class
            .getName());
    private Crawler crawler;
    private SynchronizedKeywordIndex index;
    private SynchronizedUriMap uriMap;
    private RetrievalEngine retrievalEngine;
    private Boolean started;
    private final Object lock = new Object();
    private Thread crawlThread;
    private URI startUrl;
    private PageRankerManager pageRankerManager;
    private Boolean doneCrawling;

    SearchEngine(URI startUrl){
        if (null == startUrl){
            throw new NullPointerException();
        }
        this.uriMap = new SynchronizedUriMap();
        this.startUrl = startUrl;
        this.index = new SynchronizedKeywordIndex(uriMap);
        this.crawler = new Crawler(startUrl, index, this);
        this.started = false;
        this.pageRankerManager = new PageRankerManager(index, startUrl);
        this.retrievalEngine = new RetrievalEngine(startUrl, index,
                pageRankerManager, uriMap);
        this.doneCrawling = false;
    }

    public SynchronizedUriMap getUriMap(){
        return uriMap;
    }

    public SynchronizedKeywordIndex getIndex(){
        return index;
    }

    public void startSynchronousCrawl(){
        synchronized(lock) {
            if (started || doneCrawling) {
                return;
            }
            started = true;
        }

        crawler.crawl();
    }

    /**
     *
     * @return true if started crawl, false if crawl ongoing or already
     * complete.
     */
    public boolean startAsynchronousCrawl(){
        synchronized(lock) {
            if (started || doneCrawling) {
                return false;
            }
            started = true;
        }

       Runnable runnable = new Runnable(){
            @Override
            public void run() {
                crawler.crawl();
            }
        };

        crawlThread = new Thread(runnable);
        crawlThread.start();

        return true;
    }

    public List<SearchResult> search(SearchQuery query){
        List<SearchResult> searchResults = retrievalEngine.getWebPages(query);

        return searchResults;
    }

    public void terminate(){
        synchronized (lock) {
            if (started && crawlThread != null) {
                LOGGER.info("terminating crawlThread");
                crawler.stopCrawl();
                pageRankerManager.terminate();
                started = false;
            }
        }

        new WebPageStore().deleteAllEngineUrls(startUrl);
    }

    public void updatePageRank(){
        pageRankerManager.asyncPrepareNext();
    }

    public Integer getPagesCrawled(){
        return crawler.getPagesCrawled();
    }

    public Boolean isDoneCrawling(){
        return crawler.isDoneCrawling();
    }

    public Boolean isStarted(){
        return started;
    }

    public void addListener(DropSearchListener listener) {
        crawler.addListener(listener);
    }
}
