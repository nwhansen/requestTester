/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
            Logger.getLogger(StripFileFinder.class.getName()).log(Level.INFO, String.format("Ignoring url parameters: %s", urlPart[1]));
            //Remove the leading '/'
            return urlPart[0].substring(1);
        }
		return requestedPath.substring(1);
    }
}




