/*
 * Copyright of Nathan Hansen 2018, No warrenty implicit or explicit is given. 
 */
package hourai.requesttester.implementation.filereader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import hourai.requesttester.implementation.filefinders.AbstractFileFinder;
import hourai.requesttester.interfaces.ResponseGenerator;

/**
 * A class that encapsulates the logic required to write back to the stream
 *
 * @author nhansen
 */
public class FileReaderResponseGenerator implements ResponseGenerator {
    private static final Logger LOGGER = Logger.getLogger(FileReaderResponseGenerator.class.getName());
	private final BufferedWriter writer;
    private final AbstractFileFinder requestedFileFinder;

    /**
     * Creates the RepsonseGenerator for a given writer
     *
     * @param writer
     * @param requestedFileFinder 
     */
    public FileReaderResponseGenerator(BufferedWriter writer, AbstractFileFinder requestedFileFinder) {
        this.writer = writer;
        this.requestedFileFinder = requestedFileFinder;
    }

    @Override
    public void sendResponse() {
        try {
            writeResponse(requestedFileFinder.getFilename());
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Unable to read response file", e);
        }
    }

    /**
     * Writes the response to the buffered writer
     *
     * @param requestedFile The file we should attempt to read if provided
     * @throws FileNotFoundException Thrown when our file detecting logic fails
     * @throws IOException When something goes horribly wrong when writing to
     * the output
     */
    private void writeResponse(String requestedFile) throws FileNotFoundException, IOException {
        //We need to know what file if it exists
        //Now we want to check if the file exists.

        String resolvedFile = requestedFile;
        if (resolvedFile != null && new File(resolvedFile).exists()) {
            BufferedReader fileReader = new BufferedReader(new FileReader(resolvedFile));
            String currentLine;
            try {
                while ((currentLine = fileReader.readLine()) != null) {
                    writer.write(currentLine);
                    writer.write("\r\n");
                }
            } catch (IOException e) {
                //It may be a good idea to do this sadly i don't care enough
                LOGGER.log(Level.SEVERE, "Unable to write response of resolved file", e);
            }
        } else {
            // Start sending our reply, using the HTTP 1.1 protocol
            writer.write("HTTP/1.1 200 \r\n"); // Version & status code
            writer.write("Content-Type: text/plain\r\n"); // The type of data
            writer.write("Connection: close\r\n"); // Will close stream
            writer.write("\r\n"); // End of headers
        }
    }


    public static Map<String, List<String>> getQueryParams(String url) throws UnsupportedEncodingException {
        Map<String, List<String>> params = new HashMap<String, List<String>>();
        String[] urlParts = url.split("\\?");
        if (urlParts.length < 2) {
            return params;
        }

        String query = urlParts[1];
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            String key = URLDecoder.decode(pair[0], "UTF-8");
            String value = "";
            if (pair.length > 1) {
                value = URLDecoder.decode(pair[1], "UTF-8");
            }

            // skip ?& and &&
            if ("".equals(key) && pair.length == 1) {
                continue;
            }

            List<String> values = params.get(key);
            if (values == null) {
                values = new ArrayList<String>();
                params.put(key, values);
            }
            values.add(value);
        }

        return params;
    }

    @Override
    public void close() {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
		}
    }
}


