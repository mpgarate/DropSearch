package edu.nyu.mpgarate.dropsearch;

import edu.nyu.mpgarate.dropsearch.storage.WebPageStore;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by mike on 5/1/15.
 */
public class SearchEngineFactoryTest {
    @Test
    public void moreThanOneEngineCausesDeletion() throws URISyntaxException {
        URI url1 = new URI("http://example.com?1");
        URI url2 = new URI("http://example.com?2");
        URI url3 = new URI("http://example.com?3");
        URI url4 = new URI("http://example.com?4");


        WebPageStore webPageStore = new WebPageStore();

        SearchEngine se1 = SearchEngineFactory.getSearchEngine(url1);
        se1.startSynchronousCrawl();
        SearchEngine se2 = SearchEngineFactory.getSearchEngine(url2);
        se2.startSynchronousCrawl();

        assertNotNull(webPageStore.get(url1, url1));
        assertNotNull(webPageStore.get(url2, url2));

        SearchEngine se3 = SearchEngineFactory.getSearchEngine(url3);
        se3.startSynchronousCrawl();

        assertNull(webPageStore.get(url1, url1));
        assertNotNull(webPageStore.get(url2, url2));
        assertNotNull(webPageStore.get(url3, url3));

        SearchEngine se4 = SearchEngineFactory.getSearchEngine(url4);
        se4.startSynchronousCrawl();

        assertNull(webPageStore.get(url1, url1));
        assertNull(webPageStore.get(url2, url2));
        assertNotNull(webPageStore.get(url3, url3));
        assertNotNull(webPageStore.get(url4, url4));
    }
}
