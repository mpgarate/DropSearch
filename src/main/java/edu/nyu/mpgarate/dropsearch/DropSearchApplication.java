package edu.nyu.mpgarate.dropsearch;

import edu.nyu.mpgarate.dropsearch.resources.SearchResource;
import edu.nyu.mpgarate.dropsearch.resources.StartCrawlResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

/**
 * Created by mike on 4/20/15.
 */
public class DropSearchApplication extends Application<DropSearchConfiguration> {
    public static void main(String[] args) throws Exception {
        new DropSearchApplication().run(args);
    }

    @Override
    public String getName() {
        return "drop-search";
    }

    @Override
    public void run(DropSearchConfiguration configuration,
                    Environment environment) {

        final StartCrawlResource startCrawlResource = new StartCrawlResource();
        environment.jersey().register(startCrawlResource);

        final SearchResource searchResource = new SearchResource();
        environment.jersey().register(searchResource);

   }
}
