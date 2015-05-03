package edu.nyu.mpgarate.dropsearch.crawl;

import edu.nyu.mpgarate.dropsearch.nlp.WordNet;
import org.junit.Test;
import rita.RiString;
import rita.RiTa;
import rita.RiWordNet;
import rita.wordnet.jwnl.wndata.Word;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by mike on 5/2/15.
 */
public class SynonymRetrievalTest {
    @Test
    public void retrieveSynonymTest() {
        String term = "running";

        List<String> stems = WordNet.getAnyStems(term);

        System.out.println(stems);
        assertTrue(stems.contains("run"));
        assertFalse(stems.contains("running"));
//
//        RiString rs = new RiString(term);
//        System.out.println(rs.analyze());
//        System.out.println(rs.features());
//
//        RiTa riTa = new RiTa();
//        RiWordNet wn = WordNet.get
//
//        String[] syns = wn.getAllSynonyms(term, "n", 10);
//        String[] stems = wn.getStems(term, "v");
//
//        for (String pos : wn.getPos(term)){
//            System.out.println(pos);
//        }
//
//        System.out.println(wn.getPos(term)[0]);
//
//
//
//        System.out.println("stems:");
//
//        for (String stem : stems){
//            System.out.println(stem);
//        }
//
//        System.out.println("syms:");
//
//        for (String syn : syns){
//            System.out.println(syn);
//        }
    }
}
