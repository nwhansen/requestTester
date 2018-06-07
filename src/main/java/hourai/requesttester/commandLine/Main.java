/*
 * Copyright of Nathan Hansen 2018, No warrenty implicit or explicit is given. 
 */
package hourai.requesttester.commandline;

import hourai.requesttester.RequestProcessor;
import hourai.requesttester.RequestServer;
import hourai.requesttester.argument.ArgumentListParser;
import hourai.requesttester.argument.Argument;
import hourai.requesttester.gui.ServerGui;
import java.util.Arrays;
import java.util.List;
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
        //Download arguments
        Argument downloadMIMEArgument = Arguments.createDownloadMIME();
        Argument downloadArgument = Arguments.createDownload();
        Argument helpMIME = Arguments.createMIMEHelp();
        //Arguments for CLI ussage
        Argument portArgument = Arguments.createPort();
        Argument tailArgument = Arguments.createTailMode();
        Argument proxyArgument = Arguments.createProxy();
        Argument strategyArgument = Arguments.createUrlParameterStrategy();
        //Launch GUI
        Argument guiArgument = Arguments.createGui();
        List<Argument> arguments = Arrays.asList(helpArgument, portArgument, tailArgument, guiArgument, proxyArgument,downloadArgument, downloadMIMEArgument, helpMIME);
        ArgumentListParser parser = new ArgumentListParser(arguments);
        parser.parseArguments(args);  
        ResponseGeneratorFactory responseGenerator;
        if(helpMIME.getIsPresent()) {
            MIMEType.printAll();
            return;
        } else if(helpArgument.getIsPresent()) {
            //Build and print help then return
            System.out.print(parser.writeHelp("A tool that acts as the worlds dumbest web server, it is primarly used to mock out webservices and provides a trivial request/response dumping"));
            return;
        } else if(guiArgument.getIsPresent()) {
            //All else is ilrelevent 
            ServerGui.StartApplication();
            return;
        } 
        else if(downloadArgument.getIsPresent()) {
            System.out.println("Downloading requested url");
            WebRetriever retriever = new WebRetriever();
            if(downloadMIMEArgument.getIsPresent()) {
                String mime = MIMEType.mimeHash.get(downloadMIMEArgument.getValue());
                if(mime != null) {
                    retriever.setResponseType(mime);
                }
            }
            retriever.setResponseCode(downloadArgument.getParameters()[0]);
            retriever.download(downloadArgument.getParameters()[1], downloadArgument.getParameters()[2]);
            System.out.println("Download complete, if you modify the file please update content-length header");
            return;
        } else {
            //They didn't give us any control flow arguments?
            // I think we should start the GUI...
            try {
                ServerGui.StartApplication();
                //If they started successfully we shouldn't also start a server in the background
                return;
            } catch(Exception e) {
                //Should we fail we will start conviently as an application
            }
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
    
}
