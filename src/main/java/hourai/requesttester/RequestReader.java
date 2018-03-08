/*
 * Copyright of Nathan Hansen 2018, No warrenty implicit or explicit is given. 
 */
package hourai.requesttester;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import hourai.requesttester.interfaces.RequestWriteCallback;

/**
 * A class that will write the request to a file as well as duplex the data to callback objects
 * @author nhansen
 */
public class RequestReader {
    
    private final LinkedList<RequestWriteCallback> listeners = new LinkedList<RequestWriteCallback>();
    BufferedReader reader;

    public RequestReader(BufferedReader reader) {
        this.reader = reader;
    }
       
    
    public void addCallback(RequestWriteCallback writer) {
        listeners.addLast(writer);
    }
    
    public void consumeBuffer() throws IOException {
        final String contentHeader = "content-length: ";
        final String transferEncoding = "transfer-encoding: chunked";
        
        long contentLength = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            // If we only had java streams
            notifyLine(line);
            // Read the headers until we run out of headers
            if (line.regionMatches(true, 0, contentHeader, 0, contentHeader.length())) {
                contentLength = Math.max(0, Integer.parseInt(line.substring(contentHeader.length())));
            } else if(line.regionMatches(true, 0, transferEncoding, 0, transferEncoding.length())) {
                contentLength = -1;
            }
            // After headers we have an empty line
            if (line.isEmpty()) {
                break;
            }
        }
        // Read the content as characters
        if (contentLength > 0) {
            readCharacters(contentLength);
            notifyLine("");
        } else if (contentLength == -1) {
            //CHUNKED BE DAMNED
            do {
                line = reader.readLine();
                notifyLine(line);
                if("0".equals(line)) {
                    notifyLine("");
                    break;
                }
                try {
                    contentLength = Long.parseLong(line, 16);
                    readCharacters(contentLength);
                } catch(NumberFormatException e) {
                    //Try reading in hex
                } 
                //There should be an empty newline after the number of characters they gave us
                reader.readLine();
                notifyLine("");
            } while(true);
        }
    }

    private void readCharacters(long contentLength) throws IOException {
        //We need to read until the characters are completed
        for (long i = 0; i < contentLength; i++) {
            int c = reader.read();
            notifyChar((char)c);
        }
    }
    
    public void close() {
        if(reader != null){
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(RequestReader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        reader = null;
    }
    
    private void notifyLine(String line) {
        for(RequestWriteCallback listener: listeners) {
            listener.recievedLine(line);
        }
    }
    private void notifyChar(char c) {
        for(RequestWriteCallback listener: listeners) {
            listener.recievedChar(c);
        }
    }
}




