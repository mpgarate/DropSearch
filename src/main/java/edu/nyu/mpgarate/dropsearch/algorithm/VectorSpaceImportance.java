package edu.nyu.mpgarate.dropsearch.algorithm;

/**
 * Created by mike on 4/28/15.
 */
public class VectorSpaceImportance {
    public static Double of(Integer occCount, Integer docSize, Integer
            distanceFromDocumentStart){
        return (double)occCount / (docSize + Math.pow
                (distanceFromDocumentStart, 2));
    }
}
