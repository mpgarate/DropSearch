package edu.nyu.mpgarate;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import edu.nyu.mpgarate.dropsearch.listener.DropSearchListener;
import edu.nyu.mpgarate.dropsearch.pipeline.RetrievalEngine;
import edu.nyu.mpgarate.dropsearch.pipeline.SynchronizedKeywordIndex;
import edu.nyu.mpgarate.dropsearch.document.WebPage;
import edu.nyu.mpgarate.dropsearch.pipeline.Crawler;

import java.net.URL;
import java.util.List;

import static java.lang.Thread.sleep;

public class DropSearch {
    private URL startUrl;
    private Crawler crawler;
    private SynchronizedKeywordIndex index;
    private RetrievalEngine retrievalEngine;
    private MongoDatabase db;

    private DropSearch(URL startUrl){
        this.startUrl = startUrl;
        this.index = new SynchronizedKeywordIndex();
        MongoClient mongoClient = new MongoClient();

        this.db = mongoClient.getDatabase("web_pages");

        this.crawler = new Crawler(startUrl, index, db);
        this.retrievalEngine = new RetrievalEngine(startUrl, index,
                db);

    }

    public static DropSearch fromUrl(URL startUrl){
        return new DropSearch(startUrl);
    }

    public void startSynchronousCrawl(){
        crawler.crawl();
    }

    public void startAsynchronousCrawl(){
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                crawler.crawl();
            }
        };

        new Thread(runnable).start();
    }

    public List<WebPage> search(String term){
        return retrievalEngine.getWebPages(term);
    }

    public void addListener(DropSearchListener listener) {
        crawler.addListener(listener);
    }
}
