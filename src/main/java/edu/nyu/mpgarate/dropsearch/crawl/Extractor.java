package edu.nyu.mpgarate.dropsearch.crawl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.BreakIterator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by mike on 4/15/15.
 */
public class Extractor {
    private Document jsoupDoc;
    private String bodyText;
    private URL startUrl;
    private URL startUrlBase;

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
    public List<String> keywords() {
        LinkedList<String> keyWords = new LinkedList<String>();

        BreakIterator boundary = BreakIterator.getWordInstance();
        boundary.setText(bodyText);

        int start = boundary.first();
        for (int end = boundary.next();
             end != BreakIterator.DONE;
             start = end, end = boundary.next()) {

            String keyWord = bodyText.substring(start, end);
            keyWord = keyWord.replaceAll("(?U)\\s", "");
            if (keyWord.length() > 2) {
                keyWords.add(keyWord.toLowerCase());
            }
        }

        return keyWords;
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
