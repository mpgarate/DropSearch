package edu.nyu.mpgarate.dropsearch.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * Created by mike on 4/14/15.
 */
public class IOUtil {
    /**
     * http://stackoverflow.com/questions/4328711/read-url-to-string-in-few-lines-of-java-code
     * @param url
     */
    public static String getURLAsString(URL url) throws IOException {
        URLConnection connection = url.openConnection();

        Map<String, List<String>> header = connection.getHeaderFields();
        if (!header.get("Content-Type").get(0).contains("text/html")){
            return null;
        }

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream())
        );

        StringBuilder sb = new StringBuilder();

        String line;

        while ((line = reader.readLine()) != null){
            sb.append(line);
        }

        reader.close();

        return sb.toString();
    }
}
