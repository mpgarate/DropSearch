package edu.nyu.mpgarate.dropsearch.storage;

import edu.nyu.mpgarate.dropsearch.document.WebPage;
import org.mongodb.morphia.Datastore;

import java.net.URI;
import java.util.logging.Logger;

/**
 * Created by mike on 4/27/15.
 */
public class WebPageStore {
    private final Logger LOGGER = Logger.getLogger(WebPageStore.class.getName
            ());
    private final static Object LOCK = new Object();

    private final Datastore ds = MorphiaSingleton.getInstance();


    public WebPageStore() {

    }

    public void save(WebPage webPage){
        if (null == webPage){
            throw new NullPointerException();
        }

        String url = webPage.getUrl().toString();

        synchronized (LOCK) {
            WebPage foundPage = ds.find(WebPage.class, "url", url).get();

            if (null != foundPage) {
                ds.delete(WebPage.class, foundPage.getId());
            }

            ds.save(webPage);
        }
    }

    public WebPage get(URI url, URI startUrl){
        if (null == url || startUrl == null){
            throw new NullPointerException();
        }

        return ds.find(WebPage.class, "url",
                url.toString()).filter("startUrl", startUrl.toString()).get();
    }

    public void deleteAllEngineUrls(URI startUrl){
        if (null == startUrl){
            throw new NullPointerException();
        }

        ds.delete(ds.createQuery(WebPage.class).filter("startUrl", startUrl
                .toString()));
    }
}
