package edu.nyu.mpgarate.dropsearch.resources;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.nyu.mpgarate.dropsearch.SearchEngine;
import edu.nyu.mpgarate.dropsearch.SearchEngineFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.util.logging.Logger;

/**
 * Created by mike on 4/20/15.
 */

@Path("/start_crawl")
@Produces(MediaType.APPLICATION_JSON)
public class StartCrawlResource {
    private final static Logger LOGGER = Logger.getLogger(StartCrawlResource
            .class.getName());
    @GET
    @Timed
    public String startCrawl(@QueryParam("url")
                                 URI url){

        ObjectMapper mapper = new ObjectMapper();

        SearchEngine searchEngine = SearchEngineFactory.getSearchEngine(url);

        try {
            if (searchEngine.isStarted()) {
                return mapper.writeValueAsString(searchEngine.getPagesCrawled());
            } else {
                searchEngine.startAsynchronousCrawl();
                return mapper.writeValueAsString(true);
            }
        } catch (JsonProcessingException e){
            LOGGER.warning("could not create json");
            e.printStackTrace();
        }

        return "internal error";
    }
}
