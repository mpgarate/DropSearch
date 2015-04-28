package edu.nyu.mpgarate.dropsearch.algorithm;

import edu.nyu.mpgarate.dropsearch.document.WebPage;

/**
 * Created by mike on 4/28/15.
 */
public class VectorSpaceImportance {
    public static Double of(Integer occCount, Integer docSize){
        return (double)occCount / docSize;
    }
}
