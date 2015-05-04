package edu.nyu.mpgarate.dropsearch.retrieve;

import edu.nyu.mpgarate.dropsearch.algorithm.pagerank.PageRanker;
import edu.nyu.mpgarate.dropsearch.algorithm.SearchResultRelevanceCalc;
import edu.nyu.mpgarate.dropsearch.algorithm.pagerank.PageRankerManager;
import edu.nyu.mpgarate.dropsearch.document.KeywordMatch;
import edu.nyu.mpgarate.dropsearch.document.SearchQuery;
import edu.nyu.mpgarate.dropsearch.document.SearchResult;
import edu.nyu.mpgarate.dropsearch.storage.SynchronizedKeywordIndex;

import java.net.URI;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by mike on 4/14/15.
 */
public class RetrievalEngine {
    private final Logger LOGGER = Logger.getLogger(RetrievalEngine.class.getName
            ());
    private final URI startUrl;
    private final SynchronizedKeywordIndex index;
    private final PageRankerManager pageRankerManager;

    public RetrievalEngine(URI startUrl, SynchronizedKeywordIndex index,
                           PageRankerManager pageRankerManager){
        this.startUrl = startUrl;
        this.index = index;
        this.pageRankerManager = pageRankerManager;
    }

    public List<SearchResult> getWebPages(SearchQuery searchQuery){
        Map<URI, SearchResult> results = new HashMap<URI, SearchResult>();

        SearchResultRelevanceCalc relevanceCalc = new SearchResultRelevanceCalc
                    (pageRankerManager.current());

        for(String term : searchQuery.getTerms()){
            LOGGER.info("looking at matches for term: " + term);

            for (KeywordMatch keywordMatch : index.getKeywordMatches(term)){
                URI url = keywordMatch.getUrl();

                if (results.containsKey(url)){
                    results.get(url).addKeyword(keywordMatch);
                } else {
                    SearchResult searchResult = new SearchResult(url,
                            searchQuery);
                    searchResult.addKeyword(keywordMatch);
                    results.put(url, searchResult);
                }
            }
            LOGGER.info("done looking at matches for term: " + term);
        }

        List<SearchResult> resultsCollection = new ArrayList<SearchResult>
                (results.values());

        for (SearchResult searchResult : resultsCollection){
            Double score = relevanceCalc.getRelevanceScore(searchResult);
            searchResult.setRelevanceScore(score);
        }

        LOGGER.info("sorting retrieved results");

        Collections.sort(resultsCollection, Collections.reverseOrder());

        LOGGER.info("done sorting retrieved results");

        if (resultsCollection.size() < 25){
            return resultsCollection;
        } else {
            return resultsCollection.subList(0, 25);
        }
    }
}
