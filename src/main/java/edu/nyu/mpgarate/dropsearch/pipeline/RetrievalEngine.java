package edu.nyu.mpgarate.dropsearch.pipeline;

import com.mongodb.client.MongoDatabase;
import edu.nyu.mpgarate.dropsearch.document.WebPage;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mike on 4/14/15.
 */
public class RetrievalEngine {
    private URL startUrl;
    private SynchronizedKeywordIndex index;
    private MongoDatabase db;

    public RetrievalEngine(URL startUrl, SynchronizedKeywordIndex index,
                           MongoDatabase db){
        this.startUrl = startUrl;
        this.index = index;
        this.db = db;
    }

    public List<WebPage> getWebPages(String term){
        return new LinkedList<WebPage>();
    }
}
