/*
 * Copyright of Nathan Hansen 2018, No warrenty implicit or explicit is given. 
 */
package hourai.requesttester;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import hourai.requesttester.data.RequestServerConnection;
import hourai.requesttester.implementation.RequestFileWriter;
import hourai.requesttester.interfaces.RequestFileWriterFactory;
import hourai.requesttester.interfaces.ResponseGenerator;

/**
 * A class that encapsulates the logic required to process a request. Coordinates the logic for 
 * @author nhansen
 */
public class RequestProcessor {
    
    private final RequestFileWriterFactory fileWriterFactory;
    
    /**
     * Creates the request processor to a message logic
     * @param fileWriterFactory The fileWriter factory to be used to dump requests
     */
    public RequestProcessor(RequestFileWriterFactory fileWriterFactory) {
        this.fileWriterFactory = fileWriterFactory;
    }
    
    /**
     * Processes the request calling the callback. The connection is no longer valid past this request 
     * @param connection The connection information
     */
    public void process(RequestServerConnection connection) {
        // Writing the file 
        Socket socket = connection.getUnderlyingSocket();
        RequestReader input = connection.getReader();
        ResponseGenerator output = connection.getResponseGenerator();
        RequestFileWriter fileWriter = null;
        try {
            fileWriter = fileWriterFactory.create();
            //Add the handler for writing the request file
            input.addCallback(fileWriter);
            //Process the input
            input.consumeBuffer();
            //Generate the response
            output.sendResponse();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RequestProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RequestProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(fileWriter != null) {
                fileWriter.close();
            }
        }
        //Close the streams
        close(socket, input, output);
    }
    
    
    private void close(Socket socket, RequestReader input, ResponseGenerator output) {
        //Close the sockets properly
        output.close();
        input.close();
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(RequestProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}




