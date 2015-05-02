package edu.nyu.mpgarate.dropsearch;

import java.net.URL;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by mike on 4/20/15.
 */
public class SearchEngineFactory {
    private final static int MAX_ACTIVE_SEARCH_ENGINES = 3;

    private final static Map<URL, SearchEngine> searchEngines =
            new ConcurrentHashMap<>();

    private final static Queue<URL> searchEngineQueue = new
            ConcurrentLinkedDeque<>();

    public static SearchEngine getSearchEngine(URL url){
        if (null == url){
            throw new IllegalArgumentException("url must not be empty.");
        }

        SearchEngine searchEngine;

        synchronized (searchEngines){
            if (searchEngines.size() >= MAX_ACTIVE_SEARCH_ENGINES){
                removeOldestSearchEngine();
            }

            searchEngine = searchEngines.get(url);

            if (null == searchEngine){
                searchEngine = new SearchEngine(url);
            }

            searchEngines.put(url, searchEngine);
            searchEngineQueue.add(url);
        }

        return searchEngine;
    }

    private static void removeOldestSearchEngine(){
        SearchEngine se = searchEngines.get(searchEngineQueue.peek());

        se.terminate();

        searchEngines.remove(searchEngineQueue.remove());
    }
}
