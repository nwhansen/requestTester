/*
 *  Copyright of Nathan Hansen 2018, No warrenty implicit or explicit is given.
 */
package hourai.requesttester.implementation.filereader;

import java.io.BufferedWriter;
import hourai.requesttester.RequestReader;
import hourai.requesttester.implementation.RequestedFileFinder;
import hourai.requesttester.interfaces.ResponseGenerator;
import hourai.requesttester.interfaces.ResponseGeneratorFactory;

/**
 * Defines a Response Generator that uses files on disk that match the name of the request exactly
 * @author nhansen
 */
public class FileReaderResponseGeneratorFactory implements ResponseGeneratorFactory {

    @Override
    public ResponseGenerator createResponseGenerator(BufferedWriter writer, RequestReader reader) {
        RequestedFileFinder fileFinder = new RequestedFileFinder();
        reader.addCallback(fileFinder);
        FileReaderResponseGenerator generator = new FileReaderResponseGenerator(writer, fileFinder);
        
        return generator;
    }

}


