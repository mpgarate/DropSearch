package edu.nyu.mpgarate.dropsearch;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by mike on 5/4/15.
 */
public class ConfigurationTest {

    @Test
    public void configurationLoadsMaxCrawlPages(){
        Configuration config = Configuration.getInstance();

        assertTrue(config.getMaxCrawlPages().equals(50));
    }

}
