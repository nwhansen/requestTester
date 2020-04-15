/*
 * Copyright of Nathan Hansen 2018, No warrenty implicit or explicit is given.
 */
package hourai.requesttester.implementation.filefinders;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class which finds the requested file striping out leading '/'
 *
 * @author nhansen
 */
public class StripFileFinder extends AbstractFileFinder {

    private static final Logger logger = Logger.getLogger(StripFileFinder.class.getName());

    public StripFileFinder(String defaultFilename) {
        super(defaultFilename);
    }

    @Override
    protected void processPath(String path) {
        setFilename(stripParamaters(path));
    }

    private String stripParamaters(String requestedPath) {
        //In here we simple return the part before the ? this is the only strategy i can come up with right now
        String[] urlPart = requestedPath.split("\\?");
        if (urlPart != null && urlPart.length > 1) {
            //We have params
            logger.log(Level.INFO, String.format("Ignoring url parameters: %s", urlPart[1]));
            //Remove the leading '/'
            return urlPart[0].substring(1);
        }
        String filename = requestedPath.substring(1);
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, String.format("Returning filename `%s`", filename));
        }
        return filename;
    }
}
