/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hourai.requesttester.commandline;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nhansen
 */
public class WebRetriever {
    private static final byte[] newLineBytes = getBytes("\r\n");
    private static final byte[] requestHeader = getBytes("HTTP/1.1 ");
    private static final byte[] contentTypeHeader = getBytes("Content-Type: ");
    private static final byte[] contentLengthHeader = getBytes("Content-Length: ");
    private String responseCode = "200";
    private String responseType = "text/html";
    public WebRetriever() {
        
    }
    
    public void setResponseCode(String responseCode) { 
        try {
            int code = Integer.parseInt(responseCode);
        } catch(NumberFormatException e) {
            System.out.printf("Response code parameter is not a number %s", responseCode);
            System.exit(1);
        }
        this.responseCode = responseCode;
    }
    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }
    public void download(String filename, String url) {
        try {
            //Get the content
            final byte[] websiteText = getBytes(getText(url));
            FileOutputStream outputFile = null;
            try {
                outputFile = new FileOutputStream(filename); 
                writeContent(outputFile, requestHeader, getBytes(responseCode), newLineBytes);
                writeContent(outputFile, contentTypeHeader, getBytes(responseType), newLineBytes);
                //Get the bytes to write
                writeContent(outputFile, contentLengthHeader, getBytes(Integer.toString(websiteText.length)), newLineBytes);
                //The new line between content and our headers
                outputFile.write(newLineBytes);
                outputFile.write(websiteText);
            } finally {
                if (outputFile != null) {
                    outputFile.close();
                }
            }
        } catch(Exception e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
            //Exit with a failed code
            System.exit(1);
        }
    }
    private static byte[] getBytes(String string) {
        return string.getBytes(StandardCharsets.UTF_8);
    }
    private void writeContent(FileOutputStream output, byte[]...parts) throws IOException {
        for(byte[] part: parts) {
            output.write(part);
        }
    }
        
    public static String getText(String url) throws Exception {
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
