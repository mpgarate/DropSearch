package edu.nyu.mpgarate.dropsearch.pipeline;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.nyu.mpgarate.dropsearch.listener.CrawlerListener;
import edu.nyu.mpgarate.dropsearch.document.WebPage;
import edu.nyu.mpgarate.dropsearch.util.IOUtil;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mike on 4/14/15.
 */
public class Crawler {
    private URL startUrl;
    private SynchronizedKeywordIndex index;
    private List<CrawlerListener> listeners;
    private MongoCollection<Document> pagesCollection;

    public Crawler(URL startUrl, SynchronizedKeywordIndex index,
                   MongoCollection<Document> pagesCollection){
        this.startUrl = startUrl;
        this.index = index;
        this.listeners = new LinkedList<CrawlerListener>();
        this.pagesCollection = pagesCollection;
    }

    public void crawl() {
        String body;
        try {
            body = IOUtil.getURLAsString(startUrl);
        } catch (IOException e) {
            return;
        }

        // TODO: index.addAll(List<String>, wp);
        WebPage wp = new WebPage(startUrl, body, new Date());
        Document doc = wp.getMongoDocument();
        pagesCollection.insertOne(doc);
        index.addAll(KeywordExtractor.fromBody(body).extract(), doc.getObjectId
                ("_id"));
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
