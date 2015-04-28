package edu.nyu.mpgarate.dropsearch.document;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by mike on 4/27/15.
 */
public class SearchResult implements Comparable<SearchResult> {
    private WebPage webPage;
    private SearchQuery searchQuery;
    private Double relevanceScore;
    private Set<Keyword> matchedKeywords;

    public SearchResult(WebPage webPage, SearchQuery searchQuery){
        this.webPage = webPage;
        this.searchQuery = searchQuery;
        this.matchedKeywords = new HashSet<Keyword>();
        updateRelevanceScore();
    }

    public WebPage getWebPage(){
        return webPage;
    }

    public Double getRelevanceScore() {
        return relevanceScore;
    }

    private void updateRelevanceScore(){

        //   pageRank() * pageRankWeight
        // + termRelevance() * termRelevanceWeight
        // +
        // def termRelevance(){
        //
        // }

        Double relevanceScore = 0.0;

        for (Keyword kw : matchedKeywords){
            relevanceScore += kw.getWeight();
        }

        this.relevanceScore = relevanceScore;
    }

    public void addKeyword(Keyword keyword){
        matchedKeywords.add(keyword);
        updateRelevanceScore();
    }

    @Override
    public String toString(){
        return new StringBuilder()
                .append("SearchResult {")
                .append("url: ")
                .append(webPage.getUrl())
                .append(", relevanceScore: ")
                .append(relevanceScore)
                .append(" }")
                .toString();
    }

    @Override
    public int compareTo(SearchResult otherSearchResult) {
        return getRelevanceScore()
                .compareTo(otherSearchResult.getRelevanceScore());
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(59, 23)
                .append(webPage)
                .append(searchQuery)
                .append(relevanceScore)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof WebPage)) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        SearchResult sr = (SearchResult) obj;

        return new EqualsBuilder()
                .append(webPage, sr.webPage)
                .append(searchQuery, sr.searchQuery)
                .append(relevanceScore, sr.relevanceScore)
                .isEquals();
    }
}
