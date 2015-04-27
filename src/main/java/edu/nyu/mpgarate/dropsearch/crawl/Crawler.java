package edu.nyu.mpgarate.dropsearch.crawl;

import edu.nyu.mpgarate.dropsearch.document.WebPage;
import edu.nyu.mpgarate.dropsearch.listener.CrawlerListener;
import edu.nyu.mpgarate.dropsearch.storage.SynchronizedKeywordIndex;
import edu.nyu.mpgarate.dropsearch.storage.WebPageStore;
import edu.nyu.mpgarate.dropsearch.util.IOUtil;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by mike on 4/14/15.
 */
public class Crawler {
    private URL startUrl;
    private SynchronizedKeywordIndex index;
    private List<CrawlerListener> listeners;
    private final Integer maxCrawlPages;

    public Crawler(URL startUrl, SynchronizedKeywordIndex index){
        this.startUrl = startUrl;
        this.index = index;
        this.listeners = new LinkedList<CrawlerListener>();
        this.maxCrawlPages = 15;
    }

    public void crawl() {
        Queue<URL> urls = new LinkedList<URL>();

        int pagesVisited = 0;

        urls.add(startUrl);

        while (!urls.isEmpty() && pagesVisited < maxCrawlPages){
            URL url = urls.remove();

            System.out.println(url);

            WebPage webPage = getOrFetchWebPage(url);

            if (null == webPage){
                continue;
            }

            pagesVisited++;

            Extractor extractor = Extractor.fromBody(webPage.getBody(),
                    url);

            index.addAll(extractor.keywords(), webPage.getUrl());

            urls.addAll(extractor.nextUrls());

            System.out.println(url.toString());
            System.out.println(extractor.keywords());

            fireVisitedWebPageEvent(webPage);
        }


    }

    /**
     * Retrieves the webPage from the database or from a web request.
     *
     * @param url
     * @return the webPage or null if retrieval failed
     */
    private WebPage getOrFetchWebPage(URL url){
        WebPageStore webPageStore = new WebPageStore();

        WebPage webPage = webPageStore.get(url);

        String body;

        if (null == webPage){
            try {
                body = IOUtil.getURLAsString(url);
                if (null == body){
                    return null;
                }
            } catch (IOException e) {
                System.out.println("---- could not get page ----");
                System.out.println(url.toString());
                return null;
            }

            System.out.println("---- fetched doc from the web ----");
            webPage = new WebPage(url, body, new Date());

            webPageStore.set(url, webPage);
        } else {
            System.out.println("---- grabbed doc from database ----");
        }

        return webPage;
    }

    public void addListener(CrawlerListener listener){
        listeners.add(listener);
    }

    private void fireVisitedWebPageEvent(WebPage webPage){
        for(CrawlerListener listener : listeners){
            listener.visitedWebPage(webPage);
        }
    }
}
