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
    private Object lock = new Object();

    private ConcurrentHashMap<String, Set<UrlNode>> map = new
            ConcurrentHashMap<>();

    public SynchronizedKeywordIndex(){

    }

    public void addAll(List<Keyword> keywords, WebPage webPage){
        for(Keyword keyword : keywords){
            String term = keyword.getTerm();
            URL url = webPage.getUrl();

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

    }

    public List<KeywordMatch> getWebPageUrls(String term){
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
}
