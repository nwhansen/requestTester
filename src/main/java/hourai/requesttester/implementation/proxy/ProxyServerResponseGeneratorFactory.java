/*
 * Copyright of Nathan Hansen 2018, No warrenty implicit or explicit is given.
 */

package hourai.requesttester.implementation.proxy;

import hourai.requesttester.implementation.proxy.ProxyServerResponseGenerator;
import java.io.BufferedWriter;
import hourai.requesttester.RequestReader;
import hourai.requesttester.implementation.UniqueNameGenerator;
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

    public ProxyServerResponseGeneratorFactory(String url, UniqueNameGenerator uniqueNameGenerator, RequestWriteCallback callback) {
        this.url = url; 
        this.callback = callback;
        nameGenerator = uniqueNameGenerator;
    }
    
    @Override
    public ResponseGenerator createResponseGenerator(BufferedWriter writer, RequestReader reader) {
        ProxyServerResponseGenerator proxyGenerator = new ProxyServerResponseGenerator(writer, url, nameGenerator);
        proxyGenerator.setProxyWriteCallback(callback);
        reader.addCallback(proxyGenerator);
        return proxyGenerator;
    }

}



