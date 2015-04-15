package edu.nyu.mpgarate;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.nyu.mpgarate.dropsearch.util.DB;
import edu.nyu.mpgarate.dropsearch.listener.DropSearchListener;
import edu.nyu.mpgarate.dropsearch.retrieve.RetrievalEngine;
import edu.nyu.mpgarate.dropsearch.crawl.SynchronizedKeywordIndex;
import edu.nyu.mpgarate.dropsearch.document.WebPage;
import edu.nyu.mpgarate.dropsearch.crawl.Crawler;
import org.bson.Document;

import java.net.URL;
import java.util.List;

public class DropSearch {
    private URL startUrl;
    private Crawler crawler;
    private SynchronizedKeywordIndex index;
    private RetrievalEngine retrievalEngine;

    private DropSearch(URL startUrl){
        this.startUrl = startUrl;
        this.index = new SynchronizedKeywordIndex();

        MongoClient mongoClient = new MongoClient();

        MongoDatabase db = mongoClient.getDatabase("drop_search");

        MongoCollection<Document> pagesCollection = DB.getPagesCollection();

        this.crawler = new Crawler(startUrl, index, pagesCollection);
        this.retrievalEngine = new RetrievalEngine(startUrl, index,
                pagesCollection);
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
