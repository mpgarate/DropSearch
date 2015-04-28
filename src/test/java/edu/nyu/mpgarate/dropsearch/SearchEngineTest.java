package edu.nyu.mpgarate.dropsearch;

import edu.nyu.mpgarate.dropsearch.document.SearchQuery;
import edu.nyu.mpgarate.dropsearch.document.SearchResult;
import edu.nyu.mpgarate.dropsearch.document.WebPage;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by mike on 4/14/15.
 */
public class SearchEngineTest {

    @Test
    public void kenRockwellTest() throws MalformedURLException {
        URL kenRockwellUrl = new URL("http://kenrockwell.com/nikon/d7200.htm");
        SearchEngine ds = SearchEngineFactory.getSearchEngine(kenRockwellUrl);

        ds.startSynchronousCrawl();

        List<SearchResult> results = ds.search(SearchQuery.parse("nikon"));

        WebPage result = results.get(0).getWebPage();

        System.out.println(result);

        assertEquals(kenRockwellUrl, result.getUrl());
    }


    @Test
    public void albertGallatinTest() throws MalformedURLException {
        URL albertGallatinURL = new URL("http://en.wikipedia" +
                ".org/wiki/Albert_Gallatin");
        SearchEngine ds = SearchEngineFactory.getSearchEngine
                (albertGallatinURL);

        ds.startSynchronousCrawl();

        List<SearchResult> results = ds.search(SearchQuery.parse
                ("new york university"));


        System.out.println(results);

        SearchResult result = results.get(0);
        SearchResult lastResult = results.get(results.size() - 1);

        System.out.println(result);
        System.out.println(lastResult);

        assertTrue(results.stream().anyMatch(sr -> sr.getWebPage().getUrl()
                .equals(albertGallatinURL)));
    }
}
