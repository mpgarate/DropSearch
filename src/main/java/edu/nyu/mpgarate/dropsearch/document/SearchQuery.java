package edu.nyu.mpgarate.dropsearch.document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mike on 4/27/15.
 */
public class SearchQuery {
    private String inputString;
    private List<String> keywords;

    private SearchQuery(String inputString){
        this.inputString = inputString;
        this.keywords = parseKeywords(inputString);
    }

    public static SearchQuery parse(String inputString){
        // do stuff
        return new SearchQuery(inputString);
    }

    public List<String> getKeywords(){
        return Collections.unmodifiableList(keywords);
    }

    private List<String> parseKeywords(String inputString){
        List<String> keywords = new ArrayList<String>(Arrays.asList(inputString
                .split("\\s+")));

        keywords = keywords.stream().map(kw -> kw.toLowerCase()).collect
                (Collectors.toList());

        return keywords;
    }

}
