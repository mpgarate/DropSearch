package edu.nyu.mpgarate.dropsearch;

import edu.nyu.mpgarate.dropsearch.crawl.Crawler;
import edu.nyu.mpgarate.dropsearch.document.SearchQuery;
import edu.nyu.mpgarate.dropsearch.document.SearchResult;
import edu.nyu.mpgarate.dropsearch.util.listener.DropSearchListener;
import edu.nyu.mpgarate.dropsearch.retrieve.RetrievalEngine;
import edu.nyu.mpgarate.dropsearch.storage.SynchronizedKeywordIndex;

import java.net.URL;
import java.util.List;

public class SearchEngine {
    private Crawler crawler;
    private SynchronizedKeywordIndex index;
    private RetrievalEngine retrievalEngine;
    private Boolean started;
    private Object lock = new Object();

    SearchEngine(URL startUrl){
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

        new Thread(runnable).start();
    }

    public List<SearchResult> search(SearchQuery query){
        return retrievalEngine.getWebPages(query);
    }

    public void addListener(DropSearchListener listener) {
        crawler.addListener(listener);
    }
}
