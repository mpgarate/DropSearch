package edu.nyu.mpgarate.dropsearch.storage;

import edu.nyu.mpgarate.dropsearch.algorithm.VectorSpaceImportance;
import edu.nyu.mpgarate.dropsearch.document.WebPage;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by mike on 4/14/15.
 */
public class SynchronizedKeywordIndex {
    private Object lock = new Object();

    private ConcurrentHashMap<String, Set<UrlNode>> map = new
            ConcurrentHashMap<>();

    public SynchronizedKeywordIndex(){

    }

    public void add(String term, WebPage webPage) {
        URL url = webPage.getUrl();

        UrlNode urlNode = new UrlNode(url, VectorSpaceImportance.of(webPage,
                term));

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

    public void addAll(List<String> terms, WebPage webPage){
        for(String term : terms){
            add(term, webPage);
        }

    }

    public List<URL> getWebPageUrls(String term){
        Set<UrlNode> urlNodes = map.get(term);

        if (null == urlNodes){
            return new LinkedList<>();
        }

        ArrayList<URL> urls = new ArrayList<>();
        for (UrlNode urlNode : urlNodes){
            urls.add(urlNode.getUrl());
        }

        return urls;
    }
}
