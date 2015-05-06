package edu.nyu.mpgarate.dropsearch.document;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Transient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by mike on 4/14/15.
 */

@Entity
public class WebPage {
    @Transient
    private final Logger LOGGER = Logger.getLogger(WebPage.class.getName());

    @Id
    private ObjectId id;

    @Indexed(unique = true, dropDups=true)
    private String url;
    private String body;
    private Date dateVisited;
    @Indexed
    private String startUrl;

    private WebPage(){

    }

    public WebPage(URI url, String body, Date dateVisited, URI startUrl){
        this.url = url.toString();
        this.body = body;
        this.dateVisited = new Date(dateVisited.getTime());
        this.startUrl = startUrl.toString();
    }

    public ObjectId getId(){
        return id;
    }

    public URI getUrl(){
        try {
            return new URI(url);
        } catch (URISyntaxException e) {
            LOGGER.warning("Malformed URI from Mongo. ");
            LOGGER.warning(e.toString());
            return null;
        }
    }

    public URI getStartUrl(){
        try {
            return new URI(startUrl);
        } catch (URISyntaxException e) {
            LOGGER.warning("Malformed URI from Mongo. ");
            LOGGER.warning(e.toString());
            return null;
        }
    }

    public String getBody(){
        return body;
    }

    private void setBody(String body){
        this.body = body;
    }

    public Date getDateVisited(){
        return new Date(dateVisited.getTime());
    }

    private void setDateVisited(Date dateVisited){
        this.dateVisited = dateVisited;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();

        return sb
                .append("WebPage { ")
                .append("url: ")
                .append(url)
                .append(", dateVisited: ")
                .append(dateVisited.toString())
                .append(" }")
                .toString();
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
