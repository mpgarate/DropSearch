package edu.nyu.mpgarate.dropsearch;

import edu.nyu.mpgarate.dropsearch.storage.WebPageStore;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by mike on 5/1/15.
 */
public class SearchEngineFactoryTest {
    @Test
    public void moreThanThreeEnginesCausesDeletion() throws MalformedURLException {
        URL url1 = new URL("http://example.com?1");
        URL url2 = new URL("http://example.com?2");
        URL url3 = new URL("http://example.com?3");
        URL url4 = new URL("http://example.com?4");

        SearchEngine se1 = SearchEngineFactory.getSearchEngine(url1);
        SearchEngine se2 = SearchEngineFactory.getSearchEngine(url2);
        SearchEngine se3 = SearchEngineFactory.getSearchEngine(url3);

        se1.startSynchronousCrawl();
        se2.startSynchronousCrawl();
        se3.startSynchronousCrawl();

        WebPageStore webPageStore = new WebPageStore();

        assertNotNull(webPageStore.get(url1, url1));
        assertNotNull(webPageStore.get(url2, url2));
        assertNotNull(webPageStore.get(url3, url3));

        SearchEngine se4 = SearchEngineFactory.getSearchEngine(url4);
        se4.startSynchronousCrawl();

        assertNull(webPageStore.get(url1, url1));
        assertNotNull(webPageStore.get(url2, url2));
        assertNotNull(webPageStore.get(url3, url3));
        assertNotNull(webPageStore.get(url4, url4));
    }
}
