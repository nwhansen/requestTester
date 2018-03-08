/*
 *  Copyright of Nathan Hansen 2018, No warrenty implicit or explicit is given.
 */

package hourai.requesttester.interfaces;

/**
 *
 * @author nhansen
 */
public interface ResponseGenerator {
    
    /**
     * Sends the response to the client
     */
    public void sendResponse();
    
    /**
     * Closes the response generators internal stream
     */
    public void close();
}


