package edu.nyu.mpgarate.dropsearch.retrieve;

import edu.nyu.mpgarate.dropsearch.document.*;
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
        Map<URL, SearchResult> results = new HashMap<URL, SearchResult>();

        WebPageStore webPageStore = new WebPageStore();

        for(String term: searchQuery.getKeywords()){
            List<KeywordMatch> termMatches = index.getWebPageUrls(term);

            for (KeywordMatch termMatch : termMatches){
                URL url = termMatch.getUrl();

                if (results.containsKey(url)){
                    results.get(url).addKeyword(termMatch);
                } else {
                    WebPage webPage = webPageStore.get(url);
                    SearchResult searchResult = new SearchResult(webPage, searchQuery);
                    searchResult.addKeyword(termMatch);
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
