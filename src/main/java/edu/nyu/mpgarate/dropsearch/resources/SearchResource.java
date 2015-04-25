package edu.nyu.mpgarate.dropsearch.resources;

import com.codahale.metrics.annotation.Timed;
import edu.nyu.mpgarate.dropsearch.SearchEngine;
import edu.nyu.mpgarate.dropsearch.SearchEngineFactory;
import edu.nyu.mpgarate.dropsearch.document.WebPage;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.net.URL;

/**
 * Created by mike on 4/21/15.
 */

@Path("/search")
@Produces(MediaType.APPLICATION_JSON)
public class SearchResource {
    @GET
    @Timed
    public String startCrawl(@QueryParam("url")
                             URL url, @QueryParam("q") String query){

        SearchEngine searchEngine = SearchEngineFactory.getSearchEngine(url);

        StringBuilder sb = new StringBuilder();

        for (WebPage result: searchEngine.search(query)) {
            sb.append(result.getUrl().toString());
        }

        return sb.toString();

    }
}
