/*
 * Copyright of Nathan Hansen 2018, No warrenty implicit or explicit is given. 
 */
package hourai.requesttester;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import hourai.requesttester.data.RequestServerConnection;
import hourai.requesttester.interfaces.ResponseGeneratorFactory;

/**
 * A class that creates RequestProcessors when a client connects to the server
 *
 * @author nhansen
 */
public class RequestServer {

	private int port;
	private final boolean faultTolerant; //I think
    private ServerSocket server;
    private final ResponseGeneratorFactory responseFactory;

    /**
     * Creates the request server it will attempt to start at the given port, or
     * will pick a random port if passed 0 or fails. Call getPort to figure out
     * which port it actually started on
     *
     * @param startingPort The port you want to start the server on
     * @param responseFactory The factory to create response generators
	 */
	public RequestServer(int startingPort, ResponseGeneratorFactory responseFactory) {
		server = null;
		this.faultTolerant = false;
        port = startingPort;
        this.responseFactory = responseFactory;
        Random rand = new Random();
        do {
            if (startingPort == 0) {
                port = rand.nextInt(8000) + 8000;
                System.out.printf("Attempt to create server at: %d\n", port);
            }
            try {
                server = new ServerSocket(port);
            } catch (IOException e) {
                //If we had a starting port we failed. default to random port
                // selection
                if(startingPort != 0) {
                    port = -1;
                    return;
                }
            }
        } while (server == null);
    }

    /**
     * @return the port we actually listening against
     */
    public int getPort() {
        return port;
    }

    /**
     * Gets a connection from a client and returns the information needed to manage a connection. 
     *    Constructs the RequestServerConnection to return the information 
     * @return The RequestProcessor that can process the requests for the client
     * connection, null if we failed for any reason
     */
    public RequestServerConnection getConnection() {
        try {
            Socket clientSocket = server.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
			RequestReader reader = new RequestReader(in, faultTolerant);
            return new RequestServerConnection(responseFactory.createResponseGenerator(out, reader), reader, clientSocket);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Closes our the server, it is no longer valid past this call, it also
     * terminates any pending getConnections calls
     *
     * @throws IOException It really should but to lazy for try/catch
     */
    public void close() throws IOException {
        //Shuts down the server only really called by gui
        server.close();
    }

}




