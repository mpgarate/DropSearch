package edu.nyu.mpgarate.dropsearch.document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike on 4/27/15.
 */
public class SearchQuery {
    private String inputString;

    private SearchQuery(String inputString){
        this.inputString = inputString;
    }

    public static SearchQuery parse(String inputString){
        // do stuff
        return new SearchQuery(inputString);
    }

    public List<String> keywords(){
        List<String> keywords = new ArrayList<String>();

        keywords.add(inputString);

        return keywords;
    }

}
