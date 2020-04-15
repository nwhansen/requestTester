/*
 * Copyright of Nathan Hansen 2018, No warrenty implicit or explicit is given.
 */
package hourai.requesttester.implementation.filefinders;

/**
 *
 * @author nhansen
 */
public class UnderscoreFileFinder extends AbstractFileFinder {

    public UnderscoreFileFinder(String defaultFilename) {
        super(defaultFilename);
    }

    @Override
    protected void processPath(String path) {
        setFilename(underscoreAllValues(path));
    }

    private String underscoreAllValues(String path) {
        //Remove the leading / and process the rest..
        return path.substring(1).replaceAll("[/\\\\<>:\"|?*]", "_");
    }

}
