package edu.nyu.mpgarate.dropsearch.util;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertFalse;

/**
 * Created by mike on 4/14/15.
 */
public class IOUtilTest {
    private static URL albertGallatinURL;

    @BeforeClass
    public static void setUp() throws MalformedURLException {
        albertGallatinURL = new URL("http://en.wikipedia" +
                ".org/wiki/Albert_Gallatin");
    }

    @Test
    public void testGetURLAsString_withValidURL() throws IOException {
        String str = IOUtil.getURLAsString(albertGallatinURL);

        assertFalse(str.isEmpty());
    }
}
