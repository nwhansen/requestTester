/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hourai.requesttester.implementation.filefinders;

import hourai.requesttester.interfaces.RequestWriteCallback;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nhansen
 */
public abstract class AbstractFileFinder implements RequestWriteCallback{
    private final static Logger LOGGER = Logger.getLogger(AbstractFileFinder.class.getName());
    private String filename;
    private final String defaultFilename; 
    
    /**
     * Creates the abstract file finder that implements common logic for all file finders
     * @param defaultFilename The file name to return if there was no file specified (for example index.html)
     */
    public AbstractFileFinder(String defaultFilename) {
        this.defaultFilename = defaultFilename;
    }
    
    public void recievedLine(String line) {
        if (filename == null) {
            if (hasVerb(line)) {
                String[] parts = line.split(" ");
                processPath(parts[1]);
                if(filename == null) {
                    LOGGER.log(Level.WARNING, String.format("The path: `%s` did not result in a filename", parts[1]));
                    filename = "";
                }
            }
        }
    }

    public void recievedChar(char c) {
        //No-Op
    }
    
    protected abstract void processPath(String path);
    
    public String getFilename() {
        if("".equals(filename)) {
            return defaultFilename;
        }
        return filename;
    }
    
    protected void setFilename(String filename) {
        this.filename = filename;
        LOGGER.log(Level.INFO, String.format("Will attempt to return `%s`", filename));
    }
    private static boolean hasVerb(String line) {
        return line.startsWith("GET")
                || line.startsWith("POST")
                || line.startsWith("PUT")
                || line.startsWith("PATCH")
                || line.startsWith("DELETE");
    }
}

