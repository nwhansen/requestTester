/*
 * Copyright of Nathan Hansen 2018, No warrenty implicit or explicit is given. 
 */
package hourai.requesttester.commandline;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import hourai.requesttester.RequestProcessor;
import hourai.requesttester.RequestServer;
import hourai.requesttester.argument.ArgumentListParser;
import hourai.requesttester.argument.Argument;
import hourai.requesttester.gui.ServerGui;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import hourai.requesttester.data.RequestServerConnection;
import hourai.requesttester.implementation.filereader.FileReaderResponseGeneratorFactory;
import hourai.requesttester.implementation.proxy.ProxyServerResponseGeneratorFactory;
import hourai.requesttester.implementation.RequestFileWriterFactoryImpl;
import hourai.requesttester.implementation.UniqueNameGenerator;
import hourai.requesttester.interfaces.RequestWriteCallback;
import hourai.requesttester.interfaces.ResponseGeneratorFactory;

/**
 *
 * @author nhanlineen
 */
public class Main {

    /**
     * The main entrance of the code, parses arguments.
     * @param args the command line argument line
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        int port = 0;
        Argument helpArgument = Arguments.createHelp();
        Argument wsdlArgument = Arguments.createWSDL();
        Argument portArgument = Arguments.createPort();
        Argument tailArgument = Arguments.createTailMode();
        Argument proxyArgument = Arguments.createProxy();
        Argument guiArgument = Arguments.createGui();
        Argument strategyArgument = Arguments.createUrlParameterStrategy();
        List<Argument> arguments = Arrays.asList(helpArgument,wsdlArgument, portArgument, tailArgument, guiArgument, proxyArgument, strategyArgument);
        ArgumentListParser parser = new ArgumentListParser(arguments);
        parser.parseArguments(args);  
        ResponseGeneratorFactory responseGenerator;
        if(helpArgument.getIsPresent()) {
            //Build and print help then return
            System.out.print(parser.writeHelp("A tool that acts as the worlds dumbest web server, it is primarly used to mock out webservices and provides a trivial request/response dumping"));
            return;
        } else if(guiArgument.getIsPresent()) {
            //All else is ilrelevent 
            ServerGui.StartApplication();
            return;
        } 
        else if(portArgument.getIsPresent()) {
            try {
                //Parse the port
                port = Integer.parseInt(portArgument.getValue());
            } catch(NumberFormatException e) {
                System.out.printf("Port parameter is not a number %s", portArgument.getValue());
                System.out.println();
                return;
            }
        } else if(wsdlArgument.getIsPresent()) {
            createWSDL(wsdlArgument.getParameters()[0], wsdlArgument.getParameters()[1]);
            return;
        } else {
            //They didn't give us any arguments?
            // I think we should start the GUI...
            try {
                ServerGui.StartApplication();
            } catch(Exception e) {
                //Should we fail we will start conviently as an application
            }
        }
        RequestWriteCallback callback = null;
        if(tailArgument.getIsPresent()) {
            callback = new TailRequest();
        }
        UniqueNameGenerator nameGenerator = new UniqueNameGenerator();
        if(proxyArgument.getIsPresent()) {
            responseGenerator = new ProxyServerResponseGeneratorFactory(proxyArgument.getValue(), nameGenerator, callback);
        } else {
            responseGenerator = new FileReaderResponseGeneratorFactory();
        }
        
        RequestServer server = new RequestServer(port, responseGenerator);
        System.out.printf("Starting server on port: %d", server.getPort());
        if(portArgument.getIsPresent() && server.getPort() != port) {
            System.out.println("SERVER STARTED ON DIFFERNT PORT THAN REQUESTED");
        }
        System.out.println();
        
        RequestProcessor processor = new RequestProcessor(new RequestFileWriterFactoryImpl(nameGenerator));
        RequestServerConnection connection;
        System.out.println("Awaiting Connection");
        while((connection = server.getConnection()) != null) {
            System.out.println("Recieved Connection");
            if(callback != null) {
                connection.getReader().addCallback(callback);
            }
            processor.process(connection);
            System.out.println("Awaiting Connection");
        }
    }
    private static void createWSDL(String url, String filename) {
        try {
            String wsdlContents = getText(url);
            PrintWriter wsdlFile = null;
            try {
                wsdlFile = new PrintWriter(filename);
                wsdlFile.write("HTTP/1.1 200 \r\n");
                wsdlFile.write("Content-Type: text/xml\r\n"); //Sometimes this has to be application+soap/xml but eh.
                wsdlFile.write("\r\n");
                wsdlFile.write(wsdlContents);
            } finally {
                if (wsdlFile != null) {
                    wsdlFile.close();
                }
            }
        }   catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
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
    // <editor-fold defaultstate="collapsed" desc="Original Code (now replaced but a good reference)">     
    /*private static void OldMethod(String[] args) throws Exception {
        try {
            int port = 0;
            Argument helpArgument = Arguments.createHelp();
            Argument wsdlArgument = Arguments.createWSDL();
            Argument portArgument = Arguments.createPort();
            Argument tailArgument = Arguments.createTailMode();
            List<Argument> arguments = Arrays.asList(helpArgument,wsdlArgument, portArgument, tailArgument);
            ArgumentListParser parser = new ArgumentListParser(arguments);
            parser.parseArguments(args);  
            if(helpArgument.getIsPresent()) {
                //Build and print help then return
                System.out.print(parser.writeHelp("A tool that acts as the worlds dumbest web server, it is primarly used to mock out webservices and provides a trivial request/response dumping"));
                return;
            }
            if(wsdlArgument.getIsPresent()) {
                //Create our wsdl
                createWSDL(wsdlArgument.getParameters()[0], wsdlArgument.getParameters()[1]);
                //we will try and start the server because why not
            }
            if(portArgument.getIsPresent()) {
                try {
                    //Parse the port
                    port = Integer.parseInt(portArgument.getValue());
                } catch(NumberFormatException e) {
                    System.out.printf("Port parameter is not a number %s", portArgument.getValue());
                    System.out.println();
                    return;
                }
            }
            ServerSocket serverSocket = createServerSocket(port);
            if (serverSocket == null) {
                System.out.println("Unable to create server");
                return;
            }
            do {
                System.out.println("Awaiting connection");
                Socket clientSocket = null;
                try {
                    clientSocket = serverSocket.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                    long epoch = System.currentTimeMillis() / 1000;
                    ConsumeAndWrite(epoch, in, out, tailArgument.getIsPresent());
                    System.out.println("connection completed");
                } finally {
                    if (clientSocket != null) {
                        clientSocket.close();
                    }
                }
            } while (true);
        } catch (IOException e) {
            System.out.printf("We crashed %s\n", e.getMessage());
        }
    }
    
   

    private static void ConsumeAndWrite(long epoch, BufferedReader in, BufferedWriter out, boolean standardOut) throws IOException {

        // If we get a Content-Length read a fixed amount
        final String contentHeader = "content-length: ";
        int contentLength = 0;
        String requestedFile = null;
        PrintWriter requestDump = null;
        try {
            requestDump = new PrintWriter(epoch + ".request");
            String line;
            while ((line = in.readLine()) != null) {
                if(standardOut) {
                    System.out.println(line);
                }
                requestDump.println(line);
                //The first line is the POST/GET extra. We don't really care
                // what it is just that we can figure out if we have a file representing our response
                if (requestedFile == null) {
                    String[] parts = line.split(" ");
                    if (parts != null && parts.length > 1) {
                        requestedFile = parts[1].substring(1);
                    }
                }
                if (line.regionMatches(true, 0, contentHeader, 0, contentHeader.length())) {
                    contentLength = Math.max(0, Integer.parseInt(line.substring(contentHeader.length())));
                }
                if (line.isEmpty()) {
                    break;
                }
            }
            if (contentLength > 0) {
                //We need to read until the characters are completed
                for (int i = 0; i < contentLength; i++) {
                    int c = in.read();
                    if(standardOut) {
                        System.out.print(c);
                    }
                    requestDump.write(c);
                }
            }
        } finally {
            if (requestDump != null) {
                requestDump.close();
            }
        }
        //Now we want to check if the file exists.
        if (requestedFile != null && new File(requestedFile).exists()) {
            LinkedList<String> lines = new LinkedList<String>();
            BufferedReader fileReader = new BufferedReader(new FileReader(requestedFile));
            String currentLine;
            while((currentLine = fileReader.readLine()) != null) {
                lines.addLast(currentLine);
            }
            //Just return the file contents it is easier than trying to sort out anything else. It also allows for
            // custom headers... just make sure to have \r\n line endings
            for (String line : lines) {
                try {
                    out.write(line);
                    out.write("\r\n");
                } catch (IOException e) {
                    //It may be a good idea to do this sadly i don't care enough
                }
            }

        } else {
            // Start sending our reply, using the HTTP 1.1 protocol
            out.write("HTTP/1.1 200 \r\n"); // Version & status code
            out.write("Content-Type: text/plain\r\n"); // The type of data
            out.write("Connection: close\r\n"); // Will close stream
            out.write("\r\n"); // End of headers
        }
        out.close();
        in.close();
    }

    private static ServerSocket createServerSocket(int startingPort) {
        ServerSocket server = null;
        int port = startingPort;
        Random rand = new Random();
        do {
            if (startingPort == 0) {
                port = rand.nextInt(8000) + 8000;
                System.out.printf("Attempt to create server at: %d\n", port);
            }
            try {
                server = new ServerSocket(port);
            } catch (IOException e) {
            }
        } while (server == null && startingPort == 0);
        return server;
    }

    */
    //</editor-fold>
}
