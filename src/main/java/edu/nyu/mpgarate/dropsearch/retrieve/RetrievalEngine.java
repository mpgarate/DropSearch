package edu.nyu.mpgarate.dropsearch.retrieve;

import edu.nyu.mpgarate.dropsearch.algorithm.SearchResultRelevanceCalc;
import edu.nyu.mpgarate.dropsearch.algorithm.pagerank.PageRankerManager;
import edu.nyu.mpgarate.dropsearch.document.KeywordMatch;
import edu.nyu.mpgarate.dropsearch.document.SearchQuery;
import edu.nyu.mpgarate.dropsearch.document.SearchResult;
import edu.nyu.mpgarate.dropsearch.storage.SynchronizedKeywordIndex;
import edu.nyu.mpgarate.dropsearch.storage.SynchronizedUriMap;
import org.bson.types.ObjectId;

import javax.validation.constraints.Null;
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
    private final SynchronizedUriMap uriMap;
    private final PageRankerManager pageRankerManager;

    public RetrievalEngine(URI startUrl, SynchronizedKeywordIndex index,
                           PageRankerManager pageRankerManager,
                           SynchronizedUriMap uriMap){
        this.startUrl = startUrl;
        this.index = index;
        this.uriMap = uriMap;
        this.pageRankerManager = pageRankerManager;
    }

    public List<SearchResult> getWebPages(SearchQuery searchQuery){
        Map<ObjectId, SearchResult> results = new HashMap<>();

        SearchResultRelevanceCalc relevanceCalc = new SearchResultRelevanceCalc
                    (pageRankerManager.current());

        for(String term : searchQuery.getTerms()){
            LOGGER.info("looking at matches for term: " + term);

            for (KeywordMatch keywordMatch : index.getKeywordMatches(term)){
                ObjectId urlId = keywordMatch.getUrlId();

                if (results.containsKey(urlId)){
                    results.get(urlId).addKeyword(keywordMatch);
                } else {
                    URI uri = uriMap.getUri(urlId);

                    if (null == uri){
                        LOGGER.warning("got no url for id: " + urlId);
                        throw new NullPointerException();
                    }

                    SearchResult searchResult = new SearchResult(uriMap
                            .getUri(urlId),
                            searchQuery);
                    searchResult.addKeyword(keywordMatch);
                    results.put(urlId, searchResult);
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
