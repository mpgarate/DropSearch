package edu.nyu.mpgarate.dropsearch.document;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.net.URL;
import java.util.Date;

/**
 * Created by mike on 4/14/15.
 */
public class WebPage {
    private URL url;
    private String body;
    private Date dateVisited;

    private WebPage(){

    }

    public WebPage(URL url, String body, Date dateVisited){
        this.url = url;
        this.body = body;
        this.dateVisited = dateVisited;
    }

    private WebPage(URL url, String body, Date dateVisited, Document
            mongoDocument){
        this(url, body, dateVisited);
    }

    public URL getUrl(){
        return url;
    }

    private void setURl(URL url){
        this.url = url;
    }

    public String getBody(){
        return body;
    }

    private void setBody(String body){
        this.body = body;
    }

    public Date getDateVisited(){
        return dateVisited;
    }

    private void setDateVisited(Date dateVisited){
        this.dateVisited = dateVisited;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(59, 23)
                .append(url)
                .append(body)
                .append(dateVisited)
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

        WebPage wp = (WebPage) obj;

        return new EqualsBuilder()
                .append(url, wp.url)
                .append(body, wp.body)
                .append(dateVisited, wp.dateVisited)
                .isEquals();

    }
}
