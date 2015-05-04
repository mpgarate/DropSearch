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
    private final DirectedGraph<URI, Integer> graph;
    private final Logger LOGGER = Logger.getLogger(PageRanker.class.getName());
    private Integer currentEdge;

    public PageRanker(SynchronizedKeywordIndex index, URI startUrl){
        this.index = index;
        this.startUrl = startUrl;
        this.graph = new DirectedSparseGraph<>();
        this.currentEdge = 0;
    }

    public void update(){
        LOGGER.info("updating pageRanker");

        WebPageStore webPageStore = new WebPageStore();

        List<URI> allUrls = index.getAllUrls();

        LOGGER.info("starting to add urls");

        for (URI url : allUrls){
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

    /**
     * https://github.com/castagna/mr-pagerank/blob/master/src/main/java/com/talis/labs/pagerank/jung/JungPageRank.java
     */
    public void evaluate(){
        LOGGER.info("begin evaluate");
        PageRank<URI, Integer> pageRank = new PageRank<URI, Integer>(graph, 0.15);
        LOGGER.info("begin pageRank.evaluate()");
        pageRank.evaluate();
        LOGGER.info("eng pageRank.evaluate()");

        Map<URI, Double> results = new HashMap<URI, Double>();
        for (URI v : graph.getVertices()) {
            results.put(v, pageRank.getVertexScore(v));
        }

        LOGGER.info("got results");

        Map<URI, Double> resultStream = results.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        LOGGER.info("sorted results");

        for (URI url : resultStream.keySet()){
            LOGGER.info(url.toString());
            LOGGER.info(resultStream.get(url).toString());
        }
    }



}
