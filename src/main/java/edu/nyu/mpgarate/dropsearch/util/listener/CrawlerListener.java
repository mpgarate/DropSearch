package edu.nyu.mpgarate.dropsearch.util.listener;

import edu.nyu.mpgarate.dropsearch.document.WebPage;

/**
 * Created by mike on 4/14/15.
 */
public interface CrawlerListener {
    public void visitedWebPage(WebPage webPage);
}
