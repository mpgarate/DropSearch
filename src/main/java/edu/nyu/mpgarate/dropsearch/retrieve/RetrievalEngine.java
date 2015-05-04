package edu.nyu.mpgarate.dropsearch.retrieve;

import edu.nyu.mpgarate.dropsearch.algorithm.SearchResultRelevanceCalc;
import edu.nyu.mpgarate.dropsearch.algorithm.pagerank.PageRankerManager;
import edu.nyu.mpgarate.dropsearch.crawl.Extractor;
import edu.nyu.mpgarate.dropsearch.document.*;
import edu.nyu.mpgarate.dropsearch.storage.SynchronizedKeywordIndex;
import edu.nyu.mpgarate.dropsearch.storage.SynchronizedUriMap;
import edu.nyu.mpgarate.dropsearch.storage.UrlNode;
import edu.nyu.mpgarate.dropsearch.storage.WebPageStore;
import org.bson.types.ObjectId;

import javax.validation.constraints.Null;
import java.net.URI;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by mike on 4/14/15.
 */
public class RetrievalEngine {
    private final Logger LOGGER = Logger.getLogger(RetrievalEngine.class.getName
            ());
    private final URI startUrl;
    private final SynchronizedKeywordIndex index;
    private final SynchronizedUriMap uriMap;
    private final PageRankerManager pageRankerManager;

    public RetrievalEngine(URI startUrl, SynchronizedKeywordIndex index,
                           PageRankerManager pageRankerManager,
                           SynchronizedUriMap uriMap){
        this.startUrl = startUrl;
        this.index = index;
        this.uriMap = uriMap;
        this.pageRankerManager = pageRankerManager;
    }

    private List<KeywordMatch> getMatchesAllTerms(SearchQuery searchQuery){
        Map<String, List<UrlNode>> termMatches = new HashMap<>();

        for(String term : searchQuery.getTerms()) {
            LOGGER.info("getting matches for term: " + term);
            termMatches.put(term, index.getKeywordMatches(term));
            LOGGER.info("done getting matches for term: " + term);
        }

        Map<ObjectId, List<KeywordMatch>> objectIdListMap = new HashMap<>();

        for (Map.Entry<String, List<UrlNode>> pair : termMatches.entrySet()){
            List<UrlNode> matches = pair.getValue();
            String term = pair.getKey();

            for (UrlNode urlNode : matches){
                ObjectId urlId = urlNode.getUrlId();
                List<KeywordMatch> matchUrlNodes = objectIdListMap.get(urlId);

                if (null == matchUrlNodes){
                    matchUrlNodes = new ArrayList<>();
                }

                matchUrlNodes.add(new KeywordMatch(term, urlNode
                        .getPageWeight(), urlId));

                objectIdListMap.put(urlId, matchUrlNodes);
            }
        }

        List<KeywordMatch> matchesAllTerms = new ArrayList<>();

        for (Map.Entry<ObjectId, List<KeywordMatch>> pair : objectIdListMap
                .entrySet()){
            List<KeywordMatch> matches = pair.getValue();
            ObjectId urlId = pair.getKey();

            if (matches.size() == searchQuery.getTerms().size()){
                Double avgScore = 1.0;

                for (KeywordMatch km : matches){
                    avgScore *= km.getWeight();
                }

                KeywordMatch oldKeywordMatch = matches.get(0);

                KeywordMatch keywordMatch = new KeywordMatch(oldKeywordMatch
                        .getTerm(), avgScore, oldKeywordMatch.getUrlId());

                matchesAllTerms.add(keywordMatch);
            }
        }

        return matchesAllTerms;
    }


    private List<KeywordMatch> getMatchesAnyTerms(SearchQuery searchQuery){
        Map<ObjectId, KeywordMatch> matchesAnyTerms = new HashMap<>();

        for(String term : searchQuery.getTerms()){
            List<UrlNode> termMatches = index.getKeywordMatches(term);
            for (UrlNode urlNode : termMatches){
                ObjectId urlId = urlNode.getUrlId();
                KeywordMatch match = matchesAnyTerms.get(urlId);

                if (null == match){
                    match = new KeywordMatch(term, urlNode.getPageWeight(),
                            urlId);
                } else {
                    Double nodeWeight = urlNode.getPageWeight();
                    Double matchWeight = match.getWeight();
                    match = new KeywordMatch(term, nodeWeight *
                            matchWeight,
                            urlId);
                }

                matchesAnyTerms.put(urlId, match);
            }
        }

        return new ArrayList<KeywordMatch>(matchesAnyTerms.values());
    }

    /**
     * referenced http://infolab.stanford.edu/~backrub/google.html
     *
     * @param searchQuery
     * @return
     */
    public List<SearchResult> getWebPages(SearchQuery searchQuery){
        SearchResultRelevanceCalc relevanceCalc = new SearchResultRelevanceCalc
                    (pageRankerManager.current());


        List<KeywordMatch> matches = getMatchesAllTerms(searchQuery);

        if (matches.isEmpty()){
            matches = getMatchesAnyTerms(searchQuery);
        }

        List<SearchResult> results = new ArrayList<>();

        for (KeywordMatch match : matches){
            URI url = uriMap.getUri(match.getUrlId());
            if (null == url){
                throw new NullPointerException();
            }

            SearchResult searchResult = new SearchResult(url, searchQuery);
            searchResult.addKeyword(match);
            results.add(searchResult);
        }

        for (SearchResult searchResult : results){
            Double score = relevanceCalc.getRelevanceScore(searchResult);
            searchResult.setRelevanceScore(score);
        }

        LOGGER.info("sorting retrieved results");

        Collections.sort(results, Collections.reverseOrder());

        LOGGER.info("done sorting retrieved results");

        if (results.size() >= 25){
            results = results.subList(0, 25);
        }

        insertPageTitles(results);

        return results;
    }

    private void insertPageTitles(List<SearchResult> results) {
        WebPageStore webPageStore = new WebPageStore();

        for (SearchResult searchResult: results){
            URI pageUrl = searchResult.getUrl();
            WebPage webPage = webPageStore.get(pageUrl, startUrl);

            Extractor extractor = Extractor.fromBody(webPage.getBody(), startUrl);

            searchResult.setTitle(extractor.title());
        }
    }
}
