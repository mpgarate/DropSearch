package edu.nyu.mpgarate.dropsearch.retrieve;

import edu.nyu.mpgarate.dropsearch.document.SearchQuery;
import edu.nyu.mpgarate.dropsearch.document.SearchResult;
import edu.nyu.mpgarate.dropsearch.document.WebPage;
import edu.nyu.mpgarate.dropsearch.storage.SynchronizedKeywordIndex;
import edu.nyu.mpgarate.dropsearch.storage.WebPageStore;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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

    public List<SearchResult> getWebPages(SearchQuery searchQuery){
        Set<SearchResult> sortedSearchResults = new TreeSet<SearchResult>();

        for(String keyword : searchQuery.keywords()){
            for (WebPage webPage : lookupKeyword(keyword)){
                sortedSearchResults.add(new SearchResult(webPage, searchQuery));
            }
        }

        return new ArrayList<SearchResult>(sortedSearchResults);
    }

    private List<WebPage> lookupKeyword(String keyWord) {
        List<WebPage> webPages = new ArrayList<WebPage>();

        for (URL webPageUrl : index.getWebPageUrls(keyWord)) {
            webPages.add(new WebPageStore().get(webPageUrl));
        }

        return webPages;
    }
}
