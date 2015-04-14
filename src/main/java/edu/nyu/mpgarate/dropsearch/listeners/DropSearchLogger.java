package edu.nyu.mpgarate.dropsearch.listeners;

import edu.nyu.mpgarate.dropsearch.model.WebPage;

import java.util.logging.Logger;

/**
 * Created by mike on 4/14/15.
 */
public class DropSearchLogger implements DropSearchListener {
    StringBuilder sb = new StringBuilder();

    public void clear(){
        sb = new StringBuilder();
    }

    public void visitedWebPage(WebPage webPage){
        sb.append(("visitedWebPage(" + webPage + ")"));
    }
}
