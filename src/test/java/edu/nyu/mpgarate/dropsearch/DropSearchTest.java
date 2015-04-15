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

/**
 * Created by mike on 4/14/15.
 */
public class DropSearchTest {
    private static URL albertGallatinURL;

    @BeforeClass
    public static void setUp() throws MalformedURLException {
        albertGallatinURL = new URL("http://en.wikipedia" +
                ".org/wiki/Albert_Gallatin");
    }

    @Test()
    public void dropSearchSimpleTest() throws MalformedURLException {
        DropSearch ds = DropSearch.fromUrl(albertGallatinURL);
        DropSearchListener logger = new DropSearchLogger();

        ds.addListener(logger);

        ds.startSynchronousCrawl();

        List<WebPage> results = ds.search("university");
        assertEquals(albertGallatinURL, results.get(0).getUrl());
    }
}
