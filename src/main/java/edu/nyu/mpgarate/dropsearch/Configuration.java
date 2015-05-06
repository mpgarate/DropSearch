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
        InputStream inputStream;

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

    public Double getTitleWeight(){
        return Double.parseDouble(properties.getProperty("titleWeight"));
    }

    public Integer getPageRankRefreshRate(){
        return Integer.parseInt(properties.getProperty("pageRankRefreshRate"));
    }

    public Double getKeywordsWeight(){
        return Double.parseDouble(properties.getProperty("keywordsRate"));
    }

    public Double getPageRankWeight(){
        return Double.parseDouble(properties.getProperty("pageRankWeight"));
    }

    public Double getPageRankOffset(){
        return Double.parseDouble(properties.getProperty("pageRankOffset"));
    }

    public Integer getMaxActiveSearchEngines(){
        return Integer.parseInt(properties.getProperty
                ("maxActiveSearchEngines"));
    }

    public Long getCrawlPolitenessDelay(){
        return Long.parseLong(properties.getProperty("crawlPolitenessDelay"));
    }

    public Integer getResultCount(){
        return Integer.parseInt(properties.getProperty("resultCount"));
    }
}
