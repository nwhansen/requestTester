/*
 * Copyright of Nathan Hansen 2018, No warrenty implicit or explicit is given.
 */

package hourai.requesttester.implementation.proxy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocketFactory;
import hourai.requesttester.RequestReader;
import hourai.requesttester.implementation.RequestFileWriter;
import hourai.requesttester.implementation.UniqueNameGenerator;
import hourai.requesttester.interfaces.RequestWriteCallback;
import hourai.requesttester.interfaces.ResponseGenerator;

/**
 *
 * @author nhansen
 */
public class ProxyServerResponseGenerator implements ResponseGenerator, RequestWriteCallback {

    private final StringBuffer recievedData = new StringBuffer();
    private final BufferedWriter output;
    private final UniqueNameGenerator nameGenerator;
    private String urlPrefix;
    private String host;
    private int port;
    private boolean encrypted = false;
    boolean parsedRequest = false;
    RequestWriteCallback proxyWriteCallback;
    
    public ProxyServerResponseGenerator(BufferedWriter output, String url, UniqueNameGenerator nameGenerator) {
        this.output = output;
        this.nameGenerator = nameGenerator;
        parseUrl(url);
    } 
    
    public void setProxyWriteCallback(RequestWriteCallback proxyWriteCallback) {
        this.proxyWriteCallback = proxyWriteCallback;
    }
    
    /**
     * Parse the url variable into the appropriate member variables
     */
    private void parseUrl(String url) {
        //Parse the string
        port = 80;
        host = url;
        //HTTPS default port is not 80
        if(url.startsWith("https")) {
            encrypted = true;
            port = 443;
            host = url.substring("https://".length());
        } else if (url.startsWith("http")) {
            host = url.substring("http://".length());
        }
        int urlPathIndex;
        if(( urlPathIndex = host.indexOf("/")) > 0) {
            urlPrefix = host.substring(urlPathIndex);
            if(urlPrefix.endsWith("/")) {
                 urlPrefix = urlPrefix.substring(0, urlPrefix.length() - 1);
            }
            //Null out an empty string
            if("".equals(urlPrefix)) {
                urlPrefix = null;
            }
            //we want to truncate the host
            host = host.substring(0, urlPathIndex);
        }
        if(host.contains(":")) {
            //We have a port
            //Split on that
            String[] parts = host.split(":");
            if(parts.length > 2) {
                //Well this isn't really supported
                throw new IllegalArgumentException("To many colens in url for hostname (we cannot handle more than 1)");
            }
            host = parts[0];
            try {
                port = Integer.parseInt(parts[1]);
            } catch(NumberFormatException e) {
            }
        }  
    }
    
    @Override
    public void sendResponse() {
        Socket remote = null;
        try {
            if(recievedData.length() == 0) {
                throw new Exception("Force fail");
            }
            //We need to connect and generate a response
            remote = createSocket();
            BufferedReader in = new BufferedReader(new InputStreamReader(remote.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(remote.getOutputStream()));
            out.write(recievedData.toString());
            out.flush();
            recievedData.setLength(0);
            //Now we need to basically read the response from the server. 
            RequestReader reader = new RequestReader(in); 
            RequestFileWriter serverResponsewriter = new RequestFileWriter(new PrintWriter(nameGenerator.getLastFileName() + ".response"));
            reader.addCallback(serverResponsewriter);
            reader.addCallback(this);
            if(proxyWriteCallback != null) {
                reader.addCallback(proxyWriteCallback); 
            }
            reader.consumeBuffer();
            output.write(recievedData.toString());
            //We now need the file
            serverResponsewriter.close();
            out.close();
            in.close();
        } catch (Exception ex) {
            if(remote != null) {
                try {
                    remote.close();
                } catch (IOException ex1) {
                    Logger.getLogger(ProxyServerResponseGenerator.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            try {
                if(ex instanceof IOException) {
                    Logger.getLogger(ProxyServerResponseGenerator.class.getName()).log(Level.SEVERE, null, ex);
                }
                //We need to dump the information
                //Send the ah f-it message
                // Start sending our reply, using the HTTP 1.1 protocol
                output.write("HTTP/1.1 200 \r\n"); // Version & status code
                output.write("Content-Type: text/plain\r\n"); // The type of data
                output.write("Connection: close\r\n"); // Will close stream
                output.write("\r\n"); // End of headers
            } catch (IOException ex1) {
                Logger.getLogger(ProxyServerResponseGenerator.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }
    
    @Override
    public void close() {
        try {
            output.close();
        } catch (IOException ex) {
            Logger.getLogger(ProxyServerResponseGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void recievedLine(String line) {
        //We are a bit more fault tollerant than others we should not assume such a kind remote host
        //Parse the host line and fandengle it
        final String hostIdentify = "host:";
        if(!parsedRequest) {
            if(urlPrefix != null && urlPrefix.length() > 0) {
                String[] parts = line.split(" ");
                //merge the prefix with the url
                line = String.format("%s %s%s %s", parts[0], urlPrefix, parts[1], parts[2]);
            }
            parsedRequest = true;
        } else if(line.regionMatches(true, 0, hostIdentify, 0, hostIdentify.length())) {
            line = String.format("Host: %s:%s", host, port);
        } 
        recievedData.append(line).append("\r\n");
    }

    @Override
    public void recievedChar(char c) {
        recievedData.append(c);
    }
    
    private Socket createSocket() throws IOException {  
        if(encrypted) {
            return SSLSocketFactory.getDefault().createSocket(host, port);
        } else {
            return new Socket(host, port);
        }
    }
    
}


