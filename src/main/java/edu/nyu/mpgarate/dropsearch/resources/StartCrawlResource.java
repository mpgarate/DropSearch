package edu.nyu.mpgarate.dropsearch.resources;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.nyu.mpgarate.dropsearch.SearchEngine;
import edu.nyu.mpgarate.dropsearch.SearchEngineFactory;
import edu.nyu.mpgarate.dropsearch.document.CrawlStatus;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    public Response startCrawl(@QueryParam("url")
                                 URI url){

        ObjectMapper mapper = new ObjectMapper();

        SearchEngine searchEngine = SearchEngineFactory.getSearchEngine(url);

        if (!searchEngine.isStarted() && !searchEngine.isDoneCrawling()) {
            searchEngine.startAsynchronousCrawl();
        }

        Response resp;

        try {
            CrawlStatus crawlStatus = new CrawlStatus(searchEngine
                    .getPagesCrawled(), searchEngine.isDoneCrawling());
            String respStr = mapper.writeValueAsString(crawlStatus);

            resp = Response.ok()
                    .entity(respStr)
                    .header("Access-Control-Allow-Origin", "*")
                    .build();
        } catch (JsonProcessingException e){
            LOGGER.warning("could not create json");
            e.printStackTrace();
            resp = Response.serverError().build();
        }


        return resp;
    }
}
