/*
 * Copyright of Nathan Hansen 2018, No warrenty implicit or explicit is given.
 */
package hourai.requesttester.implementation.reader.callbacks;

import java.io.Closeable;
import java.io.PrintWriter;

import hourai.requesttester.interfaces.RequestWriteCallback;

/**
 * A RequestWrite callback that writes the request to disk
 *
 * @author nhansen
 */
public class RequestFileWriter implements RequestWriteCallback, Closeable {

	private final PrintWriter fileWriter;

	public RequestFileWriter(PrintWriter fileWriter) {
		this.fileWriter = fileWriter;
	}

	@Override
	public void recievedLine(String line) {
		fileWriter.println(line);
		//Flush to disk
		fileWriter.flush();
	}

	@Override
	public void recievedChar(char c) {
		fileWriter.print(c);
	}

	@Override
	public void close() {
		fileWriter.close();
	}
}
