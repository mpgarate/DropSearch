package edu.nyu.mpgarate.dropsearch.algorithm.pagerank;

import edu.nyu.mpgarate.dropsearch.crawl.Extractor;
import edu.nyu.mpgarate.dropsearch.document.WebPage;
import edu.nyu.mpgarate.dropsearch.storage.SynchronizedKeywordIndex;
import edu.nyu.mpgarate.dropsearch.storage.WebPageStore;
import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;

import java.net.URI;
import java.util.*;
import java.util.logging.Logger;

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
    private Set<URI> allUrls;

    PageRanker(SynchronizedKeywordIndex index, URI startUrl){
        this.index = index;
        this.startUrl = startUrl;
    }

    void update(){
        LOGGER.info("updating pageRanker");
        WebPageStore webPageStore = new WebPageStore();

        Integer currentEdge = 0;

        synchronized (lock) {
            allUrls = new HashSet(index.getAllUrls());
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
    void evaluate(){
        LOGGER.info("begin evaluate");

        synchronized (lock) {
            pageRank = new PageRank<URI, Integer>(graph, 0.15);

            LOGGER.info("begin pageRank.evaluate()");

            pageRank.evaluate();

            LOGGER.info("eng pageRank.evaluate()");
        }
    }

    public Double getScore(URI url){
        if (null != pageRank) {
            if (allUrls.contains(url)) {
                synchronized (lock) {
                    return pageRank.getVertexScore(url);
                }
            }

            return 0.0;
        } else {
            LOGGER.info("uninitialized pageRanker");
        }

        return 1.0;
    }

}
