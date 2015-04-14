package edu.nyu.mpgarate;

import edu.nyu.mpgarate.dropsearch.listeners.DropSearchListener;
import edu.nyu.mpgarate.dropsearch.model.SynchronizedKeywordIndex;
import edu.nyu.mpgarate.dropsearch.model.WebPage;
import edu.nyu.mpgarate.dropsearch.service.Crawler;

import java.net.URL;
import java.util.List;

import static java.lang.Thread.sleep;

public class DropSearch {
    private Crawler crawler;
    private SynchronizedKeywordIndex index;


    private DropSearch(URL startUrl){
        this.index = new SynchronizedKeywordIndex();
        this.crawler = new Crawler(startUrl, index);
    }

    public static DropSearch fromUrl(URL startUrl){
        return new DropSearch(startUrl);
    }

    public void startCrawl(){
        startCrawlThread();
    }

    private void startCrawlThread(){
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                crawler.crawl();
            }
        };

        new Thread(runnable).start();
    }

    public List<WebPage> search(String term){
        return index.getWebPages(term);
    }

    public void addListener(DropSearchListener listener) {
        crawler.addListener(listener);
    }
}
