package edu.nyu.mpgarate.dropsearch.retrieve;

import edu.nyu.mpgarate.dropsearch.document.WebPage;
import edu.nyu.mpgarate.dropsearch.storage.SynchronizedKeywordIndex;
import edu.nyu.mpgarate.dropsearch.storage.WebPageStore;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mike on 4/14/15.
 */
public class RetrievalEngine {
    private URL startUrl;
    private SynchronizedKeywordIndex index;

    public RetrievalEngine(URL startUrl, SynchronizedKeywordIndex index){
        this.startUrl = startUrl;
        this.index = index;
    }

    public List<WebPage> getWebPages(String query){
        List<URL> webPageUrls = index.getWebPageUrls(query);
        List<WebPage> webPages = new ArrayList<WebPage>();

        for(URL webPageUrl : webPageUrls){
            WebPage webPage = new WebPageStore().get(webPageUrl);
            webPages.add(webPage);
        }

        return webPages;
    }
}
