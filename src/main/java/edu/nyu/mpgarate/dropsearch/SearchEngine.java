package edu.nyu.mpgarate.dropsearch;

import edu.nyu.mpgarate.dropsearch.algorithm.PageRanker;
import edu.nyu.mpgarate.dropsearch.crawl.Crawler;
import edu.nyu.mpgarate.dropsearch.document.SearchQuery;
import edu.nyu.mpgarate.dropsearch.document.SearchResult;
import edu.nyu.mpgarate.dropsearch.retrieve.RetrievalEngine;
import edu.nyu.mpgarate.dropsearch.storage.SynchronizedKeywordIndex;
import edu.nyu.mpgarate.dropsearch.storage.WebPageStore;
import edu.nyu.mpgarate.dropsearch.util.listener.DropSearchListener;

import java.net.URI;
import java.util.List;

public class SearchEngine {
    private Crawler crawler;
    private SynchronizedKeywordIndex index;
    private RetrievalEngine retrievalEngine;
    private Boolean started;
    private final Object lock = new Object();
    private Thread crawlThread;
    private URI startUrl;

    SearchEngine(URI startUrl){
        if (null == startUrl){
            throw new NullPointerException();
        }
        this.startUrl = startUrl;
        this.index = new SynchronizedKeywordIndex();
        this.crawler = new Crawler(startUrl, index);
        this.retrievalEngine = new RetrievalEngine(startUrl, index);
        this.started = false;
    }

    public void startSynchronousCrawl(){
        synchronized(lock) {
            if (started) {
                return;
            }
            started = true;
        }

        crawler.crawl();

        started = false;
    }

    public void startAsynchronousCrawl(){
        synchronized(lock) {
            if (started) {
                return;
            }
            started = true;
        }

       Runnable runnable = new Runnable(){
            @Override
            public void run() {
                crawler.crawl();
                started = false;
            }
        };

        crawlThread = new Thread(runnable);
        crawlThread.start();
    }

    public List<SearchResult> search(SearchQuery query){
        List<SearchResult> searchResults = retrievalEngine.getWebPages(query);

        return searchResults;
    }

    public void terminate(){
        synchronized (lock) {
            if (started && crawlThread != null) {
                crawlThread.interrupt();
                started = false;
            }
        }

        new WebPageStore().deleteAllEngineUrls(startUrl);
    }

    public void addListener(DropSearchListener listener) {
        crawler.addListener(listener);
    }
}
