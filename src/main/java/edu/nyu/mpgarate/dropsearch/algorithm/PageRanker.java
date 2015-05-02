package edu.nyu.mpgarate.dropsearch.algorithm;

import edu.nyu.mpgarate.dropsearch.crawl.Extractor;
import edu.nyu.mpgarate.dropsearch.document.KeywordMatch;
import edu.nyu.mpgarate.dropsearch.document.WebPage;
import edu.nyu.mpgarate.dropsearch.storage.SynchronizedKeywordIndex;
import edu.nyu.mpgarate.dropsearch.storage.WebPageStore;
import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;

import java.net.URL;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by mike on 4/28/15.
 */
public class PageRanker {
    private final DirectedGraph<URL, Integer> graph = new
            DirectedSparseGraph<URL, Integer>();

    private final Logger LOGGER = Logger.getLogger(PageRanker.class.getName());

    public PageRanker(SynchronizedKeywordIndex index, URL startUrl){
        LOGGER.info("constructing pageRanker");
        Integer currentEdge = 0;

        WebPageStore webPageStore = new WebPageStore();

        Set<URL> allUrls = new HashSet<URL>(index.getAllUrls());

        LOGGER.info("starting to add urls");
        for (URL url : allUrls){
            WebPage webPage = webPageStore.get(url, startUrl);

            Extractor extractor = Extractor.fromBody(webPage.getBody(), url);

            for (URL nextUrl : extractor.nextUrls()) {
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
        PageRank<URL, Integer> pageRank = new PageRank<URL, Integer>(graph, 0.85);
        LOGGER.info("begin pageRank.evaluate()");
        pageRank.evaluate();
        LOGGER.info("eng pageRank.evaluate()");

        Map<URL, Double> results = new HashMap<URL, Double>();
        for (URL v : graph.getVertices()) {
            results.put(v, pageRank.getVertexScore(v));
        }

        LOGGER.info("got results");

        Map<URL, Double> resultStream = results.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .limit(10)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        LOGGER.info("sorted results");

        for (URL url : resultStream.keySet()){
            LOGGER.info(url.toString());
            LOGGER.info(resultStream.get(url).toString());
        }
    }



}
