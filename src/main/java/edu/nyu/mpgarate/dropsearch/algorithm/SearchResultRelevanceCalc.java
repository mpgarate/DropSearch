package edu.nyu.mpgarate.dropsearch.algorithm;

import edu.nyu.mpgarate.dropsearch.Configuration;
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

    private final Configuration conf;

    public SearchResultRelevanceCalc(PageRanker pageRanker){
        this.pageRanker = pageRanker;
        this.conf = Configuration.getInstance();

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

        relevanceScore *= getKeywordsScore(keywords) * conf
                .getKeywordsWeight();

        Double pageRankScore = pageRanker.getScore(searchResult.getUrl());

        // since this pageRanker may not be complete, we boost the scores so
        // that scores of 0 do not eliminate a page from consideration.
        relevanceScore *= (pageRankScore + conf.getPageRankOffset()) * conf
                .getPageRankWeight();

        LOGGER.info("pageRank: " + searchResult
                        .getUrl() + " : " + pageRankScore);

        LOGGER.info("keywordsScore: " + searchResult
                .getUrl() + " : " + getKeywordsScore(keywords));

        return relevanceScore;
    }
}
