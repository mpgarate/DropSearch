package edu.nyu.mpgarate.dropsearch.crawl;

import edu.nyu.mpgarate.dropsearch.Configuration;
import edu.nyu.mpgarate.dropsearch.SearchEngine;
import edu.nyu.mpgarate.dropsearch.document.WebPage;
import edu.nyu.mpgarate.dropsearch.storage.SynchronizedKeywordIndex;
import edu.nyu.mpgarate.dropsearch.storage.WebPageStore;
import edu.nyu.mpgarate.dropsearch.util.IOUtil;

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
    private final Integer maxCrawlPages;
    private final SearchEngine searchEngine;
    private final Object lock;
    private Boolean doneCrawling;
    private Integer pagesCrawled;
    private Boolean stopCrawl;
    private Long lastRetrievalTime;
    private Long crawlPolitenessDelay;


    public Crawler(URI startUrl, SynchronizedKeywordIndex index, SearchEngine
            searchEngine){
        this.startUrl = startUrl;
        this.index = index;
        this.searchEngine = searchEngine;
        this.lock = new Object();
        this.maxCrawlPages = Configuration.getInstance().getMaxCrawlPages();
        this.doneCrawling = false;
        this.pagesCrawled = 0;
        this.stopCrawl = false;
        this.lastRetrievalTime = 0l;
        this.crawlPolitenessDelay = Configuration.getInstance()
                .getCrawlPolitenessDelay();
    }

    public Integer getPagesCrawled(){
        synchronized (lock){
            return pagesCrawled;
        }
    }

    public void stopCrawl(){
        synchronized (lock) {
            this.stopCrawl = true;
        }
    }

    private void setPagesCrawled(Integer pagesCrawled){
        synchronized (lock){
            this.pagesCrawled = pagesCrawled;
        }
    }

    private void setDoneCrawling(){
        synchronized (lock) {
            this.doneCrawling = true;
        }
    }

    public Boolean isDoneCrawling(){
        synchronized (lock){
            return doneCrawling;
        }
    }

    public void crawl() {
        Set<URI> seenUrls = new HashSet<>();
        Queue<URI> urls = new LinkedList<>();


        int pagesVisited = 0;

        urls.add(startUrl);
        seenUrls.add(startUrl);

        while (!urls.isEmpty() && pagesVisited < maxCrawlPages){
            synchronized (lock) {
                if (stopCrawl) {
                    return;
                }
            }

            URI url = urls.remove();

            LOGGER.info("visiting url: " + url);

            WebPage webPage = getOrFetchWebPage(url);

            if (null == webPage){
                continue;
            }

            pagesVisited++;

            if (pagesVisited % Configuration.getInstance()
                    .getPageRankRefreshRate() == 0){
                LOGGER.info("pagesVisited: " + pagesVisited);
                searchEngine.updatePageRank();
            }

            Extractor extractor = Extractor.fromBody(webPage.getBody(), url);

            index.addAll(extractor.keywords(), webPage);

            List<URI> nextUrls = new ArrayList<>();

            // TODO: don't add URIs to queue if there are greater than
            // maxCrawlPages - pagesVisited
            for (URI nextUrl : extractor.nextUrls()){
                if (!seenUrls.contains(nextUrl)) {
                    seenUrls.add(nextUrl);
                    nextUrls.add(nextUrl);
                }
            }

            urls.addAll(nextUrls);

            setPagesCrawled(pagesVisited);
        }

        searchEngine.updatePageRank();
        LOGGER.info("done crawling.");
        setDoneCrawling();
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
            Long timeNow = System.nanoTime();

            // convert nanoseconds difference to milliseconds
            Long delta = (timeNow - lastRetrievalTime) / 1_000_000;

            if (delta < crawlPolitenessDelay){
                try {
                    Long sleepDuration = crawlPolitenessDelay - delta;
                    Thread.sleep(sleepDuration);
                } catch (InterruptedException ignoredException) {
                }
            }

            try {
                body = IOUtil.getURLAsString(url.toURL());

                lastRetrievalTime = System.nanoTime();

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
        }

        return webPage;
    }
}
