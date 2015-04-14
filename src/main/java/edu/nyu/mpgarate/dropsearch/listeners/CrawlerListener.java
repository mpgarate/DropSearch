package edu.nyu.mpgarate.dropsearch.listeners;

import edu.nyu.mpgarate.dropsearch.model.WebPage;

/**
 * Created by mike on 4/14/15.
 */
public interface CrawlerListener {
    public void visitedWebPage(WebPage webPage);
}
