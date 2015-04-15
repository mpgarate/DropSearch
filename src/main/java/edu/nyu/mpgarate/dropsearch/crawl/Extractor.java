package edu.nyu.mpgarate.dropsearch.crawl;

import edu.nyu.mpgarate.dropsearch.document.WebPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.BreakIterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mike on 4/15/15.
 */
public class Extractor {
    private String body;
    private Document jsoupDoc;
    private String bodyText;
    private URL startUrl;

    private Extractor(String body, URL startUrl){
        this.body = body;
        this.jsoupDoc = Jsoup.parse(body);
        this.bodyText = jsoupDoc.body().text();
        this.startUrl = startUrl;
    }

    public static Extractor fromBody(String body, URL startUrl){
        return new Extractor(body, startUrl);
    }

    public List<String> keywords(){
        LinkedList<String> keyWords = new LinkedList<String>();

        BreakIterator boundary = BreakIterator.getWordInstance();
        boundary.setText(bodyText);

        int start = boundary.first();
        for (int end = boundary.next();
             end != BreakIterator.DONE;
             start = end, end = boundary.next()) {

            String keyWord = bodyText.substring(start, end);
            if (keyWord.length() > 2) {
                keyWords.add(keyWord.toLowerCase());
            }
        }

        return keyWords;
    }

    private URL getUrlBase(URL url){
        try {
            return new URL(url.getProtocol() + "://" + url
                    .getHost());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid URL" + url);
        }
    }

    private boolean urlIsNotSamePageAnchor(URL url){
        String trimmedUrl = url.toString().split("#")[0];

        return !startUrl.toString().equals(trimmedUrl);
    }

    public List<URL> nextURLs(){
        List<URL> nextURLs = new LinkedList<URL>();
        Elements links = jsoupDoc.select("a[href]");

        URL startUrlBase = getUrlBase(startUrl);

        jsoupDoc.setBaseUri(startUrl.toString());

        for (Element link : links){
            String urlStr = link.attr("abs:href");

            if (urlStr.trim().length() > 4){
                try {
                    URL url = new URL(startUrlBase, urlStr);
                    URL urlBase = getUrlBase(url);

                    if (startUrlBase.equals(urlBase) &&
                            urlIsNotSamePageAnchor(url)){

                        nextURLs.add(url);
                        System.out.println(url);
                    }

                } catch (MalformedURLException ignoredException) {
                }
            }
        }

        return new LinkedList<URL>();
    }
}
