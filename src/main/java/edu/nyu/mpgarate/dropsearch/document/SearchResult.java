package edu.nyu.mpgarate.dropsearch.document;

/**
 * Created by mike on 4/27/15.
 */
public class SearchResult implements Comparable<SearchResult> {
    private WebPage webPage;
    private SearchQuery searchQuery;
    private Double relevanceScore;

    public SearchResult(WebPage webPage, SearchQuery searchQuery){
        this.webPage = webPage;
        this.searchQuery = searchQuery;
    }

    public WebPage getWebPage(){
        return webPage;
    }

    public Double getRelevanceScore(){
        if (null != relevanceScore){
            return relevanceScore;
        }

        //   pageRank() * pageRankWeight
        // + termRelevance() * termRelevanceWeight
        // +
        // def termRelevance(){
        //
        // }

        return 1.0;
    }

    @Override
    public int compareTo(SearchResult otherSearchResult) {
        return getRelevanceScore()
                .compareTo(otherSearchResult.getRelevanceScore());
    }
}
