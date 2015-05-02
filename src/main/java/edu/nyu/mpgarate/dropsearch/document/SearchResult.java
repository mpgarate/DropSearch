package edu.nyu.mpgarate.dropsearch.document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by mike on 4/27/15.
 */

public class SearchResult implements Comparable<SearchResult> {
    private final SearchQuery searchQuery;
    private Double relevanceScore;
    private final Set<Keyword> matchedKeywords;
    private final URL url;

    public SearchResult(URL url, SearchQuery searchQuery){
        this.url = url;
        this.searchQuery = searchQuery;
        this.matchedKeywords = new HashSet<Keyword>();
        updateRelevanceScore();
    }

    public Double getRelevanceScore() {
        return relevanceScore;
    }

    public URL getUrl(){
        return url;
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

    public void addKeyword(Keyword keyword) {
        matchedKeywords.add(keyword);
        updateRelevanceScore();
    }

    @Override
    public String toString(){
        return new StringBuilder()
                .append("SearchResult {")
                .append("url: ")
                .append(url.toString())
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
                .append(searchQuery)
                .append(url)
                .append(relevanceScore)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof SearchResult)) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        SearchResult sr = (SearchResult) obj;

        return new EqualsBuilder()
                .append(searchQuery, sr.searchQuery)
                .append(url, sr.url)
                .append(relevanceScore, sr.relevanceScore)
                .isEquals();
    }
}
