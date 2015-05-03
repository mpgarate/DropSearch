package edu.nyu.mpgarate.dropsearch.nlp;

import rita.RiWordNet;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by mike on 5/3/15.
 */
public class WordNet {
    private final static RiWordNet riWordNet = new RiWordNet
            ("/usr/local/Cellar/wordnet/3.1/");

    private final static Logger LOGGER = Logger.getLogger(WordNet.class.getName
            ());

    /**
     * From a single word, produce a list of any stems found that are not the
     * same as the input term.
     *
     * @param term
     * @return any unique stems
     */
    public static List<String> getAnyStems(String term){
        Set<String> stems = new HashSet<>();
        stems.add(term);

        for (String pos : new String[] {"n", "v", "r", "a"}){
            stems.addAll(Arrays.asList(riWordNet.getStems(term, pos)));

            for (String stem : stems){
                LOGGER.info(stem);
            }
        }

        stems.remove(term);

        return new ArrayList<String>(stems);
    }
}
