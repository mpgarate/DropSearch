package edu.nyu.mpgarate.dropsearch.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.nyu.mpgarate.dropsearch.document.WebPage;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by mike on 4/27/15.
 */
public class WebPageStore {
    private final static JedisPool pool = new JedisPool(
            new JedisPoolConfig(), "localhost");

    private final static ObjectMapper mapper = new ObjectMapper();

    public WebPageStore(){};

    public void set(URL url, WebPage webPage){
        if (null == url || null == webPage){
            throw new NullPointerException();
        }

        try {
            String webPageStr = mapper.writeValueAsString(webPage);
            try (Jedis jedis = pool.getResource()){
                // TODO: don't fail silently
                jedis.set("" + url.hashCode(), webPageStr);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public WebPage get(URL url){
        if (null == url){
            throw new NullPointerException();
        }

        WebPage webPage = null;

        try (Jedis jedis = pool.getResource()){
            String webPageStr = jedis.get("" + url.hashCode());

            if (null == webPageStr){
                return null;
            }

            webPage = mapper.readValue(webPageStr, WebPage.class);
        } catch (IOException e) {
            // TODO: log this error and continue execution
            e.printStackTrace();
            return null;
        }

        return webPage;
    }
}
