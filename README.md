# Request Tester
This application provides a incredibly simple implementation of a webserver & basic proxy server. It writes all requests to the file system for future diagnostics

# THIS IS A TEST AND DIAGNOSTIC APPLICATION DO NOT USE THIS IN PRODUCTION ENVIROMENTS
This application lacks many of the security and safety systems implemented in modern web servers and technologies. This is supposed to be a diagnostic tool

## Basic Syntax To run
To run the application you will need java 1.6 and above. 

```java -jar requestTester.jar [options]```

If you are running an operating system with a SWING compatible UI framework and run the application with no commands it will attempt to launch the UI otherwise the application will start in server mode (no proxy) and selects a random port to use

### Quick Uses:
Create a server that will write requests and serve files in the same directory  
`java -jar requestTester.jar -p 8080`  
Create a proxy that will write requests and forward them, as well as write responses  
`java -jar requestTester.jar -p 8080 -x https://localhost`

## Server Mode
To run the application in server mode you only need to exclude the `--proxy` option. If you have a user interface you must specify a port (to prevent UI from showing)

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

### URL Resolution Strategies 

There is a command line parameter called `--strategy` this is used to control how the application will resolve your files. The default is `ignore` 

#### Ignore

Ignore simply removes all characters after the `?` (including the `?`) and resolves the path that is the result. 

This means it will resolve the following paths the same:  
`http://localhost:13286/test?a=Wizard`  
`http://localhost:13286/test?a=Muggle`

To the file: `test`
This is useful for fast and simple tests in which you are less concerned about getting the request data, vs returning a meaningful response.

This will traverse sub folders to attempt to find the file.

#### Underscore

The `underscore` option causes the application to convert the following characters into underscore 
`/\<>:\"|?*`

This allows you differentiate between requests that have specific url parameters. This does not process the request body in any fashion so form parameters are not added the entire URL is processed by this so a request to:

`http://localhost:13286/folderA/folderB/index.php?testing=1&testing=2&a=b<c`

Would be resolved to the following file  
`folderA_folderB_index.php_testing=1&testing=2&a=b_c`  
Of course this has limitations but it provides a fairly easy way to mock more sophisticated systgems.

## Proxy Mode
In this mode the application forwards any requests to the passed in URL. It is important to note that while this is technically a poor-mans man in the middle attack. It requires all inbound connections to be HTTP and not HTTPS (HTTPS stripping).  

This was for 2 reasons.
1. It isn't supposed to be used as a man-in-them-middle attack tool
2. I don't want to deal with presenting a SSL certificate. 

The main use case for this mode is you own the client application and you want to get the nitty-gritty details on the http(s) connection. And you are using a library to make the Web Request. While it can only accept HTTP requests it can make an HTTPS due to the fact that java supports that negotiation automatically via SSLSocket class, while still exposing the raw socket communication. (Yes this is how HTTPS works but it allows for trivial) usage.

Another limitation of the Proxy mode is that while most Web Browsers and HTTP clients support the server not giving a Content-Length in its response Header. This application will assume that the Content-Length is 0 and close the connection with the destination proxy server. (We are rude clients)