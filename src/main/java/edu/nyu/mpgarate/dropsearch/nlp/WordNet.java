package edu.nyu.mpgarate.dropsearch.nlp;

import rita.RiMarkov;
import rita.RiString;
import rita.RiTa;
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

    private final static String[] PARTS_OF_SPEECH = new String[] {"n", "v",
            "r", "a"};

    /**
     * From a single word, produce a list of any stems found that are not the
     * same as the input term.
     *
     * First, an attempt is made using the Pling algorithm
     * https://github.com/dhowe/RiTa/blob/b4c753a32d2db1cb22e927cfb0e0a11882dbfec1/src/rita/support/PlingStemmer.java
     *
     * If this fails, then WordNet is used as a backup.
     *
     * @param term
     * @return any unique stems
     */
    public static List<String> getAnyStems(String term){


        Set<String> stems = new HashSet<>();
        stems.add(term);

        // first try using the PLING algorithm
        stems.addAll(Arrays.asList(RiTa.stem(term, RiTa.PLING)));

        // if none found, use WordNet
        if (1 == stems.size()){
            for (String pos : PARTS_OF_SPEECH) {
                stems.addAll(Arrays.asList(riWordNet.getStems(term, pos)));
            }
        }


        stems.remove(term);

        return new ArrayList<String>(stems);
    }

    public static List<String> getRelatedWords(String term){
        Set<String> relatedWords = new HashSet<>();

        for (String pos : PARTS_OF_SPEECH){
            relatedWords.addAll(Arrays.asList(riWordNet.getAllSynsets(term,
                    pos)));

        }

        if (relatedWords.isEmpty()){
            for (String pos : PARTS_OF_SPEECH) {
                relatedWords.addAll(Arrays.asList(riWordNet.getAllMeronyms(term,
                        pos)));

                }
        }

        List<String> relatedWordsList = new ArrayList<String>();

        for (String w : relatedWords){
            for (String pos : PARTS_OF_SPEECH) {
                Float dist = riWordNet.getDistance(term, w,pos);

                if (dist < 0.3){
                    relatedWordsList.add(w);
                    break;
                }
            }
        }

        return relatedWordsList;
    }
}
