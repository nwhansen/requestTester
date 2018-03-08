/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hourai.requesttester.implementation;

import hourai.requesttester.interfaces.RequestWriteCallback;

/**
 * A class which finds the requested file striping out leading '/'
 *
 * @author nhansen
 */
public class RequestedFileFinder implements RequestWriteCallback {

    private String requestedFile = null;
    private String requestedMethod = null;
    
    @Override
    public void recievedLine(String line) {
        if (requestedFile == null) {
            if (hasVerb(line)) {
                String[] parts = line.split(" ");
                if (parts != null && parts.length > 1) {
                    requestedFile = parts[1].substring(1);
                    requestedMethod = parts[0];
                } else {
                    requestedFile = "";
                }
            }
        }
    }

    @Override
    public void recievedChar(char c) {
        // No-op
    }

    /**
     * @return the requestedFile from the request, null if they did not request a file
     */
    public String getRequestedFile() {
        return "".equals(requestedFile) ? null : requestedFile;
    }

    public String getRequestedMethod() {
        return requestedMethod;
    }
    
    private boolean hasVerb(String line) {
        return line.startsWith("GET")
                || line.startsWith("POST")
                || line.startsWith("PUT")
                || line.startsWith("PATCH")
                || line.startsWith("DELETE");
    }
}




