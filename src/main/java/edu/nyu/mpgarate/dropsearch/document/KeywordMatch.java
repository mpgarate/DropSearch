package edu.nyu.mpgarate.dropsearch.document;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.net.URL;

/**
 * Created by mike on 4/28/15.
 */
public class KeywordMatch extends Keyword {
    private URL url;

    public KeywordMatch(String term, Double weight, URL url){
        super(term, weight);
        this.url = url;
    }

    public URL getUrl(){
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
