/*
 * Copyright of Nathan Hansen 2018, No warrenty implicit or explicit is given.
 */

package hourai.requesttester.interfaces;

/**
 *
 * @author nhansen
 */
public interface RequestFileWriterFactory {

    /**
     * Creates a Request File writer
     * @return The request file writer to be used
     */
	RequestWriteCallback create();
    
}




