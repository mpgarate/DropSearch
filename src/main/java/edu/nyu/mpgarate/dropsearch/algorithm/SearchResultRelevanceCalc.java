package edu.nyu.mpgarate.dropsearch.algorithm;

import edu.nyu.mpgarate.dropsearch.algorithm.pagerank.PageRanker;
import edu.nyu.mpgarate.dropsearch.document.Keyword;
import edu.nyu.mpgarate.dropsearch.document.SearchResult;

import java.util.List;
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

    private Double getKeywordsScore(List<Keyword> keywords){
        Double score = 0.0;

        for (Keyword keyword : keywords){
            score += keyword.getWeight();

            score = score / keywords.size();
        }

        return score;
    }

    public Double getRelevanceScore(SearchResult searchResult){
        Double relevanceScore = 1.0;

        List<Keyword> keywords = searchResult.getMatchedKeywords();

        relevanceScore *= getKeywordsScore(keywords) * 0.65;

        Double pageRankScore = pageRanker.getScore(searchResult.getUrl());

        relevanceScore *= pageRankScore * 0.45;

        LOGGER.info("relevance " + pageRankScore + " for: " + searchResult
                        .getUrl());

        return relevanceScore;
    }
}
