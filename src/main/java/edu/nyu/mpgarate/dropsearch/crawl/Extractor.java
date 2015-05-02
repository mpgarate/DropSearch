package edu.nyu.mpgarate.dropsearch.crawl;

import edu.nyu.mpgarate.dropsearch.algorithm.VectorSpaceImportance;
import edu.nyu.mpgarate.dropsearch.document.Keyword;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.BreakIterator;
import java.util.*;

/**
 * Created by mike on 4/15/15.
 */
public class Extractor {
    private final Document jsoupDoc;
    private final String bodyText;
    private final URL startUrl;
    private final URL startUrlBase;

    private Extractor(String body, URL startUrl) {
        this.jsoupDoc = Jsoup.parse(body);
        this.bodyText = jsoupDoc.body().text();
        this.startUrl = startUrl;
        this.startUrlBase = getUrlBase(startUrl);

        jsoupDoc.setBaseUri(startUrl.toString());
    }

    public static Extractor fromBody(String body, URL startUrl) {
        return new Extractor(body, startUrl);
    }

    /**
     * Referenced for whitespace treatment:
     * http://stackoverflow.com/questions/7240190/remove-whitespace-chars-from-string-instance
     * @return
     */
    public List<Keyword> keywords() {
        // TODO: consider the count for how many times a keyword appears

        List<String> keywordList = new ArrayList<String>();

        BreakIterator boundary = BreakIterator.getWordInstance();
        boundary.setText(bodyText);

        int start = boundary.first();
        for (int end = boundary.next();
             end != BreakIterator.DONE;
             start = end, end = boundary.next()) {

            String keyWord = bodyText.substring(start, end);
            keyWord = keyWord.replaceAll("(?U)\\s", "");
            if (keyWord.length() > 2) {
                keywordList.add(keyWord.toLowerCase());
            }
        }

        Integer totalKeywords = keywordList.size();
        
        Map<String, Integer> keywordCount = new HashMap<String, Integer>();

        for (String term : keywordList){
            Integer prevCount = 0;

            if (keywordCount.containsKey(term)){
                prevCount = keywordCount.get(term);
            }

            keywordCount.put(term, prevCount + 1);
        }

        List<Keyword> keywords = new ArrayList<>();

        for (String term : keywordCount.keySet()){
            Integer occCount = keywordCount.get(term);
            Keyword keyword = new Keyword(term, VectorSpaceImportance.of
                    (occCount, totalKeywords));
            keywords.add(keyword);
        }

        return keywords;
    }

    public List<URL> nextUrls() {
        List<URL> nextUrls = new LinkedList<URL>();
        Elements links = jsoupDoc.select("a[href]");

        for (Element link : links) {
            String urlStr = link.attr("abs:href");

            URL url = getValidNextUrlOrNull(urlStr);

            if (null != url) {
                nextUrls.add(url);
            }
        }

        return nextUrls;
    }

    private URL getValidNextUrlOrNull(String urlStr) {
        if (urlStr.trim().length() <= 4) {
            return null;
        }

        if (urlStr.indexOf(startUrlBase.toString()) != 0){
            return null;
        }

        try {
            URL url = new URL(startUrlBase, urlStr);
            URL urlBase = getUrlBase(url);

            if (startUrlBase.equals(urlBase) &&
                    urlIsNotSamePageAnchor(url) &&
                    urlIsNotForbiddenType(url)) {

                return url;
            }

        } catch (MalformedURLException ignoredException) {
        }

        return null;
    }

    private URL getUrlBase(URL url) {
        try {
            return new URL(url.getProtocol() + "://" + url.getHost());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL" + url);
        }
    }

    private boolean urlIsNotSamePageAnchor(URL url) {
        String trimmedUrl = url.toString().split("#")[0];

        return !startUrl.toString().equals(trimmedUrl);
    }

    private boolean urlIsNotForbiddenType(URL url) {
        Set<String> forbiddenTypes = new HashSet<String>();
        forbiddenTypes.add("jpg");
        forbiddenTypes.add("png");

        String urlStr = url.toString();
        String extension = urlStr.substring(urlStr.lastIndexOf('.'));
        extension = extension.toLowerCase().replace(".", "");

        return !forbiddenTypes.contains(extension);
    }

}
