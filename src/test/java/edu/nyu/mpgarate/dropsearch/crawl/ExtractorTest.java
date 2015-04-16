package edu.nyu.mpgarate.dropsearch.crawl;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by mike on 4/16/15.
 */
public class ExtractorTest {
    @Test
    public void testKeywords() throws MalformedURLException {
        String body = "lorem ipsum dolor sit amet mailman mail man mail-man";
        URL url = new URL("http://example.com");
        Extractor e = Extractor.fromBody(body, url);

        List<String> keywords = e.keywords();

        assertTrue(keywords.contains("lorem"));
        assertTrue(keywords.contains("mailman"));
        assertTrue(keywords.contains("man"));
        assertTrue(keywords.contains("mail-man"));
    }

    @Test
    public void testNextUrls() throws MalformedURLException {
        String body = "<html><body>"
                + "<a href='http://example.com/subdir'>foo</a>"
                + "<a href='http://example.com/image.jpg'>foo</a>"
                + "<a href='/relative/subdir'>foo</a>"
                + "<a href='http://example.com/#samepageanchor'>foo</a>"
                + "<a href='http://example.com#samepageanchor'>foo</a>"
                + "<a href='http://example.com'>foo</a>"
                + "<a href='http://google.com'>foo</a>"
                + "</body></html>";

        URL startUrl = new URL("http://example.com");
        Extractor e = Extractor.fromBody(body, startUrl);

        List<URL> nextUrls = e.nextUrls();

        System.out.println(nextUrls.size());
        for (URL url : nextUrls){
            System.out.println(url);
            System.out.println("got here");
            System.out.println(url.equals(new URL("http://example" +
                    ".com/subdir")));
        }

        assertTrue(nextUrls.contains(new URL("http://example.com/subdir")));
        assertTrue(nextUrls.contains(new URL("http://example.com/relative/subdir")));
        assertFalse(nextUrls.contains(new URL("http://example.com")));
        assertFalse(nextUrls.contains(new URL("http://example.com/#samepageanchor")));
        assertTrue(nextUrls.contains(new URL("http://google.com")));
        assertTrue(nextUrls.contains(new URL("http://example.com/image.jpg")));
    }
}
