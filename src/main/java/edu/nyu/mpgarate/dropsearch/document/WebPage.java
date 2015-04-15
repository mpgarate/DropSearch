package edu.nyu.mpgarate.dropsearch.document;

import java.net.URL;

/**
 * Created by mike on 4/14/15.
 */
public class WebPage {
    private URL url;

    public WebPage(URL url){
        this.url = url;
    }

    public URL getUrl(){
        return url;
    }
}
