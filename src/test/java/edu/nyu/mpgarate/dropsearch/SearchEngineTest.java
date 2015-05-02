package edu.nyu.mpgarate.dropsearch;

import edu.nyu.mpgarate.dropsearch.document.SearchQuery;
import edu.nyu.mpgarate.dropsearch.document.SearchResult;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by mike on 4/14/15.
 */
public class SearchEngineTest {

    @Test
    public void kenRockwellTest() throws URISyntaxException {
        URI kenRockwellUrl = new URI("http://kenrockwell.com/nikon/d7200.htm");
        URI d7200Url = new URI("http://kenrockwell.com/nikon/d7200.htm");

        SearchEngine ds = SearchEngineFactory.getSearchEngine(kenRockwellUrl);

        ds.startSynchronousCrawl();

        List<SearchResult> results = ds.search(SearchQuery.parse("nikon " +
                "d7200"));

        System.out.println(results);

        assertTrue(results.stream().anyMatch(sr -> sr.getUrl()
                .equals(d7200Url)));
    }


    @Test
    public void albertGallatinTest() throws URISyntaxException {
        URI albertGallatinURI = new URI("http://en.wikipedia" +
                ".org/wiki/Albert_Gallatin");
        SearchEngine ds = SearchEngineFactory.getSearchEngine
                (albertGallatinURI);

        ds.startSynchronousCrawl();

        System.out.println("done crawl");

        List<SearchResult> results = ds.search(SearchQuery.parse
                ("university"));


        System.out.println(results);

        SearchResult result = results.get(0);
        SearchResult lastResult = results.get(results.size() - 1);

        System.out.println(result);
        System.out.println(lastResult);

        assertTrue(results.stream().anyMatch(sr -> sr.getUrl()
                .equals(albertGallatinURI)));
    }

//    @Test
//    public void powerPirateTest() throws MalformedURIException {
//        URI powerPirateUrl = new URI("http://powerpirate.com/");
//
//        SearchEngine ds = SearchEngineFactory.getSearchEngine
//                (powerPirateUrl);
//
//        ds.startSynchronousCrawl();
//
//        List<SearchResult> results = ds.search(SearchQuery.parse
//                ("pirate press ping pong"));
//
//        System.out.println(results);
//
//        SearchResult result = results.get(0);
//        SearchResult lastResult = results.get(results.size() - 1);
//
//        System.out.println(result);
//        System.out.println(lastResult);
//
//        assertTrue(results.stream().anyMatch(sr -> sr.getUrl()
//                .equals(powerPirateUrl)));
//    }
}
