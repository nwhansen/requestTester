/*
 * Copyright of Nathan Hansen 2018, No warrenty implicit or explicit is given. 
 */

package hourai.requesttester.data;

import java.net.Socket;
import hourai.requesttester.RequestReader;
import hourai.requesttester.interfaces.ResponseGenerator;

/**
 * When the request server makes a connection this object is returned
 * @author nhansen
 */
public class RequestServerConnection {
    private final ResponseGenerator responseGenerator; 
    private final RequestReader reader;
    private Socket socket;
    
    /**
     * Creates the request server connection
     * @param responseGenerator The response generator for the request
     * @param reader The reader that operates on the given socket
     * @param socket The underlying socket for reading/writing
     */
    public RequestServerConnection(ResponseGenerator responseGenerator, RequestReader reader, Socket socket) {
        this.responseGenerator = responseGenerator;
        this.reader = reader;
        this.socket = socket;
    }
    
    public ResponseGenerator getResponseGenerator() {
        return responseGenerator;
    }
    
    public RequestReader getReader() {
        return reader;
    }
    
    /**
     * @return The socket that is underlying the reader and response generator
     */
    public Socket getUnderlyingSocket() {
        return socket;
    }
    
}




