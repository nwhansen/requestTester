/*
 *  Copyright of Nathan Hansen 2018, No warrenty implicit or explicit is given.
 */

package hourai.requesttester.interfaces;

import java.io.BufferedWriter;
import hourai.requesttester.RequestReader;

/**
 *
 * @author nhansen
 */
public interface ResponseGeneratorFactory {
    /**
     * 
     * @param writer The writer to send data through
     * @param reader The request reader that may effect the response generator
     * @return The response generator for response
     */
    public ResponseGenerator createResponseGenerator(BufferedWriter writer, RequestReader reader);
}




