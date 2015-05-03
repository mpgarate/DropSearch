package edu.nyu.mpgarate.dropsearch.retrieve;

import edu.nyu.mpgarate.dropsearch.algorithm.PageRanker;
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

    public RetrievalEngine(URI startUrl, SynchronizedKeywordIndex index){
        this.startUrl = startUrl;
        this.index = index;
    }

    public List<SearchResult> getWebPages(SearchQuery searchQuery){
        Map<URI, SearchResult> results = new HashMap<URI, SearchResult>();

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


        new PageRanker(index, startUrl).evaluate();

        LOGGER.info("sorting retrieved results");

        Collections.sort(resultsCollection, Collections.reverseOrder());

        LOGGER.info("done sorting retrieved results");

        return resultsCollection;
    }
}
