package edu.nyu.mpgarate.dropsearch.document;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.net.URL;
import java.util.Date;

/**
 * Created by mike on 4/14/15.
 */
public class WebPage {
    private URL url;
    private String body;
    private Date dateVisited;
    private Document mongoDocument;

    public WebPage(URL url, String body, Date dateVisited){
        this.url = url;
        this.body = body;
        this.dateVisited = dateVisited;
        createMongoDocument();
    }

    public URL getUrl(){
        return url;
    }

    public Document getMongoDocument(){
        return mongoDocument;
    }

    private void createMongoDocument(){
        this.mongoDocument = new Document("url", url.toString())
                .append("body", body)
                .append("date", dateVisited);
    }

    public String toString(){
        return mongoDocument.toString();
    }
}
