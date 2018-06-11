/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hourai.requesttester.implementation.filefinders;

/**
 * A factory to provide the file finder based on user preference
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
        this.method  = method;
    }
    
    public AbstractFileFinder getFileFinder() {
        switch(method) {
            case Strip: return new StripFileFinder(defaultFilename);
            case Underscore: return new UnderscoreFileFinder(defaultFilename);      
        }
        return null;
    }
    
}
