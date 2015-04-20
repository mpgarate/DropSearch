package edu.nyu.mpgarate.dropsearch;

import edu.nyu.mpgarate.DropSearch;
import edu.nyu.mpgarate.dropsearch.listener.DropSearchListener;
import edu.nyu.mpgarate.dropsearch.document.WebPage;
import edu.nyu.mpgarate.dropsearch.listener.DropSearchLogger;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by mike on 4/14/15.
 */
public class DropSearchTest {
    private static URL albertGallatinURL;
    private static URL kenRockwellUrl;

    @BeforeClass
    public static void setUp() throws MalformedURLException {
        //albertGallatinURL = new URL("http://en.wikipedia" +
          //      ".org/wiki/Albert_Gallatin");
        kenRockwellUrl = new URL("http://kenrockwell.com/nikon/d7200.htm");
        albertGallatinURL = new URL("http://en.wikipedia" +
                ".org/wiki/Albert_Gallatin");
    }

    @Test
    public void kenRockwellTest() throws MalformedURLException {
        DropSearch ds = DropSearch.fromUrl(kenRockwellUrl);

        ds.startSynchronousCrawl();

        List<WebPage> results = ds.search("nikon");

        WebPage result = results.get(0);

        System.out.println(result);

        assertEquals(kenRockwellUrl, result.getUrl());
    }

    @Test
    public void albertGallatinTest() throws MalformedURLException {
        DropSearch ds = DropSearch.fromUrl(albertGallatinURL);

        ds.startSynchronousCrawl();

        List<WebPage> results = ds.search("university");

        WebPage result = results.get(0);

        System.out.println(result);

        assertEquals(albertGallatinURL, result.getUrl());
    }
}
