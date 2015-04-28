package edu.nyu.mpgarate.dropsearch.retrieve;

import edu.nyu.mpgarate.dropsearch.document.KeywordMatch;
import edu.nyu.mpgarate.dropsearch.document.SearchQuery;
import edu.nyu.mpgarate.dropsearch.document.SearchResult;
import edu.nyu.mpgarate.dropsearch.document.WebPage;
import edu.nyu.mpgarate.dropsearch.storage.SynchronizedKeywordIndex;
import edu.nyu.mpgarate.dropsearch.storage.WebPageStore;

import java.net.URL;
import java.util.*;

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
        Map<URL, SearchResult> results = new HashMap<URL, SearchResult>();

        WebPageStore webPageStore = new WebPageStore();

        for(String term : searchQuery.getTerms()){
            List<KeywordMatch> keywordMatches = index.getWebPageUrls(term);

            for (KeywordMatch keywordMatch: keywordMatches){
                URL url = keywordMatch.getUrl();

                if (results.containsKey(url)){
                    results.get(url).addKeyword(keywordMatch);
                } else {
                    WebPage webPage = webPageStore.get(url);
                    SearchResult searchResult = new SearchResult(webPage, searchQuery);
                    searchResult.addKeyword(keywordMatch);
                    results.put(url, searchResult);
                }
            }
        }

        List<SearchResult> resultsCollection = new ArrayList<SearchResult>
                (results.values());

        Collections.sort(resultsCollection, Collections.reverseOrder());

        return resultsCollection;
    }
}
