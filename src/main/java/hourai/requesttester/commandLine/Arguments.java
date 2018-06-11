/*
 * Copyright of Nathan Hansen 2018, No warrenty implicit or explicit is given. 
 */
package hourai.requesttester.commandline;

import hourai.requesttester.argument.Argument;
import hourai.requesttester.argument.ArgumentHelp;
/**
 *
 * @author nhansen
 */
public class Arguments {
    
    private static ArgumentHelp createHelpMessage(String name, String message) {
        ArgumentHelp helpMessage = new ArgumentHelp();
        helpMessage.setArgumentHelpText(message);
        helpMessage.setCommandLineName(name);
        return helpMessage; 
     }
   
    private static  ArgumentHelp createHelpMessage(String name, String message, String...parameters) {
        ArgumentHelp helpMessage = new ArgumentHelp();
        helpMessage.setArgumentHelpText(message);
        helpMessage.setCommandLineName(name);
        helpMessage.setArgumentParametersHelpText(parameters);
        return helpMessage;
    }
    
    public static Argument createHelp() {
        Argument help = new Argument();
        help.setLongCommand("help");
        help.setShortCommand("h");
        help.setHelp(createHelpMessage("help", "This message"));
        return help;
    }
    
    public static Argument createProxy() {
        Argument proxy = new Argument();
        proxy.setLongCommand("proxy");
        proxy.setShortCommand("x");
        proxy.setParameterCount(1);
        proxy.setHelp(createHelpMessage("proxy", "Causes the request server to forward the requests to the given url. Acting as a intentional man in the middle server", 
                "URL", "The url to proxy to"));
        
        return proxy;
    }
      
    public static  Argument createDownload() {
        Argument download = new Argument();
        download.setLongCommand("download");
        download.setShortCommand("d");
        download.setParameterCount(3);
        download.setHelp(createHelpMessage("download",
                        "Downloads a file and places the requested http header", 
                        "response", "The response code",
                        "name", "The filename to download the page to",
                        "url", "The URL to download"));
        return download;
    }
    
    public static Argument createDownloadMIME() {
        Argument downloadMIME = new Argument();
        downloadMIME.setLongCommand("mime-type");
        downloadMIME.setParameterCount(1);
        downloadMIME.setHelp(createHelpMessage("mime-type", 
                "Sets the mimetype for a downloaded file, defaults to text/hmtl", 
                "mime", "the mime type such as text,html,json etc"));
        return downloadMIME;
    }
    
    public static Argument createMIMEHelp() {
        Argument helpMIME = new Argument();
        helpMIME.setLongCommand("help-mime-types");
        helpMIME.setHelp(createHelpMessage("help-mime-types", "Lists the supported mime types and the exists"));
        return helpMIME;
    }
    
    public static  Argument createPort() {
        Argument port = new Argument();
        port.setPositional(0);
        port.setPositionalRequred(false);
        port.setShortCommand("p");
        port.setLongCommand("port");
        port.setParameterCount(1);
        port.setHelp(createHelpMessage("port", "The port to start the server on", "port", "The port number to open"));
        return port;
    }

    public static Argument createGui() {
        Argument gui = new Argument();
        gui.setShortCommand(null);
        gui.setLongCommand("gui");
        gui.setParameterCount(0);
        gui.setHelp(createHelpMessage("gui", "Launches a GUI for this application"));
        return gui;
    }
    
    public static Argument createTailMode() {
        Argument tail = new Argument();
        tail.setShortCommand("t");
        tail.setLongCommand("tail");
        tail.setParameterCount(0);
        tail.setHelp(createHelpMessage("tail", "Causes the application to write requests to the standard output in addition to the file"));
        return tail;
    }
    
    public static Argument createUrlParameterStrategy() {
        Argument urlParameterStategy = new Argument();
        urlParameterStategy.setLongCommand("strategy");
        urlParameterStategy.setShortCommand("s");
        urlParameterStategy.setParameterCount(1);
        urlParameterStategy.setHelp(createHelpMessage("strategy", "The strategy used to resolve file names when the url has parameters, and not in proxy mode", 
                "ignore", "[Default] Ignores the parameters and just returns the file on disk",
                "underscore", "Converts all windows non-valid characters commonly used in url parameters into underscores, useful for simple testing with url parameters [/\\<>:\"|?*]"));
        
        return urlParameterStategy;
    }
    
    public static Argument createLogLevel() {
        Argument logLevel = new Argument();
        logLevel.setLongCommand("logLevel");
        logLevel.setShortCommand("l");
        logLevel.setParameterCount(1);
        logLevel.setHelp(createHelpMessage("loglevel", "The level of logging the application will be using. By default it is off", 
                "level", "The level using standard log level, info is the most used by the interface. All strings (case insensitive): off, severe, warning, info, config, fine, finer, finest, all"));
        return logLevel;
    }
}





