package edu.nyu.mpgarate.dropsearch.document;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.net.URI;

/**
 * Created by mike on 4/28/15.
 */
public class KeywordMatch extends Keyword {
    private final URI url;

    public KeywordMatch(String term, Double weight, URI url){
        super(term, weight);
        this.url = url;
    }

    public URI getUrl(){
        return url;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(59, 23)
                .append(url)
                .append(getTerm())
                .append(getWeight())
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

        KeywordMatch kw = (KeywordMatch) obj;

        return new EqualsBuilder()
                .append(url, kw.url)
                .append(getTerm(), kw.getTerm())
                .append(getWeight(), kw.getWeight())
                .isEquals();
    }
}
