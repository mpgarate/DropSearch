package edu.nyu.mpgarate.dropsearch.retrieve;

import edu.nyu.mpgarate.dropsearch.document.SearchQuery;
import edu.nyu.mpgarate.dropsearch.document.SearchResult;
import edu.nyu.mpgarate.dropsearch.document.WebPage;
import edu.nyu.mpgarate.dropsearch.storage.SynchronizedKeywordIndex;
import edu.nyu.mpgarate.dropsearch.storage.WebPageStore;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

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
        Map<WebPage, SearchResult> results = new HashMap<WebPage,
                SearchResult>();

        for(String keyword : searchQuery.getKeywords()){
            for (WebPage webPage : lookupKeyword(keyword)){
                if (results.containsKey(webPage)){
                    results.get(webPage).addKeyword(keyword);
                } else {
                    SearchResult searchResult = new SearchResult(webPage,
                            searchQuery);
                    searchResult.addKeyword(keyword);
                    results.put(webPage, searchResult);
                }
            }
        }

        List<SearchResult> resultsCollection = new ArrayList<SearchResult>
                (results.values());

        Collections.sort(resultsCollection, Collections.reverseOrder());

        return resultsCollection;
    }

    private List<WebPage> lookupKeyword(String keyWord) {
        List<WebPage> webPages = new ArrayList<WebPage>();

        for (URL webPageUrl : index.getWebPageUrls(keyWord)) {
            webPages.add(new WebPageStore().get(webPageUrl));
        }

        return webPages;
    }
}
