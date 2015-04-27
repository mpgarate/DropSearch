package edu.nyu.mpgarate.dropsearch.storage;

import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mike on 4/14/15.
 */
public class SynchronizedKeywordIndex {
    private Object lock = new Object();

    private ConcurrentHashMap<String, List<URL>> map = new
            ConcurrentHashMap<String,
            List<URL>>();

    public SynchronizedKeywordIndex(){

    }

    public void add(String term, URL url) {
        synchronized (lock) {
            List<URL> webPageUrls = map.get(term);

            if (null == webPageUrls) {
                webPageUrls = new LinkedList<URL>();
                webPageUrls.add(url);
                map.put(term, webPageUrls);
            } else {
                map.put(term, webPageUrls);
            }
        }
    }

    public void addAll(List<String> terms, URL url){
        // make sure pageId is unique, maybe use a set?
        for(String term : terms){
            add(term, url);
        }
    }

    public List<URL> getWebPageUrls(String term){
        List<URL> webPageUrls = map.get(term);

        if (null == webPageUrls){
            return new LinkedList<URL>();
        }

        return Collections.unmodifiableList(webPageUrls);
    }
}
