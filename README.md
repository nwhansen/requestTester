# Request Tester
This application provides a incredibly simple implementation of a webserver & basic proxy server. It writes all requests to the file system for future diagnostics

# THIS IS A TEST AND DIAGNOSTIC APPLICATION DO NOT USE THIS IN PRODUCTION ENVIROMENTS
This application lacks many of the security and safety systems implemented in modern web servers and technologies. This is supposed to be a diagnostic tool

## Basic Syntax To run
To run the application you will need java 1.6 and above. 

```java -jar requestTester.jar [options]```

If you are running an operating system with a SWING compatible UI framework and run the application with no commands it will attempt to launch the UI otherwise the application will start in server mode (no proxy) and selects a random port to use

## Server Mode
To run the application in server mode you only need to exclude the `--proxy` option. 

This mode acts like an HTTP/1.1 webserver. It handles the basic protocol information. This mode will attempt to return a file from disk if the path after `/` is a valid file (This will return ANY file that matches the path)

The file that is returned must contain the HTTP response headers otherwise the request can fail.

For example if you wanted to return a simple hello world page you would make a response file that looks like

```
HTTP/1.1 200
Content-Type: text/html
Content-Length: 46

<HTML><BODY><H1>HELLO WORLD</H1></BODY></HTML>
```

A couple of important notes about the HTTP/1.1 protocol.
1. The content-length is not strictly required however it greatly eases the consumer application that you may be testing.
2. The New Line between Content-length and the response payload is required. However all new lines in the file are automatically converted to the correct `\r\n` to be HTTP compliant. 

### Additional Features 

Currently any Uri parameters that are passed are striped from the request and only the Uri part before the `?` are used to resolve the file.

This is a feature enhancement to create a method which may "route" the call to a file on disk (something like a bash script, or another executable) which would be responsible for writing to standard out the response. This can allow for a level of capability which makes it questionable if this is the right application

## Proxy Mode
In this mode the application forwards any requests to the passed in URL. It is important to note that while this is technically a poor-mans man in the middle attack. It requires all inbound connections to be HTTP and not HTTPS (HTTPS stripping).  

This was for 2 reasons.
1. It isn't supposed to be used as a man-in-them-middle attack tool
2. I don't want to deal with presenting a SSL certificate. 

The main use case for this mode is you own the client application and you want to get the nitty-gritty details on the http(s) connection. And you are using a library to make the Web Request. While it can only accept HTTP requests it can make an HTTPS due to the fact that java supports that negotiation automatically via SSLSocket class, while still exposing the raw socket communication. (Yes this is how HTTPS works but it allows for trivial) usage.

Another limitation of the Proxy mode is that while most Web Browsers and HTTP clients support the server not giving a Content-Length in its response Header. This application will assume that the Content-Length is 0 and close the connection with the destination proxy server. (We are rude clients)