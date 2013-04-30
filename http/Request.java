package http;

import java.util.Map;

/**
 * Request is a representation of an HTTP request
 */
public class Request extends HTTPMessage {

    private String method;
    private String path;

    /**
     * Create a Request, initializing it by pre-filling values from the passed
     * request line
     * 
     * @param requestLine
     * @throws HTTPMessageException
     */
    public Request(String requestLine) throws HTTPMessageException {
        parseRawRequestLine(requestLine);
    }

    /**
     * Break a raw request line (the first line of an HTTP message) into its
     * parts and add them to this object.
     * 
     * @param requestLine
     * @throws HTTPMessageException
     */
    private void parseRawRequestLine(String requestLine) throws HTTPMessageException {
        if (requestLine == null) {
            throw new HTTPMessageException("Null request line");
        }

        String[] requestParameters = requestLine.split(" ");

        if (requestParameters.length < 2 || requestParameters.length > 3) {
            throw new HTTPMessageException("Invalid number of parameters in request line");
        }

        method = requestParameters[0];
        path = requestParameters[1];
        
        // If there's a third parameter (which says HTTP version) find out what
        // it is
        if (requestParameters.length >= 3) {
            String[] expandedProtocol = requestParameters[2].split("/");

            if (expandedProtocol.length != 2 || !expandedProtocol[0].equals("HTTP")) {
                throw new HTTPMessageException("Invalid protocol specified in request line");
            }

            try {
                protocol = Double.parseDouble(expandedProtocol[1]);
            } catch (Exception e) {
                throw new HTTPMessageException("Non-numeric or missing HTTP version specified");
            }
        }
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        StringBuilder completeRequest = new StringBuilder();
        
        // Reconstitute request string
        completeRequest.append(method).append(" ").append(path).append(" HTTP/").append(protocol).append("\r\n");

        // Reconstitute headers
        for (Map.Entry<String, String> header : headers.entrySet()) {
            completeRequest.append(header.getKey()).append(": ").append(header.getValue()).append("\r\n");
        }
        completeRequest.append("\r\n");
        
        // Attach content
        if (content != null) {
            completeRequest.append(content);
            completeRequest.append("\r\n");
        }
        completeRequest.append("\r\n");

        return completeRequest.toString();
    }
}