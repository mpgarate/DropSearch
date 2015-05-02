package edu.nyu.mpgarate.dropsearch.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import edu.nyu.mpgarate.dropsearch.document.WebPage;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by mike on 4/27/15.
 */
public class WebPageStoreTest {
    @Test
    public void webPageSavesTest() throws MalformedURLException,
            JsonProcessingException {
        WebPageStore webPageStore = new WebPageStore();

        URL url = new URL("http://example.com");
        URL startUrl = new URL("http://example.com");

        String body = "lorem ipsum dolor sit amet";

        WebPage webPage = new WebPage(url, body, new Date(), startUrl);

        ObjectMapper mapper = new ObjectMapper();

        webPageStore.save(webPage);

        WebPage wp2 = webPageStore.get(webPage.getUrl(), startUrl);
        assertEquals(webPage, wp2);
    }

    @Test
    public void testMongoConnection() throws MalformedURLException {
        Datastore ds = new Morphia().createDatastore(new MongoClient(),
                "testdb");

        URL url = new URL("http://example.com");
        URL startUrl = new URL("http://example.com");

        String body = "lorem ipsum dolor sit amet";
        WebPage webPage = new WebPage(url, body, new Date(), startUrl);

        ds.save(webPage);

        System.out.println(webPage.getId());

        WebPage wp2 = ds.find(WebPage.class, "_id", webPage.getId()).get();

        System.out.println(wp2.getId());

        assertEquals(webPage, wp2);
    }
}
