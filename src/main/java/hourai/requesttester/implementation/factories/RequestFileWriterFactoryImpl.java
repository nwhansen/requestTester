/*
 * Copyright of Nathan Hansen 2018, No warrenty implicit or explicit is given.
 */

package hourai.requesttester.implementation.factories;

import hourai.requesttester.implementation.UniqueNameGenerator;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import hourai.requesttester.implementation.reader.callbacks.RequestFileWriter;
import hourai.requesttester.interfaces.RequestFileWriterFactory;

/**
 * The trivial implementation of the RequestFileWriterFactory
 * @author nhansen
 */
public class RequestFileWriterFactoryImpl implements RequestFileWriterFactory {

    private final UniqueNameGenerator nameGenerator;

    public RequestFileWriterFactoryImpl(UniqueNameGenerator nameGenerator) {
        this.nameGenerator = nameGenerator;
    }
     
    @Override
    public RequestFileWriter create() {
        
        //Generate the filename
        String filename = nameGenerator.generateNewFileName() + ".request";
        
        try {
            //Create the writer
            RequestFileWriter fileWriter = new RequestFileWriter(new PrintWriter(filename));
            
            return fileWriter;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RequestFileWriterFactoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }
}




