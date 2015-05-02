package edu.nyu.mpgarate.dropsearch.storage;

import edu.nyu.mpgarate.dropsearch.document.Keyword;
import edu.nyu.mpgarate.dropsearch.document.KeywordMatch;
import edu.nyu.mpgarate.dropsearch.document.SearchResult;
import edu.nyu.mpgarate.dropsearch.document.WebPage;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by mike on 4/14/15.
 */
public class SynchronizedKeywordIndex {
    private final Object lock = new Object();

    private final ConcurrentHashMap<String, Set<UrlNode>> map = new
            ConcurrentHashMap<>();

    private final Set<URL> allUrls = new HashSet<URL>();

    public SynchronizedKeywordIndex(){

    }

    public void addAll(List<Keyword> keywords, WebPage webPage){
        URL url = webPage.getUrl();

        for(Keyword keyword : keywords){
            String term = keyword.getTerm();

            UrlNode urlNode = new UrlNode(url, keyword.getWeight());

            synchronized (lock) {
                Set<UrlNode> urlNodes = map.get(term);

                if (null == urlNodes) {
                    urlNodes = new ConcurrentSkipListSet<UrlNode>();
                }

                if (!urlNodes.contains(urlNode)){
                    urlNodes.add(urlNode);
                    map.put(term, urlNodes);
                }
            }
        }

        allUrls.add(url);
    }

    public List<KeywordMatch> getKeywordMatches(String term){
        Set<UrlNode> urlNodes = map.get(term);

        if (null == urlNodes){
            return new LinkedList<>();
        }

        ArrayList<KeywordMatch> matches = new ArrayList<>();

        for (UrlNode urlNode : urlNodes){
            KeywordMatch match = new KeywordMatch(term, urlNode.getPageWeight
                    (), urlNode.getUrl());
            matches.add(match);
        }

        return matches;
    }

    public List<URL> getAllUrls(){
        return new ArrayList<URL>(allUrls);
    }
}
