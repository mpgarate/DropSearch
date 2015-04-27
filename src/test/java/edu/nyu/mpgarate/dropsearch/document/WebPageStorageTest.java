package edu.nyu.mpgarate.dropsearch.document;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.nyu.mpgarate.dropsearch.storage.WebPageStore;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by mike on 4/27/15.
 */
public class WebPageStorageTest {
    @Test
    public void webPageSavesInJedisTest() throws MalformedURLException, JsonProcessingException {
        WebPageStore webPageStore = new WebPageStore();

        URL url = new URL("http://example.com");
        String body = "lorem ipsum dolor sit amet";

        WebPage webPage = new WebPage(url, body, new Date());

        ObjectMapper mapper = new ObjectMapper();

        webPageStore.set(webPage.getUrl(), webPage);

        WebPage wp2 = webPageStore.get(webPage.getUrl());
        assertEquals(webPage, wp2);
    }
}
