package edu.nyu.mpgarate.dropsearch.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by mike on 4/27/15.
 */

public class SearchResult implements Comparable<SearchResult> {
    private final SearchQuery searchQuery;
    private Double relevanceScore;
    @JsonIgnore
    private final Set<Keyword> matchedKeywords;
    private final URI url;
    private String title;

    public SearchResult(URI url, SearchQuery searchQuery){
        this.url = url;
        this.searchQuery = searchQuery;
        this.matchedKeywords = new HashSet<Keyword>();
        this.relevanceScore = -1.0;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public Double getRelevanceScore() {
        return relevanceScore;
    }

    public void setRelevanceScore(Double relevanceScore){
        this.relevanceScore = relevanceScore;
    }

    public URI getUrl(){
        return url;
    }

    public List<Keyword> getMatchedKeywords(){
        return new ArrayList(matchedKeywords);
    }

    public void addKeyword(Keyword keyword) {
        matchedKeywords.add(keyword);
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
