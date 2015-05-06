package edu.nyu.mpgarate.dropsearch.document;

import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import static org.junit.Assert.assertTrue;

/**
 * Created by mike on 4/27/15.
 */
public class SearchQueryTest {
    @Test
    public void searchQueryTest_construction() throws URISyntaxException {
        URI url = new URI("http://example.com");
        URI startUrl = new URI("http://example.com");
        String body = "lorem ipsum dolor sit amet";

        SearchQuery query = SearchQuery.parse("ipsum dolor");

        assertTrue(query.getTerms().contains("ipsum"));
        assertTrue(query.getTerms().contains("dolor"));
    }
}
