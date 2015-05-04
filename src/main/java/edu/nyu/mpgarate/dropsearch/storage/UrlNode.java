package edu.nyu.mpgarate.dropsearch.storage;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bson.types.ObjectId;

import java.net.URI;

/**
 * Created by mike on 4/28/15.
 */
public class UrlNode implements Comparable<UrlNode> {
    private final ObjectId urlId;
    private final Double pageWeight;

    public UrlNode(ObjectId urlId, Double pageWeight){
        this.urlId = urlId;
        this.pageWeight = pageWeight;
    }

    public ObjectId getUrlId(){
        return urlId;
    }

    public Double getPageWeight(){
        return pageWeight;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(59, 23)
                .append(urlId)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof UrlNode)) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        UrlNode un = (UrlNode) obj;

        return new EqualsBuilder()
                .append(urlId, un.urlId)
                .isEquals();
    }

    @Override
    public String toString(){
        return new StringBuilder()
                .append("UrlNode {")
                .append("urlId: ")
                .append(urlId.toString())
                .append(", pageWeight: ")
                .append(pageWeight)
                .append(" }")
                .toString();
    }

    @Override
    public int compareTo(UrlNode otherUrlNode) {
        return pageWeight.compareTo(otherUrlNode.getPageWeight());
    }
}
