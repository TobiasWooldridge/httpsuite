## Implementation

HTTPServer dispatches sockets created from the listened port to
ConnectionThread.

ConnectionThread reads input from the http request, reading it into a
httpfoundation.Request object, generates a response using net1.RequestHandler,
and writes the generated response back to the socket.

RequestHandler, in particular, processes how to handle the request. The
implementation is specific to how the server is being used. In the case of the
net1 script, RequestHandler will send the readme if / was requested or will send
a 404 File Not Found if any other path was requested.

InternalServiceError is used for errors relating to HTTP. If an
InternalServiceError occurs, a HTTP 500 Internal Service Error will be
dispatched. This is messy, and I'd prefer to use more explicit exceptions that
specify the cause of the error and choose to send an Internal Service Error
only at the HttpServer level.


HTTPServer is threaded, but there is no limit on the number of threads it can
spawn. I'd like to fix this soonish (as having a simple Java server could come
in handy some time in the future) and limit the number of threads to a variable
defined on the HTTPServer object.

HTTP 400 (Bad Request) errors are not implemented yet; though I intend to do
this some time soon.



## Usage
To build the project, run

    make


To build and run

    make run
