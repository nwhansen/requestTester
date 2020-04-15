/*
 * Copyright 2018 Nathan Hansen. No warrenty implicit or explicit is given.
 */

package hourai.requesttester.implementation.factories;

import hourai.requesttester.implementation.reader.callbacks.PassthroughFileWriter;
import hourai.requesttester.interfaces.RequestFileWriterFactory;
import hourai.requesttester.interfaces.RequestWriteCallback;

/**
 * Returns nothing for a request file factory.
 */
public class PassthroughFileWriterFactoryImpl implements RequestFileWriterFactory {

	@Override
	public RequestWriteCallback create() {
		return new PassthroughFileWriter();
	}

}
