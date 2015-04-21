package edu.nyu.mpgarate.dropsearch.crawl;

import org.bson.types.ObjectId;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mike on 4/14/15.
 */
public class SynchronizedKeywordIndex {
    private final static SynchronizedKeywordIndex instance = new
    SynchronizedKeywordIndex();

    public static SynchronizedKeywordIndex getInstance(){
        return instance;
    }

    private Object lock = new Object();

    private ConcurrentHashMap<String, List<ObjectId>> map = new
            ConcurrentHashMap<String,
            List<ObjectId>>();

    public void add(String term, ObjectId pageId) {
        synchronized (lock) {
            List<ObjectId> pagesList = map.get(term);

            if (null == pagesList) {
                pagesList = new LinkedList<ObjectId>();
                pagesList.add(pageId);
                map.put(term, pagesList);
            } else {
                map.put(term, pagesList);
            }
        }
    }

    public void addAll(List<String> terms, ObjectId pageId){
        for(String term : terms){
            add(term, pageId);
        }
    }

    public List<ObjectId> getObjectIds(String term){
        List<ObjectId> objectIds = map.get(term);

        if (null == objectIds){
            return new LinkedList<ObjectId>();
        }

        return Collections.unmodifiableList(objectIds);
    }
}
