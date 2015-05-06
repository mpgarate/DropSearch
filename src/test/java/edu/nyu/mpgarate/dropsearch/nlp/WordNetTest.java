package edu.nyu.mpgarate.dropsearch.nlp;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by mike on 5/2/15.
 */
public class WordNetTest {

    private boolean stemsContains(String term, String expectedStem){
        return WordNet.getAnyStems(term).contains(expectedStem);
    }
    @Test
    public void getAnyStemsTest() {
        assertFalse(stemsContains("running", "running"));

        assertTrue(stemsContains("running", "run"));
        assertTrue(stemsContains("slapped", "slap"));
        assertTrue(stemsContains("argues", "argue"));
        assertTrue(stemsContains("cats", "cat"));
        assertTrue(stemsContains("firemen", "fireman"));
        assertTrue(stemsContains("appendices", "appendix"));
        assertTrue(stemsContains("dries", "dry"));
        assertTrue(stemsContains("physics", "physic"));
        assertTrue(stemsContains("people", "person"));
    }

    private boolean relatedWordsContains(String term, String relatedTerm){
        return WordNet.getRelatedWords(term).contains(relatedTerm);
    }

    @Test
    public void getRelatedWordsTest(){
        assertTrue(relatedWordsContains("computer", "calculator"));
        assertTrue(relatedWordsContains("university", "college"));
        assertTrue(relatedWordsContains("universe", "existence"));
        assertTrue(relatedWordsContains("fish", "angle"));
        assertTrue(relatedWordsContains("text", "schoolbook"));
        assertTrue(relatedWordsContains("steward", "keeper"));
    }

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
