/*
 *  Copyright of Nathan Hansen 2018, No warrenty implicit or explicit is given.
 */

package hourai.requesttester;

import hourai.requesttester.interfaces.RequestWriteCallback;

/**
 *
 * @author nhansen
 */
public class RequestForwarder implements RequestWriteCallback {

    //We know need the information to forward oursevles. 
    
    @Override
    public void recievedLine(String line) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void recievedChar(char c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}




