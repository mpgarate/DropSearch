package edu.nyu.mpgarate.dropsearch.algorithm;

import edu.nyu.mpgarate.dropsearch.algorithm.pagerank.PageRanker;
import edu.nyu.mpgarate.dropsearch.document.Keyword;
import edu.nyu.mpgarate.dropsearch.document.SearchResult;

import java.util.logging.Logger;

/**
 * Created by mike on 5/3/15.
 */
public class SearchResultRelevanceCalc {
    private final static Logger LOGGER = Logger.getLogger
            (SearchResultRelevanceCalc.class.getName());
    private PageRanker pageRanker;

    public SearchResultRelevanceCalc(PageRanker pageRanker){
        this.pageRanker = pageRanker;
    }

    public Double getRelevanceScore(SearchResult searchResult){
        Double relevanceScore = 0.0;

        for (Keyword keyword : searchResult.getMatchedKeywords()){
            relevanceScore += keyword.getWeight();
        }

        Double pageRankScore = pageRanker.getScore(searchResult.getUrl());

        relevanceScore *= pageRankScore;

        LOGGER.info("relevance " + pageRankScore + " for: " + searchResult
                        .getUrl());

        return relevanceScore;
    }
}
