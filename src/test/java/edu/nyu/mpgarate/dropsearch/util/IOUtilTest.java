package edu.nyu.mpgarate.dropsearch.util;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void testGetImageReturnsNull() throws IOException {
        URL imgUrl = new URL("https://www.google.com/images/srpr/logo11w.png");
        String str = IOUtil.getURLAsString(imgUrl);

        assertTrue(null == str);
    }
}
