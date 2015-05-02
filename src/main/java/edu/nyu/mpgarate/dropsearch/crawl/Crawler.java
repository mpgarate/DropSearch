package edu.nyu.mpgarate.dropsearch.crawl;

import edu.nyu.mpgarate.dropsearch.document.WebPage;
import edu.nyu.mpgarate.dropsearch.storage.SynchronizedKeywordIndex;
import edu.nyu.mpgarate.dropsearch.storage.WebPageStore;
import edu.nyu.mpgarate.dropsearch.util.IOUtil;
import edu.nyu.mpgarate.dropsearch.util.listener.CrawlerListener;

import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by mike on 4/14/15.
 */
public class Crawler {
    private final Logger LOGGER = Logger.getLogger(Crawler.class.getName());
    private final URI startUrl;
    private final SynchronizedKeywordIndex index;
    private final List<CrawlerListener> listeners;
    private final Integer maxCrawlPages;

    public Crawler(URI startUrl, SynchronizedKeywordIndex index){
        this.startUrl = startUrl;
        this.index = index;
        this.listeners = new LinkedList<CrawlerListener>();
        this.maxCrawlPages = 50;
    }



    public void crawl() {
        Set<URI> seenUrls = new HashSet<URI>();
        Queue<URI> urls = new LinkedList<>();


        int pagesVisited = 0;

        urls.add(startUrl);
        seenUrls.add(startUrl);

        while (!urls.isEmpty() && pagesVisited < maxCrawlPages){

            URI url = urls.remove();

            LOGGER.info("visiting url: " + url);

            WebPage webPage = getOrFetchWebPage(url);

            if (null == webPage){
                continue;
            }

            pagesVisited++;

            if (pagesVisited % 100 == 0){
                LOGGER.info("pagesVisited: " + pagesVisited);
            }

            Extractor extractor = Extractor.fromBody(webPage.getBody(), url);

            index.addAll(extractor.keywords(), webPage);

            List<URI> nextUrls = new ArrayList<URI>();

            // TODO: don't add URIs to queue if there are greater than
            // maxCrawlPages - pagesVisited
            for (URI nextUrl : extractor.nextUrls()){
                if (!seenUrls.contains(nextUrl)) {
                    seenUrls.add(nextUrl);
                    nextUrls.add(nextUrl);
                }
            }

            urls.addAll(nextUrls);

            fireVisitedWebPageEvent(webPage);
        }

    }

    /**
     * Retrieves the webPage from the database or from a web request.
     *
     * @param url
     * @return the webPage or null if retrieval failed
     */
    private WebPage getOrFetchWebPage(URI url){
        WebPageStore webPageStore = new WebPageStore();

        WebPage webPage = webPageStore.get(url, startUrl);

        String body;

        if (null == webPage){
            try {
                body = IOUtil.getURLAsString(url.toURL());
                if (null == body){
                    LOGGER.info("---- got null body ----");
                    return null;
                }
            } catch (IOException e) {
                LOGGER.info("---- could not get page ----");
                LOGGER.info(e.toString());
                LOGGER.info(url.toString());
                return null;
            }

            LOGGER.info("---- fetched doc from the web ----");
            LOGGER.info(url.toString());
            webPage = new WebPage(url, body, new Date(), startUrl);

            webPageStore.save(webPage);
        } else {
//            LOGGER.info("---- grabbed doc from database ----");
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
