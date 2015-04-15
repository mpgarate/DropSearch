package edu.nyu.mpgarate.dropsearch.pipeline;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import edu.nyu.mpgarate.dropsearch.document.DeserializationException;
import edu.nyu.mpgarate.dropsearch.document.WebPage;
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
    private MongoCollection<Document> pagesCollection;

    public RetrievalEngine(URL startUrl, SynchronizedKeywordIndex index,
                           MongoCollection<Document> pagesCollection){
        this.startUrl = startUrl;
        this.index = index;
        this.pagesCollection = pagesCollection;
    }

    public List<WebPage> getWebPages(String term){
        List<ObjectId> objectIds = index.getObjectIds(term);
        List<WebPage> webPages = new LinkedList<WebPage>();

        for (ObjectId id : objectIds){
            Document doc = pagesCollection.find(eq("_id", id)).first();
            try {
                WebPage page = WebPage.fromMongoDocument(doc);
                webPages.add(page);
            } catch (DeserializationException ignoredException) {
            }
        }

        return webPages;
    }
}
