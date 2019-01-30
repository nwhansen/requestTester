/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hourai.requesttester.commandline;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author nhansen
 */
public class MIMEType {
    public static Map<String,String> mimeHash;
    static {
        HashMap<String,String> mimeTypes = new HashMap<String,String>();
        mimeTypes.put("soap", "application/soap+xml");
        mimeTypes.put("text", "text/plain");
        mimeTypes.put("html", "text/html");
        mimeTypes.put("json", "application/json");
	mimeTypes.put("xml", "text/xml");
        mimeHash = Collections.unmodifiableMap(mimeTypes);
    }
    
    public static void printAll() {
        System.out.println("<mime-type> -> <content-type>");
        for(String key: mimeHash.keySet()) {
            System.out.println(String.format("%s -> %s", key, mimeHash.get(key)));
        }
    }
}
