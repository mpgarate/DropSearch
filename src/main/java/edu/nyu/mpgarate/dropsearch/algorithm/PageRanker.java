package edu.nyu.mpgarate.dropsearch.algorithm;

import edu.nyu.mpgarate.dropsearch.crawl.Extractor;
import edu.nyu.mpgarate.dropsearch.document.KeywordMatch;
import edu.nyu.mpgarate.dropsearch.document.WebPage;
import edu.nyu.mpgarate.dropsearch.storage.SynchronizedKeywordIndex;
import edu.nyu.mpgarate.dropsearch.storage.WebPageStore;
import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;

import java.net.URI;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by mike on 4/28/15.
 */
public class PageRanker {
    private final SynchronizedKeywordIndex index;
    private final URI startUrl;
    private DirectedGraph<URI, Integer> graph;
    private final Logger LOGGER = Logger.getLogger(PageRanker.class.getName());
    private PageRank<URI, Integer> pageRank;
    private final Object lock = new Object();

    public PageRanker(SynchronizedKeywordIndex index, URI startUrl){
        this.index = index;
        this.startUrl = startUrl;
    }

    public void update(){
        LOGGER.info("updating pageRanker");
        WebPageStore webPageStore = new WebPageStore();

        List<URI> allUrls = index.getAllUrls();

        Integer currentEdge = 0;

        synchronized (lock) {
            graph = new DirectedSparseGraph<>();

            LOGGER.info("starting to add urls");

            for (URI url : allUrls) {
                WebPage webPage = webPageStore.get(url, startUrl);

                Extractor extractor = Extractor.fromBody(webPage.getBody(), url);

                for (URI nextUrl : extractor.nextUrls()) {
                    if (allUrls.contains(nextUrl)) {
                        graph.addEdge(currentEdge, url, nextUrl);
                        currentEdge++;
                    }
                }
            }

            LOGGER.info("done construction.");
        }
    }

    /**
     * https://github.com/castagna/mr-pagerank/blob/master/src/main/java/com/talis/labs/pagerank/jung/JungPageRank.java
     */
    public void evaluate(){
        LOGGER.info("begin evaluate");

        synchronized (lock) {
            pageRank = new PageRank<URI, Integer>(graph, 0.15);

            LOGGER.info("begin pageRank.evaluate()");

            pageRank.evaluate();

            LOGGER.info("eng pageRank.evaluate()");
        }
    }

    public Double getScore(URI url){
        synchronized (lock) {
            if (null == pageRank) {
                LOGGER.info("pageRank is null!");
                return 1.0;
            }

            return pageRank.getVertexScore(url);
        }
    }

}
