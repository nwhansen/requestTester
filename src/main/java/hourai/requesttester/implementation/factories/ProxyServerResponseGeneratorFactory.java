/*
 * Copyright of Nathan Hansen 2018, No warrenty implicit or explicit is given.
 */

package hourai.requesttester.implementation.factories;

import java.io.BufferedWriter;

import hourai.requesttester.RequestReader;
import hourai.requesttester.implementation.UniqueNameGenerator;
import hourai.requesttester.implementation.proxy.ProxyServerResponseGenerator;
import hourai.requesttester.interfaces.RequestWriteCallback;
import hourai.requesttester.interfaces.ResponseGenerator;
import hourai.requesttester.interfaces.ResponseGeneratorFactory;

/**
 *
 * @author nhansen
 */
public class ProxyServerResponseGeneratorFactory implements ResponseGeneratorFactory {
    
    private final String url;
    private final UniqueNameGenerator nameGenerator;
	private final RequestWriteCallback callback;
	private final boolean faultTolerant;

	/**
	 * A factory for creating response generators
	 *
	 * @param url
	 * @param uniqueNameGenerator
	 * @param callback
	 * @param tolerantConnection
	 */
	public ProxyServerResponseGeneratorFactory(String url, UniqueNameGenerator uniqueNameGenerator, RequestWriteCallback callback, boolean tolerantConnection) {
        this.url = url; 
		this.callback = callback;
		this.faultTolerant = tolerantConnection;
        nameGenerator = uniqueNameGenerator;
    }
    
    @Override
	public ResponseGenerator createResponseGenerator(BufferedWriter writer, RequestReader reader) {
		ProxyServerResponseGenerator proxyGenerator = new ProxyServerResponseGenerator(writer, url, nameGenerator, faultTolerant);
        proxyGenerator.setProxyWriteCallback(callback);
        reader.addCallback(proxyGenerator);
        return proxyGenerator;
    }

}



