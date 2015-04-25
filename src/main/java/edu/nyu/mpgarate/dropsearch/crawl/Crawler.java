package edu.nyu.mpgarate.dropsearch.crawl;

import com.mongodb.client.MongoCollection;
import edu.nyu.mpgarate.dropsearch.document.DeserializationException;
import edu.nyu.mpgarate.dropsearch.listener.CrawlerListener;
import edu.nyu.mpgarate.dropsearch.document.WebPage;
import edu.nyu.mpgarate.dropsearch.util.IOUtil;
import org.bson.Document;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by mike on 4/14/15.
 */
public class Crawler {
    private URL startUrl;
    private SynchronizedKeywordIndex index;
    private List<CrawlerListener> listeners;
    private MongoCollection<Document> pagesCollection;
    private final Integer maxCrawlPages;

    public Crawler(URL startUrl, SynchronizedKeywordIndex index,
                   MongoCollection<Document> pagesCollection){
        this.startUrl = startUrl;
        this.index = index;
        this.listeners = new LinkedList<CrawlerListener>();
        this.pagesCollection = pagesCollection;
        this.maxCrawlPages = 15;
    }

    public void crawl() {
        Queue<URL> urls = new LinkedList<URL>();

        int pagesVisited = 0;

        urls.add(startUrl);

        while (!urls.isEmpty() && pagesVisited < maxCrawlPages){
            URL url = urls.remove();

            System.out.println(url);

            WebPage webPage = getOrFetchWebPage(url);

            if (null == webPage){
                continue;
            }

            pagesVisited++;

            Extractor extractor = Extractor.fromBody(webPage.getBody(),
                    url);

            index.addAll(extractor.keywords(), webPage.getObjectId());

            System.out.println("getting urls");
            urls.addAll(extractor.nextUrls());
            System.out.println("done.");

            System.out.println("Visited:");
            System.out.println(url.toString());
            System.out.println(extractor.keywords());

            fireVisitedWebPageEvent(webPage);
        }


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
                if (null == body){
                    return null;
                }
            } catch (IOException e) {
                System.out.println("---- could not get page ----");
                System.out.println(url.toString());
                return null;
            }

            System.out.println("---- fetched doc from the web ----");
            webPage = new WebPage(url, body, new Date());
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
