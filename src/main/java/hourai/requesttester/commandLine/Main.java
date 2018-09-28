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
import hourai.requesttester.implementation.filefinders.FileFinderFactory;
import hourai.requesttester.interfaces.RequestWriteCallback;
import hourai.requesttester.interfaces.ResponseGeneratorFactory;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.ErrorManager;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.math.BigInteger;

/**
 *
 * @author nhanlineen
 */
public class Main {

    /**
     * The main entrance of the code, parses arguments.
     *
     * @param args the command line argument line
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        int port = 0;
        //The standard help argument
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
        Argument logLevelArgument = Arguments.createLogLevel();
        //Launch GUI
        Argument guiArgument = Arguments.createGui();
        List<Argument> arguments = Arrays.asList(
                helpArgument,
                portArgument,
                tailArgument,
                guiArgument,
                proxyArgument,
                downloadArgument,
                downloadMIMEArgument,
                helpMIME,
                strategyArgument,
                logLevelArgument);
        ArgumentListParser parser = new ArgumentListParser(arguments);
        parser.parseArguments(args);
        ResponseGeneratorFactory responseGenerator;
        //Setup the log level. 
        if (logLevelArgument.getIsPresent() && !"off".equalsIgnoreCase(logLevelArgument.getValue())) {
            setupLogging(logLevelArgument.getValue());
        } else {
            setupLogging("off");
        }
        if (helpMIME.getIsPresent()) {
            MIMEType.printAll();
            return;
        } else if (helpArgument.getIsPresent()) {
            printUsage(parser);
        } else if (guiArgument.getIsPresent()) {
            startGui(logLevelArgument.getIsPresent());
            return;
        } else if (downloadArgument.getIsPresent()) {
            downloadFile(downloadMIMEArgument, downloadArgument);
        } else if (portArgument.getIsPresent()) {
            port = setPort(portArgument.getValue());
        } else {
            try {
                //They didn't give us any control flow arguments?
                // I think we should start the GUI...
                //If the log argument is present keep the terminal on lockdown
                startGui(logLevelArgument.getIsPresent());
                return;
            } catch (Exception e) {
                //Should we fail we will start conviently as an server..
            }
        }
        RequestWriteCallback callback = null;
        if (tailArgument.getIsPresent()) {
            callback = new TailRequest();
        }
        UniqueNameGenerator nameGenerator = new UniqueNameGenerator();
        if (proxyArgument.getIsPresent()) {
            responseGenerator = new ProxyServerResponseGeneratorFactory(proxyArgument.getValue(), nameGenerator, callback);
        } else {
            responseGenerator = createResponseGenerator(strategyArgument);
        }

        RequestServer server = new RequestServer(port, responseGenerator);
        if (server.getPort() == -1) {
            System.out.printf("Unable to start server for requested port %d shutting down", port);
            System.out.println();
            return;
        }
        System.out.printf("Starting server on port: %d", server.getPort());
        System.out.println();

        RequestProcessor processor = new RequestProcessor(new RequestFileWriterFactoryImpl(nameGenerator));
        RequestServerConnection connection;
        System.out.println("Awaiting Connection");
        BigInteger connectionCount = BigInteger.ONE;
        while ((connection = server.getConnection()) != null) {
            System.out.println("Recieved Connection");
            if (callback != null) {
                connection.getReader().addCallback(callback);
            }
            processor.process(connection);
            System.out.printf("Awaiting Connection (%s)", connectionCount.toString());
            System.out.println();
            connectionCount = connectionCount.add(BigInteger.ONE);
        }
    }

    private static ResponseGeneratorFactory createResponseGenerator(Argument strategyArgument) {
        ResponseGeneratorFactory responseGenerator;
        FileFinderFactory finderFactory;
        if(strategyArgument.getIsPresent()) {
            if(strategyArgument.getValue().equalsIgnoreCase("underscore")) {
                finderFactory = new FileFinderFactory(null, FileFinderFactory.Method.Underscore);
            } else {
                finderFactory = new FileFinderFactory(null, FileFinderFactory.Method.Strip);
            }
        } else {
            finderFactory = new FileFinderFactory(null, FileFinderFactory.Method.Strip);
        }
        responseGenerator = new FileReaderResponseGeneratorFactory(finderFactory);
        return responseGenerator;
    }
    
    private static void printUsage(ArgumentListParser parser) {
        //Build and print help then return
        System.out.print(parser.writeHelp("A tool that acts as the worlds dumbest web server, it is primarly used to mock out webservices and provides a trivial request/response dumping"));
        System.exit(0);
    }

    private static void downloadFile(Argument downloadMIMEArgument, Argument downloadArgument) {
        System.out.println("Downloading requested url");
        WebRetriever retriever = new WebRetriever();
        if (downloadMIMEArgument.getIsPresent()) {
            String mime = MIMEType.mimeHash.get(downloadMIMEArgument.getValue());
            if (mime != null) {
                retriever.setResponseType(mime);
            }
        }
        retriever.setResponseCode(downloadArgument.getParameters()[0]);
        retriever.download(downloadArgument.getParameters()[1], downloadArgument.getParameters()[2]);
        System.out.println("Download complete, if you modify the file please update content-length header");
        System.exit(0);
    }

    private static int setPort(String portArgument) {
        int port = -1;
        try {
            //Parse the port
            port = Integer.parseInt(portArgument);
        } catch (NumberFormatException e) {
            System.out.printf("Port parameter is not a number %s", portArgument);
            System.out.println();
            System.exit(1);
        }
        return port;
    }

    /**
     * Starts the GUI and optionally pauses main window execution
     */
    private static void startGui(boolean await) {
        //All else is ilrelevent
        ServerGui.StartApplication(await);
    }

    /**
     * Sets the logging level
     *
     * @param levelString The level string
     * @throws SecurityException
     * @throws IllegalArgumentException
     */
    private static void setupLogging(String levelString) throws SecurityException, IllegalArgumentException {
        Level level = Level.parse(levelString.toUpperCase());
		Logger root = Logger.getLogger("");
		Handler[] handlers = root.getHandlers();
		for (int i = 0; i < handlers.length; i++) {
			if (handlers[i] instanceof ConsoleHandler) {
				return;
			}
		}
        Handler consoleWriter = new Handler() {
            @Override
            public void publish(LogRecord record)
            {
                if (getFormatter() == null)
                {
                    setFormatter(new SimpleFormatter());
                }
                try {
                    String message = getFormatter().format(record);
                    if (record.getLevel().intValue() >= Level.WARNING.intValue())
                    {
                        System.err.write(message.getBytes());                       
                    }
                    else
                    {
                        System.out.write(message.getBytes());
                    }
                } catch (IOException exception) {
                    reportError(null, exception, ErrorManager.FORMAT_FAILURE);
                }

            }

            @Override
            public void close() throws SecurityException {}
            @Override
            public void flush(){}
        };
        //consoleWriter.setFormatter(new SimpleFormatter());
        root.addHandler(consoleWriter);
        root.setLevel(level);
    }

}
