package edu.nyu.mpgarate.dropsearch.service;

import edu.nyu.mpgarate.dropsearch.listeners.CrawlerListener;
import edu.nyu.mpgarate.dropsearch.model.SynchronizedKeywordIndex;
import edu.nyu.mpgarate.dropsearch.model.WebPage;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mike on 4/14/15.
 */
public class Crawler {
    URL startUrl;
    SynchronizedKeywordIndex index;
    private List<CrawlerListener> listeners = new LinkedList<CrawlerListener>();

    public Crawler(URL startUrl, SynchronizedKeywordIndex index){
        this.startUrl = startUrl;
        this.index = index;
    }

    public void crawl() {
        WebPage wp = new WebPage(startUrl);
        index.add("university", wp);
        fireVisitedWebPageEvent(wp);
    }

    public void addListener(CrawlerListener listener){
        listeners.add(listener);
    }

    private void fireVisitedWebPageEvent(WebPage webPage){
        for(CrawlerListener listener : listeners){
            listener.visitedWebPage(webPage);
        }
    }
}
