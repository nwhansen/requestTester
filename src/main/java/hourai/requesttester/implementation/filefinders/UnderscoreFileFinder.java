/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
