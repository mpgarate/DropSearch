package edu.nyu.mpgarate.dropsearch.util;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mike on 4/15/15.
 */
public class DB {
    public static MongoCollection<Document> getPagesCollection(){
        MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase("drop_search");
        MongoCollection<Document> pagesCollection= db.getCollection("web_pages");

        Map<String, Object> indexRule = new HashMap<String, Object>();
        indexRule.put("url", 1);
        indexRule.put("unique", true);

        pagesCollection.createIndex(new BasicDBObject(indexRule));

        return pagesCollection;
    }
}
