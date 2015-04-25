package edu.nyu.mpgarate.dropsearch;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mike on 4/20/15.
 */
public class SearchEngineFactory {
    private final static Map<URL, SearchEngine> searchEngines =
            new ConcurrentHashMap<URL, SearchEngine>();

    public static SearchEngine getSearchEngine(URL url){
        if (null == url){
            throw new IllegalArgumentException("url must not be empty.");
        }

        SearchEngine searchEngine;

        synchronized (searchEngines){
            searchEngine = searchEngines.get(url);
            if (null == searchEngine){
                searchEngine = new SearchEngine(url);
            }
            searchEngines.put(url, searchEngine);
        }

        return searchEngine;
    }
}
