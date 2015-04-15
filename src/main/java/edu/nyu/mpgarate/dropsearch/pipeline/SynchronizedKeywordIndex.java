package edu.nyu.mpgarate.dropsearch.pipeline;

import edu.nyu.mpgarate.dropsearch.document.WebPage;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mike on 4/14/15.
 */
public class SynchronizedKeywordIndex {

    private Object lock = new Object();

    private ConcurrentHashMap<String, List<WebPage>> map = new
            ConcurrentHashMap<String,
            List<WebPage>>();

    public void add(String term, WebPage webPage) {
        synchronized (lock) {
            List<WebPage> pagesList = map.get(term);

            if (null == pagesList) {
                pagesList = new LinkedList<WebPage>();
                pagesList.add(webPage);
                map.put(term, pagesList);
            } else {
                map.put(term, pagesList);
            }
        }
    }

    public List<WebPage> getWebPages(String term){
        return Collections.unmodifiableList(map.get(term));
    }
}
