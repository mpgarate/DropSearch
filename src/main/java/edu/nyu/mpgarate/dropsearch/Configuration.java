package edu.nyu.mpgarate.dropsearch;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by mike on 5/4/15.
 */
public class Configuration {
    private static Properties properties = new Properties();
    private static Configuration instance = null;

    private Configuration(){
        InputStream inputStream = null;

        try {
            inputStream = Configuration.class.getClassLoader()
                    .getResourceAsStream("dropsearch.properties");

            if (null == inputStream){
                throw new RuntimeException("Could not load properties");
            } else {
                properties.load(inputStream);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static synchronized Configuration getInstance(){
        if (null == instance){
            instance = new Configuration();
        }

        return instance;
    }

    public Integer getMaxCrawlPages(){
        return Integer.parseInt(properties.getProperty("maxCrawlPages"));
    }
}
