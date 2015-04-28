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
        this.relevanceScore = 1.0;
    }

    public WebPage getWebPage(){
        return webPage;
    }

    public Double getRelevanceScore(){
        return relevanceScore;
    }

    @Override
    public int compareTo(SearchResult otherSearchResult) {
        return getRelevanceScore()
                .compareTo(otherSearchResult.getRelevanceScore());
    }
}
