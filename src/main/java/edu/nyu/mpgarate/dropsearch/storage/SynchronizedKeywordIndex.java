package edu.nyu.mpgarate.dropsearch.storage;

import edu.nyu.mpgarate.dropsearch.document.Keyword;
import edu.nyu.mpgarate.dropsearch.document.KeywordMatch;
import edu.nyu.mpgarate.dropsearch.document.WebPage;
import org.bson.types.ObjectId;

import java.net.URI;
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

    private final Set<URI> allUrls = new HashSet<URI>();
    private final SynchronizedUriMap uriMap;

    public SynchronizedKeywordIndex(SynchronizedUriMap uriMap){
        this.uriMap = uriMap;
    }

    public void addAll(List<Keyword> keywords, WebPage webPage){
        URI url = webPage.getUrl();

        if (null == uriMap.getId(url)){
            uriMap.putUri(new ObjectId(), url);
        }

        for(Keyword keyword : keywords){
            String term = keyword.getTerm();

            UrlNode urlNode = new UrlNode(uriMap.getId(url), keyword
                    .getWeight());

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

    public List<UrlNode> getKeywordMatches(String term){
        Set<UrlNode> urlNodes = map.get(term);

        if (null == urlNodes){
            return Collections.emptyList();
        }

        List<UrlNode> urlNodeList = Arrays.asList(urlNodes.toArray(new
                UrlNode[urlNodes.size()]));


        return urlNodeList;


//        Set<KeywordMatch> matches = new HashSet<>();
//
//        for (UrlNode urlNode : urlNodes){
//            KeywordMatch match = new KeywordMatch(term, urlNode.getPageWeight
//                    (), urlNode.getUrlId());
//            matches.add(match);
//        }
//
//        return matches;
    }

    public List<URI> getAllUrls(){
        synchronized (lock) {
            return new ArrayList<URI>(allUrls);
        }
    }
}
