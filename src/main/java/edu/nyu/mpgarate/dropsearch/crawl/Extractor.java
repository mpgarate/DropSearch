package edu.nyu.mpgarate.dropsearch.crawl;

import com.google.common.base.CharMatcher;
import edu.nyu.mpgarate.dropsearch.algorithm.VectorSpaceImportance;
import edu.nyu.mpgarate.dropsearch.document.Keyword;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.BreakIterator;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by mike on 4/15/15.
 */
public class Extractor {
    private final Logger LOGGER = Logger.getLogger(Extractor.class.getName());
    private final Document jsoupDoc;
    private final String bodyText;
    private final URI startUrl;
    private final URI startUrlBase;
    private final Set<String> stopwords;

    private Extractor(String body, URI startUrl) {
        this.jsoupDoc = Jsoup.parse(body);
        this.bodyText = jsoupDoc.body().text();
        this.startUrl = startUrl;
        this.startUrlBase = getUrlBase(startUrl);
        this.stopwords = loadStopwords();

        jsoupDoc.setBaseUri(startUrl.toString());
    }

    private Set<String> loadStopwords(){
        InputStream in = Extractor.class.getClassLoader().getResourceAsStream
                ("stopwords.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        try {
            String line = reader.readLine();
            return new HashSet<String>(Arrays.asList(line.split
                    (",")));
        } catch (IOException e) {
            LOGGER.warning("Could not load stopwords.");
            LOGGER.warning(e.toString());
            return Collections.emptySet();
        }
    }

    public static Extractor fromBody(String body, URI startUrl) {
        return new Extractor(body, startUrl);
    }


    private List<String> extractTitleTermsFromBody(){
        List<String> titleTerms = new ArrayList<>();
        String title = jsoupDoc.title();

        for (String s : extractTermsFrom(title)){
            titleTerms.add(s);
        }

        return titleTerms;
    }

    private List<String> extractTermsFrom(String body){
        List<String> termList = new ArrayList<String>();

        BreakIterator boundary = BreakIterator.getWordInstance();
        boundary.setText(body);

        int start = boundary.first();
        for (int end = boundary.next();
             end != BreakIterator.DONE;
             start = end, end = boundary.next()) {

            String keyWord = body.substring(start, end);
            keyWord = CharMatcher.WHITESPACE.removeFrom(keyWord);
            keyWord = keyWord.toLowerCase();

            if (keyWord.length() > 2 && !stopwords.contains(keyWord)) {
                termList.add(keyWord);
            }
        }

        return termList;
    }

    private Map<String, Integer> countTerms(List<String> terms){
        Map<String, Integer> keywordCount = new HashMap<String, Integer>();

        for (String term : terms){
            Integer prevCount = 0;

            if (keywordCount.containsKey(term)){
                prevCount = keywordCount.get(term);
            }

            keywordCount.put(term, prevCount + 1);
        }

        return keywordCount;
    }

    private List<Keyword> compileKeywords(Map<String, Integer> termCounts,
                                          Integer totalTerms,
                                          List<String> titleTerms){
        Map<String, Keyword> keywordMap = new HashMap<String, Keyword>();

        for(Map.Entry<String, Integer> entry : termCounts.entrySet()){
            Integer occCount = entry.getValue();
            String term = entry.getKey();

            Double weight = VectorSpaceImportance.of(occCount, totalTerms);
            Keyword keyword = new Keyword(term, weight);
            keywordMap.put(term, keyword);
        }

        Integer titleTermsCount = titleTerms.size();
        Double titleWeight = 50.0 / titleTerms.size();

        for (String titleTerm : titleTerms){
            Keyword keyword = keywordMap.get(titleTerm);

            if (null == keyword){
                keyword = new Keyword(titleTerm, titleWeight);
            } else {
                keyword = new Keyword(keyword.getTerm(), keyword.getWeight()
                        + titleWeight);
            }

            keywordMap.put(titleTerm, keyword);
        }

        return new ArrayList<Keyword>(keywordMap.values());
    }
    /**
     * Referenced for whitespace treatment:
     * http://stackoverflow.com/questions/7240190/remove-whitespace-chars-from-string-instance
     * @return
     */
    public List<Keyword> keywords() {

        List<String> terms = extractTermsFrom(bodyText);

        Integer totalTerms = terms.size();

        Map<String, Integer> termCounts = countTerms(terms);

        List<String> titleTerms = extractTitleTermsFromBody();
        return compileKeywords(termCounts, totalTerms, titleTerms);
    }

    public List<URI> nextUrls() {
        List<URI> nextUrls = new LinkedList<URI>();
        Elements links = jsoupDoc.select("a[href]");

        for (Element link : links) {
            String urlStr = link.attr("abs:href");

            URI url = getValidNextUrlOrNull(urlStr);

            if (null != url) {
                nextUrls.add(url);
            }
        }

        return nextUrls;
    }

    private URI getValidNextUrlOrNull(String urlStr) {
        if (urlStr.trim().length() <= 4) {
            return null;
        }

        if (urlStr.indexOf(startUrlBase.toString()) != 0){
            return null;
        }

        URI url = startUrlBase.resolve(urlStr);
        URI urlBase = getUrlBase(url);

        if (startUrlBase.equals(urlBase) &&
                urlIsNotSamePageAnchor(url) &&
                urlIsNotForbiddenType(url)) {

            return url;
        }


        return null;
    }

    private URI getUrlBase(URI url) {
        try {
            return new URI(url.getScheme() + "://" + url.getHost());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URI" + url);
        }
    }

    private boolean urlIsNotSamePageAnchor(URI url) {
        String trimmedUrl = url.toString().split("#")[0];

        return !startUrl.toString().equals(trimmedUrl);
    }

    private boolean urlIsNotForbiddenType(URI url) {
        Set<String> forbiddenTypes = new HashSet<String>();
        forbiddenTypes.add("jpg");
        forbiddenTypes.add("png");

        String urlStr = url.toString();
        String extension = urlStr.substring(urlStr.lastIndexOf('.'));
        extension = extension.toLowerCase().replace(".", "");

        return !forbiddenTypes.contains(extension);
    }

}
