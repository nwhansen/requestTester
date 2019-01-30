/*
 * Copyright of Nathan Hansen 2018, No warrenty implicit or explicit is given. 
 */
package hourai.requesttester.commandline;

import java.io.IOException;
import java.math.BigInteger;
import java.util.logging.ConsoleHandler;
import java.util.logging.ErrorManager;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import hourai.argumentparser.help.HelpPrinter;
import hourai.argumentparser.interfaces.ArgumentList;
import hourai.argumentparser.interfaces.CommandLineArgument;
import hourai.argumentparser.parser.ArgumentListParser;
import hourai.requesttester.RequestProcessor;
import hourai.requesttester.RequestServer;
import hourai.requesttester.data.RequestServerConnection;
import hourai.requesttester.gui.ServerGui;
import hourai.requesttester.implementation.UniqueNameGenerator;
import hourai.requesttester.implementation.factories.FileFinderFactory;
import hourai.requesttester.implementation.factories.FileReaderResponseGeneratorFactory;
import hourai.requesttester.implementation.factories.PassthroughFileWriterFactoryImpl;
import hourai.requesttester.implementation.factories.ProxyServerResponseGeneratorFactory;
import hourai.requesttester.implementation.factories.RequestFileWriterFactoryImpl;
import hourai.requesttester.implementation.reader.callbacks.TailRequest;
import hourai.requesttester.interfaces.RequestFileWriterFactory;
import hourai.requesttester.interfaces.RequestWriteCallback;
import hourai.requesttester.interfaces.ResponseGeneratorFactory;

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
		ArgumentListParser parser = ArgumentData.createArgumentParser();
		parser.parseArguments(args);
		//Setup the log level.
		if (ArgumentData.logLevelArgument.getIsPresent() && !"off".equalsIgnoreCase(ArgumentData.logLevelArgument.getValue())) {
			setupLogging(ArgumentData.logLevelArgument.getValue());
		} else {
			setupLogging("off");
		}
		if (false && ArgumentData.helpMIME.getIsPresent()) {
			MIMEType.printAll();
			return;
		} else if (ArgumentData.helpArgument.getIsPresent()) {
			printUsage(ArgumentData.arguments);
		} else if (ArgumentData.guiArgument.getIsPresent()) {
			startGui(ArgumentData.logLevelArgument.getIsPresent());
			return;
		} else if (ArgumentData.downloadArgument.getIsPresent()) {
			downloadFile(ArgumentData.downloadMIMEArgument, ArgumentData.downloadArgument);
		} else if (ArgumentData.portArgument.getIsPresent()) {
			port = setPort(ArgumentData.portArgument.getValue());
		} else if (!ArgumentData.proxyArgument.getIsPresent()) {
			try {
				//They didn't give us any control flow arguments?
				// I think we should start the GUI...
				//If the log argument is present keep the terminal on lockdown
				startGui(ArgumentData.logLevelArgument.getIsPresent());
				return;
			} catch (Exception e) {
				//Should we fail we will start conviently as an server..
			}
		}
		RequestWriteCallback callback = null;
		if (ArgumentData.tailArgument.getIsPresent()) {
			callback = new TailRequest();
		}
		ResponseGeneratorFactory responseGenerator;
		UniqueNameGenerator nameGenerator = new UniqueNameGenerator();
		if (ArgumentData.proxyArgument.getIsPresent()) {
			responseGenerator = new ProxyServerResponseGeneratorFactory(ArgumentData.proxyArgument.getValue(), nameGenerator, callback, ArgumentData.tolerantProxy.getIsPresent());
		} else {
			responseGenerator = createResponseGenerator(ArgumentData.strategyArgument);
		}

		RequestServer server = new RequestServer(port, responseGenerator);
		if (server.getPort() == -1) {
			System.out.printf("Unable to start server for requested port %d shutting down", port);
			System.out.println();
			return;
		}
		System.out.printf("Starting server on port: %d", server.getPort());
		System.out.println();
		RequestFileWriterFactory requestWriterFactory;
		if (ArgumentData.noRequestFiles.getIsPresent()) {
			requestWriterFactory = new PassthroughFileWriterFactoryImpl();
		} else {
			requestWriterFactory = new RequestFileWriterFactoryImpl(nameGenerator);
		}
		RequestProcessor processor = new RequestProcessor(requestWriterFactory);
		RequestServerConnection connection;
		System.out.println("Awaiting Connection");
		BigInteger connectionCount = BigInteger.ONE.add(BigInteger.ONE);
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

	private static ResponseGeneratorFactory createResponseGenerator(CommandLineArgument strategyArgument) {
		ResponseGeneratorFactory responseGenerator;
		FileFinderFactory finderFactory;
		if (strategyArgument.getIsPresent()) {
			if (strategyArgument.getValue().equalsIgnoreCase("underscore")) {
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

	private static void printUsage(ArgumentList argumentList) {
		HelpPrinter printer = new HelpPrinter(160);
		//Build and print help then return
		System.out.print(printer.generateHelp("A tool that acts as the worlds dumbest web server, it is primarly used to mock out webservices and provides a trivial request/response dumping", argumentList));
		System.exit(0);
	}

	private static void downloadFile(CommandLineArgument downloadMIMEArgument, CommandLineArgument downloadArgument) {
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
		Level level;
		try {
			level = Level.parse(levelString.toUpperCase());
		} catch (IllegalArgumentException ex) {
			return;
		}
		Logger root = Logger.getLogger("");
		Handler[] handlers = root.getHandlers();
		for (Handler handler : handlers) {
			if (handler instanceof ConsoleHandler) {
				root.setLevel(level);
				return;
			}
		}
		Handler consoleWriter = new Handler() {
			@Override
			public void publish(LogRecord record) {
				if (getFormatter() == null) {
					setFormatter(new SimpleFormatter());
				}
				try {
					String message = getFormatter().format(record);
					if (record.getLevel().intValue() >= Level.WARNING.intValue()) {
						System.err.write(message.getBytes());
					} else {
						System.out.write(message.getBytes());
					}
				} catch (IOException exception) {
					reportError(null, exception, ErrorManager.FORMAT_FAILURE);
				}

			}

			@Override
			public void close() throws SecurityException {
			}

			@Override
			public void flush() {
			}
		};
		//consoleWriter.setFormatter(new SimpleFormatter());
		root.addHandler(consoleWriter);
		root.setLevel(level);
	}


}
