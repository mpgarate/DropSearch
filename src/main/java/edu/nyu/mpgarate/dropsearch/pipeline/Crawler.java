package edu.nyu.mpgarate.dropsearch.pipeline;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.nyu.mpgarate.dropsearch.document.DeserializationException;
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

import static com.mongodb.client.model.Filters.eq;

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


        WebPage webPage = getOrFetchWebPage(startUrl);

        if (null == webPage){
            // skip this iteration
        }

        index.addAll(KeywordExtractor.fromBody(webPage.getBody()).extract(),
                webPage.getObjectId());

        fireVisitedWebPageEvent(webPage);
    }

    /**
     * Retrieves the webPage from the database or from a web request.
     *
     * @param url
     * @return the webPage or null if retrieval failed
     */
    private WebPage getOrFetchWebPage(URL url){
        Document doc = pagesCollection.find(eq("url", url.toString())).first();
        String body;
        WebPage webPage;

        if (null == doc){
            try {
                body = IOUtil.getURLAsString(url);
            } catch (IOException e) {
                return null;
            }

            webPage = new WebPage(startUrl, body, new Date());
            doc = webPage.getMongoDocument();
            pagesCollection.insertOne(doc);
        } else {
            System.out.println("---- grabbed doc from database ----");
            try {
                webPage = WebPage.fromMongoDocument(doc);
            } catch (DeserializationException e) {
                return null;
            }
        }

        return webPage;
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
