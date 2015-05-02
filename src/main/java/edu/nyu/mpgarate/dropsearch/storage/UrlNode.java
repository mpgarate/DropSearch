package edu.nyu.mpgarate.dropsearch.storage;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.net.URL;

/**
 * Created by mike on 4/28/15.
 */
class UrlNode implements Comparable<UrlNode> {
    private final URL url;
    private final Double pageWeight;

    public UrlNode(URL url, Double pageWeight){
        this.url = url;
        this.pageWeight = pageWeight;
    }

    public URL getUrl(){
        return url;
    }

    public Double getPageWeight(){
        return pageWeight;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(59, 23)
                .append(url)
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
                .append(url, un.url)
                .isEquals();
    }

    @Override
    public String toString(){
        return new StringBuilder()
                .append("UrlNode {")
                .append("url: ")
                .append(url.toString())
                .append(", pageWeight: ")
                .append(pageWeight)
                .append(" }")
                .toString();
    }

    @Override
    public int compareTo(UrlNode otherUrlNode) {
        return pageWeight.compareTo(otherUrlNode.getPageWeight()) + url
                .toString().compareTo(otherUrlNode.getUrl().toString());
    }
}
