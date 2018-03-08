/*
 * Copyright of Nathan Hansen 2018, No warrenty implicit or explicit is given.
 */

package hourai.requesttester.implementation;

/**
 * Generates a unique name and stashes it until requested
 * @author nhansen
 */
public class UniqueNameGenerator {

    private String filename;
    
    public String generateNewFileName() {
        filename = (System.nanoTime()) + "";
        return filename;
    }
    
    public String getLastFileName() {
        return filename;
    }
}


