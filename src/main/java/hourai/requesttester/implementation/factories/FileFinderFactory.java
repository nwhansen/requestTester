/*
 * Copyright of Nathan Hansen 2018, No warrenty implicit or explicit is given.
 */
package hourai.requesttester.implementation.factories;

import hourai.requesttester.implementation.filefinders.AbstractFileFinder;
import hourai.requesttester.implementation.filefinders.StripFileFinder;
import hourai.requesttester.implementation.filefinders.UnderscoreFileFinder;

/**
 * A factory to provide the file finder based on user preference
 *
 * @author nhansen
 */
public class FileFinderFactory {

    public enum Method {
        Strip,
        Underscore
    }

    private final String defaultFilename;
    private final Method method;

    public FileFinderFactory(String defaultFilename, Method method) {
        this.defaultFilename = defaultFilename;
        this.method = method;
    }

    public AbstractFileFinder getFileFinder() {
        switch (method) {
            case Strip:
                return new StripFileFinder(defaultFilename);
            case Underscore:
                return new UnderscoreFileFinder(defaultFilename);
        }
        return null;
    }

}
