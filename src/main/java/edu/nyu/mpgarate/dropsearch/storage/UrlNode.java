package edu.nyu.mpgarate.dropsearch.storage;

import java.net.URL;

/**
 * Created by mike on 4/28/15.
 */
class UrlNode implements Comparable<UrlNode> {
    private URL url;
    private Double pageWeight;

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
    public int compareTo(UrlNode otherUrlNode) {
        return pageWeight.compareTo(otherUrlNode.getPageWeight());
    }
}
