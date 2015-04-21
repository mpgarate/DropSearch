package edu.nyu.mpgarate.dropsearch;

import com.mongodb.client.MongoCollection;
import edu.nyu.mpgarate.dropsearch.util.DB;
import edu.nyu.mpgarate.dropsearch.listener.DropSearchListener;
import edu.nyu.mpgarate.dropsearch.retrieve.RetrievalEngine;
import edu.nyu.mpgarate.dropsearch.crawl.SynchronizedKeywordIndex;
import edu.nyu.mpgarate.dropsearch.document.WebPage;
import edu.nyu.mpgarate.dropsearch.crawl.Crawler;
import org.bson.Document;

import java.net.URL;
import java.util.List;

public class SearchEngine {
    private Crawler crawler;
    private SynchronizedKeywordIndex index;
    private RetrievalEngine retrievalEngine;

    private SearchEngine(URL startUrl){
        this.index = SynchronizedKeywordIndex.getInstance();

        MongoCollection<Document> pagesCollection = DB.getPagesCollection();

        this.crawler = new Crawler(startUrl, index, pagesCollection);
        this.retrievalEngine = new RetrievalEngine(startUrl, index,
                pagesCollection);
    }

    public static SearchEngine fromUrl(URL startUrl){
        return new SearchEngine(startUrl);
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
