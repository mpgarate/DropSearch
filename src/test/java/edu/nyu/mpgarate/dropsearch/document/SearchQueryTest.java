package edu.nyu.mpgarate.dropsearch.document;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * Created by mike on 4/27/15.
 */
public class SearchQueryTest {
    @Test
    public void searchQueryTest_construction() throws MalformedURLException {
        URL url = new URL("http://example.com");
        URL startUrl = new URL("http://example.com");
        String body = "lorem ipsum dolor sit amet";
        WebPage webPage = new WebPage(url, body, new Date(), startUrl);

        SearchQuery query = SearchQuery.parse("ipsum dolor");

        assertTrue(query.getTerms().contains("ipsum"));
        assertTrue(query.getTerms().contains("dolor"));
    }
}
