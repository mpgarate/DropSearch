package edu.nyu.mpgarate.dropsearch.storage;

import org.bson.types.ObjectId;

import javax.validation.constraints.Null;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mike on 5/4/15.
 */
public class SynchronizedUriMap {
    private Map<ObjectId, URI> uriMap;
    private Map<URI, ObjectId> objectIdMap;
    private Object lock;

    public SynchronizedUriMap(){
        this.uriMap = new HashMap<>();
        this.objectIdMap = new HashMap<>();
        this.lock = new Object();
    }

    public URI getUri(ObjectId objId){
        synchronized (lock){
            return uriMap.get(objId);
        }
    }

    public void putUri(ObjectId objectId, URI uri){
        if (null == objectId || null == uri){
            throw new IllegalArgumentException("params must not be null");
        }
        synchronized (lock){
            objectIdMap.put(uri, objectId);
            uriMap.put(objectId, uri);
        }
    }

    public ObjectId getId(URI uri){
        synchronized (lock) {
            return objectIdMap.get(uri);
        }
    }
}
