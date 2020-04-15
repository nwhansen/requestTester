/*
 * Copyright 2018 Nathan Hansen. No warrenty implicit or explicit is given.
 */

package hourai.requesttester.implementation.reader.callbacks;

import java.util.logging.Level;
import java.util.logging.Logger;

import hourai.requesttester.interfaces.RequestWriteCallback;

/**
 * This service just ignores the data logs it at finer/finest.
 */
public class PassthroughFileWriter implements RequestWriteCallback {

	private static final Logger logger = Logger.getLogger(PassthroughFileWriter.class.getName());

	@Override
	public void recievedLine(String line) {
		logger.log(Level.FINER, line);
	}

	@Override
	public void recievedChar(char c) {
		//Only on the finest of logs we dump this.
		// This will be far less helpful if tail is on
		logger.log(Level.FINEST, String.valueOf(c));
	}

}
