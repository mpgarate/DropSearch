package edu.nyu.mpgarate.dropsearch.pipeline;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.text.BreakIterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mike on 4/15/15.
 */
public class KeywordExtractor {
    private String body;

    private KeywordExtractor(String body){
        this.body = body;
    }

    public static KeywordExtractor fromBody(String body){
        return new KeywordExtractor(body);
    }

    public List<String> extract(){
        LinkedList<String> keyWords = new LinkedList<String>();

        Document jsoupDoc = Jsoup.parse(body);

        String bodyText = jsoupDoc.body().text();

        BreakIterator boundary = BreakIterator.getWordInstance();
        boundary.setText(bodyText);

        int start = boundary.first();
        for (int end = boundary.next();
             end != BreakIterator.DONE;
             start = end, end = boundary.next()) {

            String keyWord = bodyText.substring(start, end);
            if (keyWord.length() > 2) {
                keyWords.add(keyWord.toLowerCase());
            }
        }

        return keyWords;
    }
}
