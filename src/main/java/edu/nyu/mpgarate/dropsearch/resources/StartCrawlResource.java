package edu.nyu.mpgarate.dropsearch.resources;

import com.codahale.metrics.annotation.Timed;
import edu.nyu.mpgarate.dropsearch.SearchEngine;
import edu.nyu.mpgarate.dropsearch.SearchEngineFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.net.URL;

/**
 * Created by mike on 4/20/15.
 */

@Path("/start_crawl")
@Produces(MediaType.APPLICATION_JSON)
public class StartCrawlResource {
    public StartCrawlResource(){

    }

    @GET
    @Timed
    public String startCrawl(@QueryParam("url")
            URL url){

        SearchEngine searchEngine = SearchEngineFactory.getSearchEngine(url);
        searchEngine.startAsynchronousCrawl();

        return "started crawl.";
    }
}
