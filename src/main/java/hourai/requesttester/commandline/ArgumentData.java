/*
 * Copyright 2018 Nathan Hansen. No warrenty implicit or explicit is given.
 */
package hourai.requesttester.commandline;

import hourai.argumentparser.builder.ArgumentListBuilder;
import hourai.argumentparser.exception.AggregateException;
import hourai.argumentparser.interfaces.ArgumentList;
import hourai.argumentparser.interfaces.CommandLineArgument;
import hourai.argumentparser.parser.ArgumentListParser;

/**
 * The argument information for Request Tester
 */
public class ArgumentData {

    public static CommandLineArgument helpArgument;
    //Download arguments
    public static CommandLineArgument downloadMIMEArgument;
    public static CommandLineArgument downloadArgument;
    public static CommandLineArgument helpMIME;
    //Arguments for CLI ussage
    public static CommandLineArgument portArgument;
    public static CommandLineArgument tailArgument;
    public static CommandLineArgument proxyArgument;
    public static CommandLineArgument strategyArgument;
    public static CommandLineArgument logLevelArgument;
    //public static CommandLineArgument helpSubTypeArgument;
    public static CommandLineArgument noRequestFiles;
    public static CommandLineArgument tolerantProxy;
    //Launch GUI
    public static CommandLineArgument guiArgument;
    public static ArgumentList arguments;

    public static ArgumentListParser createArgumentParser() throws AggregateException {

        ArgumentListBuilder builder = new ArgumentListBuilder();
        portArgument = builder.addPositionalArgument("port", false)
                .setHelpText("The port to listen to")
                .setLongCommand("port")
                .setShortCommand("p")
                .create();
        tailArgument = builder.addArgument("tail")
                .setHelpText("Causes the application to write requests to the standard output in addition to the files")
                .setLongCommand("tail")
                .setShortCommand("t")
                .create();
        proxyArgument = builder.addArgument("proxy")
                .setHelpText("Causes the request server to forward the requests to the given url. Acting as a sort of man in the middle server")
                .setLongCommand("proxy")
                .setShortCommand("x")
                .appendParameter("url", "The URL to proxy requests to")
                .create();
        strategyArgument = builder.addArgument("strategy")
                .setHelpText("The strategy used to resolve file names when the url has parameters, and not in proxy mode")
                .setLongCommand("strategy")
                .setShortCommand("s")
                .appendParameter("strategy", "The strategy to use (for options use help argument help)")
                .create();
        downloadArgument = builder.addArgument("download")
                .setHelpText("Downloads a file and places the requested http header")
                .setLongCommand("download")
                .setShortCommand("d")
                .appendParameter("response", "The response code to give to a client which requests this file")
                .appendParameter("name", "The filename to download the url to")
                .appendParameter("url", "The URL to download")
                .create();
        downloadMIMEArgument = builder.addArgument("mime-type")
                .setHelpText("Sets the mimetype for a downloaded file, defaults to text/hmtl")
                .setLongCommand("mime-type")
                .appendParameter("mime", "The mime type such as text,html,json. (for options use help argument help)")
                .create();
        helpMIME = builder.addArgument("help-mime")
                .setHelpText("Displays the mime types")
                .setLongCommand("help-mime")
                .create();
        logLevelArgument = builder.addArgument("loglevel")
                .setHelpText("The level of logging the application will be using. By default it is off")
                .setLongCommand("loglevel")
                .setShortCommand("l")
                .appendParameter("level", "The level using standard log level, info is the most used by the interface. All strings (case insensitive): off, severe, warning, info, config, fine, finer, finest, all")
                .create();
        guiArgument = builder.addArgument("gui")
                .setHelpText("Launches a GUI if an option")
                .setLongCommand("gui")
                .create();
        tolerantProxy = builder.addArgument("tolerant")
                .setHelpText("Can the server be tolerant about 0 content length headers, when in proxy mode")
                .setLongCommand("tolerant")
                .create();
        //Help arguments
        helpArgument = builder.createTemplateHelpMessage();
        //Merge the help into a singular entry
        /*helpSubTypeArgument = builder.addArgument("Argument help")
				.setHelpText("Provides details for specific parameters with many options")
				.setLongCommand("help-parameter")
				.setShortCommand("y")
				.appendParameter("parameter", "The parameter to get specific help for, list prints all that support extended help")
				.create();*/
        noRequestFiles = builder.addArgument("no-files")
                .setHelpText("Disables writing of files")
                .setLongCommand("no-files")
                .create();
        arguments = builder.createArgumentList();
        return builder.createParser();
    }
}
