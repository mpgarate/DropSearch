package edu.nyu.mpgarate.dropsearch.retrieve;

import com.mongodb.client.MongoCollection;
import edu.nyu.mpgarate.dropsearch.storage.SynchronizedKeywordIndex;
import edu.nyu.mpgarate.dropsearch.document.DeserializationException;
import edu.nyu.mpgarate.dropsearch.document.WebPage;
import edu.nyu.mpgarate.dropsearch.storage.WebPageStore;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by mike on 4/14/15.
 */
public class RetrievalEngine {
    private URL startUrl;
    private SynchronizedKeywordIndex index;

    public RetrievalEngine(URL startUrl, SynchronizedKeywordIndex index){
        this.startUrl = startUrl;
        this.index = index;
    }

    public List<WebPage> getWebPages(String term){
        List<URL> webPageUrls = index.getWebPageUrls(term);
        List<WebPage> webPages = new LinkedList<WebPage>();

        for(URL webPageUrl : webPageUrls){
            WebPage webPage = new WebPageStore().get(webPageUrl);
            webPages.add(webPage);
        }

        return webPages;
    }
}
