package edu.nyu.mpgarate.dropsearch.crawl;

import edu.nyu.mpgarate.dropsearch.document.Keyword;
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

        List<Keyword> keywords = e.keywords();

        assertTrue(keywords.stream().anyMatch(
                kw -> kw.getTerm().equals("lorem")));
        assertTrue(keywords.stream().anyMatch(
                kw -> kw.getTerm().equals("mailman")));
        assertTrue(keywords.stream().anyMatch(
                kw -> kw.getTerm().equals("man")));
        assertTrue(keywords.stream().anyMatch(
                kw -> kw.getTerm().equals("mail-man")));
    }

    @Test
    public void testKeywords_ignoresWhitespace() throws MalformedURLException {
        String body = "<a href=\"http://www.kenrockwell.com/index" +
                ".htm\">Home</a> &nbsp;&nbsp;&nbsp;";


        URL url = new URL("http://example.com");
        Extractor e = Extractor.fromBody(body, url);

        List<Keyword> keywords = e.keywords();

        System.out.println(keywords);
        assertTrue(keywords.size() == 1);
        assertTrue(keywords.stream().anyMatch(kw -> kw.getTerm().equals
                ("home")));
    }

    @Test
    public void testNextUrls() throws MalformedURLException {
        String body = "<html><body>"
                + "<a href='http://example.com/subdir'>foo</a>"
                + "<a href='http://example.com/image.jpg'>foo</a>"
                + "<a href='/relative/subdir'>foo</a>"
                + "<a href='http://example.com#samepageanchor'>foo</a>"
                + "<a href='http://example.com'>foo</a>"
                + "<a href='http://google.com'>foo</a>"
                + "</body></html>";

        URL startUrl = new URL("http://example.com");
        Extractor e = Extractor.fromBody(body, startUrl);

        List<URL> nextUrls = e.nextUrls();

        assertTrue(nextUrls.contains(new URL("http://example.com/subdir")));
        assertTrue(nextUrls.contains(new URL("http://example.com/relative/subdir")));
        assertFalse(nextUrls.contains(new URL("http://example.com")));
        assertFalse(nextUrls.contains(new URL("http://example.com#samepageanchor")));
        assertFalse(nextUrls.contains(new URL("http://google.com")));
        assertFalse(nextUrls.contains(new URL("http://example.com/image.jpg")));
    }
}
