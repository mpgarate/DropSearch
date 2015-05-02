package edu.nyu.mpgarate.dropsearch.document;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by mike on 4/28/15.
 */
public class Keyword {
    private final String term;
    private final Double weight;

    public Keyword(String term, Double weight){
        this.term = term;
        this.weight = weight;
    }

    public String getTerm(){
        return term;
    }

    public Double getWeight(){
        return weight;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(59, 23)
                .append(term)
                .append(weight)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof Keyword)) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        Keyword kw = (Keyword) obj;

        return new EqualsBuilder()
                .append(term, kw.term)
                .append(weight, kw.weight)
                .isEquals();
    }
}
