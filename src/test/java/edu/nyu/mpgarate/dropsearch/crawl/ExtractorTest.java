package edu.nyu.mpgarate.dropsearch.crawl;

import edu.nyu.mpgarate.dropsearch.document.Keyword;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by mike on 4/16/15.
 */
public class ExtractorTest {
    @Test
    public void testKeywords() throws URISyntaxException {
        String body = "lorem ipsum dolor sit amet mailman mail man mail-man";
        URI url = new URI("http://example.com");
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
    public void testKeywords_ignoresWhitespace() throws URISyntaxException {
        String body = "<a href=\"http://www.kenrockwell.com/index" +
                ".htm\">Home</a> &nbsp;&nbsp;&nbsp;";


        URI url = new URI("http://example.com");
        Extractor e = Extractor.fromBody(body, url);

        List<Keyword> keywords = e.keywords();

        System.out.println(keywords);
        assertTrue(keywords.size() == 1);
        assertTrue(keywords.stream().anyMatch(kw -> kw.getTerm().equals
                ("home")));
    }

    @Test
    public void testNextUrls() throws URISyntaxException {
        String body = "<html><body>"
                + "<a href='http://example.com/subdir'>foo</a>"
                + "<a href='http://example.com/image.jpg'>foo</a>"
                + "<a href='/relative/subdir'>foo</a>"
                + "<a href='http://example.com#samepageanchor'>foo</a>"
                + "<a href='http://example.com'>foo</a>"
                + "<a href='http://google.com'>foo</a>"
                + "</body></html>";

        URI startUrl = new URI("http://example.com");
        Extractor e = Extractor.fromBody(body, startUrl);

        List<URI> nextUrls = e.nextUrls();

        assertTrue(nextUrls.contains(new URI("http://example.com/subdir")));
        assertTrue(nextUrls.contains(new URI("http://example.com/relative/subdir")));
        assertFalse(nextUrls.contains(new URI("http://example.com")));
        assertFalse(nextUrls.contains(new URI("http://example.com#samepageanchor")));
        assertFalse(nextUrls.contains(new URI("http://google.com")));
        assertFalse(nextUrls.contains(new URI("http://example.com/image.jpg")));
    }
}
