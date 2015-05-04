package edu.nyu.mpgarate.dropsearch.crawl;

import edu.nyu.mpgarate.dropsearch.document.Keyword;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
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
                + "<a href='http://en.wikipedia.org/w/index" +
                ".php?title=Albert_Gallatin&action=edit&section=11'>foo</a>"
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
        assertFalse(nextUrls.contains(new URI("http://en.wikipedia.org/w/index.php?title=Albert_Gallatin&action=edit&section=11")));
    }

    @Test
    public void testKeywords_removesStopwords() throws URISyntaxException {
        String body = "lorem ipsum any bEcause been could be that so some " +
                "banana";

        List<String> ignoredWords = Arrays.asList(new String[]{"any",
                "because",
                "bEcause",
                "been",
                "could",
                "be", "that", "so", "some"});


        URI url = new URI("http://example.com");
        Extractor e = Extractor.fromBody(body, url);

        List<Keyword> keywords = e.keywords();

        System.out.println(keywords);
        assertTrue(keywords.stream().anyMatch(kw -> kw.getTerm().equals
                ("lorem")));

        assertTrue(keywords.stream().anyMatch(kw -> kw.getTerm().equals
                ("ipsum")));
        assertTrue(keywords.stream().anyMatch(kw -> kw.getTerm().equals
                ("banana")));

        assertFalse(keywords.stream().anyMatch(kw -> ignoredWords.contains(kw
                .getTerm())));
    }

    private boolean listContainsPair(List<Keyword> keywords, String term, Double
            value){
        return keywords.stream().anyMatch(kw -> (kw.getTerm().equals(term) &&
                kw.getWeight().equals(value)));
    }

//    @Test
//    public void testMetaKeywords() throws URISyntaxException {
//        String body = "<title>France - Wikipedia, the free " +
//                "encyclopedia</title> the beatles are a great band and their " +
//                "music is available for free";
//
//        URI url = new URI("http://example.com?france");
//        Extractor e = Extractor.fromBody(body, url);
//
//        List<Keyword> keywords = e.keywords();
//
//        assertTrue(listContainsPair(keywords, "france", 1.0));
//        assertTrue(listContainsPair(keywords, "wikipedia", 1.0));
//        assertTrue(listContainsPair(keywords, "free", 1.1666666666666667));
//    }


}
