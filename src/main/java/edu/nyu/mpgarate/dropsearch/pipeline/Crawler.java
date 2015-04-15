package edu.nyu.mpgarate.dropsearch.pipeline;

import com.mongodb.client.MongoDatabase;
import edu.nyu.mpgarate.dropsearch.listener.CrawlerListener;
import edu.nyu.mpgarate.dropsearch.document.WebPage;
import edu.nyu.mpgarate.dropsearch.util.IOUtil;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mike on 4/14/15.
 */
public class Crawler {
    private URL startUrl;
    private SynchronizedKeywordIndex index;
    private List<CrawlerListener> listeners;
    private MongoDatabase db;

    public Crawler(URL startUrl, SynchronizedKeywordIndex index,
                   MongoDatabase db){
        this.startUrl = startUrl;
        this.index = index;
        this.listeners = new LinkedList<CrawlerListener>();
        this.db = db;
    }

    public void crawl() {
        WebPage wp = new WebPage(startUrl);
        index.add("university", wp);
        fireVisitedWebPageEvent(wp);

        String body = null;
        try {
            body = IOUtil.getURLAsString(startUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(body);
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
