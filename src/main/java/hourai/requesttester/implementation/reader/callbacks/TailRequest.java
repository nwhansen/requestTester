/*
 * Copyright of Nathan Hansen 2018, No warrenty implicit or explicit is given. 
 */
package hourai.requesttester.implementation.reader.callbacks;

import hourai.requesttester.interfaces.RequestWriteCallback;

/**
 * A class that tails the request to console out
 *
 * @author nhansen
 */
public class TailRequest implements RequestWriteCallback {
            
    @Override
    public void recievedLine(String line) {
        System.out.println(line);
    }

    @Override
    public void recievedChar(char c) {
        System.out.print(c);
    }


}




