package edu.nyu.mpgarate.dropsearch.storage;

import com.mongodb.MongoClient;
import edu.nyu.mpgarate.dropsearch.document.WebPage;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/**
 * Created by mike on 5/1/15.
 */
public class MorphiaSingleton {
    private static final Datastore INSTANCE = initMorphiaInstance();

    private static Datastore initMorphiaInstance(){
        MongoClient mongoClient = new MongoClient();
        Morphia morphia = new Morphia();
        Datastore ds = morphia.createDatastore(mongoClient,
                "drop_search");

        morphia.map(WebPage.class);
        ds.ensureIndexes();

        return ds;
    }

    public static Datastore getInstance(){
        return MorphiaSingleton.INSTANCE;
    }
}
