package edu.nyu.mpgarate.dropsearch.document;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bson.types.ObjectId;

/**
 * Created by mike on 4/28/15.
 */
public class KeywordMatch extends Keyword {
    private final ObjectId urlId;

    public KeywordMatch(String term, Double weight, ObjectId urlId){
        super(term, weight);
        this.urlId = urlId;
    }

    public ObjectId getUrlId(){
        return urlId;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(59, 23)
                .append(urlId)
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
                .append(urlId, kw.urlId)
                .append(getTerm(), kw.getTerm())
                .append(getWeight(), kw.getWeight())
                .isEquals();
    }
}
