package edu.nyu.mpgarate.dropsearch.algorithm;

import edu.nyu.mpgarate.dropsearch.document.WebPage;

import java.util.logging.Logger;

/**
 * Created by mike on 4/28/15.
 */
public class VectorSpaceImportance {
    private final static Logger LOGGER = Logger.getLogger
            (VectorSpaceImportance.class.getName());

    private final static Double MAX_OCCURRENCE_RATE = 10.012;

    public static Double of(Integer occCount, Integer docSize){
        Double quotient = (double)occCount / docSize;

        if (quotient < MAX_OCCURRENCE_RATE){
            return quotient;
        } else {
            return 0.0;
        }
    }
}
