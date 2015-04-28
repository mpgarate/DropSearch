package edu.nyu.mpgarate.dropsearch.resources;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.nyu.mpgarate.dropsearch.SearchEngine;
import edu.nyu.mpgarate.dropsearch.SearchEngineFactory;
import edu.nyu.mpgarate.dropsearch.document.SearchQuery;
import edu.nyu.mpgarate.dropsearch.document.SearchResult;
import edu.nyu.mpgarate.dropsearch.document.WebPage;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mike on 4/21/15.
 */

@Path("/search")
@Produces(MediaType.APPLICATION_JSON)
public class SearchResource {
    @GET
    @Timed
    public Response search(@QueryParam("url")
                                   URL url, @QueryParam("q") String query){

        SearchEngine searchEngine = SearchEngineFactory.getSearchEngine(url);

        List<SearchResult> results = searchEngine.search(SearchQuery.parse
                (query));

        System.out.println(results);

        List resultUrls = results.stream().map(sr -> new Object[] {sr
                .getWebPage().getUrl(), sr.getRelevanceScore()}).collect
                (Collectors.toList());


        try {
            String resp = new ObjectMapper().writeValueAsString(resultUrls);

            return Response.ok()
                    .entity(resp)
                    .header("Access-Control-Allow-Origin", "*")
                    .build();

        } catch (JsonProcessingException e) {
            return Response.ok().entity("[ ___ ]").build();
        }
    }
}
