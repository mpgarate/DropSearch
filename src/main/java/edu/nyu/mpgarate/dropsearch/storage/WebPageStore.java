package edu.nyu.mpgarate.dropsearch.storage;

import com.mongodb.MongoClient;
import edu.nyu.mpgarate.dropsearch.document.WebPage;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.net.URL;
import java.util.logging.Logger;

/**
 * Created by mike on 4/27/15.
 */
public class WebPageStore {
    private final Logger LOGGER = Logger.getLogger(WebPageStore.class.getName
            ());
    private final static Object LOCK = new Object();

    private Datastore ds = MorphiaSingleton.getInstance();


    public WebPageStore(){

    };

    public void save(WebPage webPage){
        if (null == webPage){
            throw new NullPointerException();
        }

        String url = webPage.getUrl().toString();

        synchronized (LOCK) {
            WebPage foundPage = ds.find(WebPage.class, "url", url).get();

            if (null == foundPage) {
                LOGGER.info("not a duplicate");
            } else {
                LOGGER.info("we found a duplicate");
                ds.delete(WebPage.class, foundPage.getId());
            }

            ds.save(webPage);
        }
    }

    public WebPage get(URL url){
        if (null == url){
            throw new NullPointerException();
        }

        WebPage webPage = ds.find(WebPage.class, "url", url.toString()).get();

        return webPage;
    }
}
