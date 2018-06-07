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
        //This combo is unlikley to ever collide. (but names are long)
        filename = (System.currentTimeMillis()) + "-" + (System.nanoTime());
        return filename;
    }
    
    public String getLastFileName() {
        return filename;
    }
}


