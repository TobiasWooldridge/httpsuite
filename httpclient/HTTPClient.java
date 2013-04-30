package httpclient;

import java.net.*;
import java.io.*;

import http.Request;
import http.Response;
import http.InternalServiceException;
import http.HTTPMessageException;

/**
 * HTTPClient connects to remote servers, sends requests, and parses responses.
 */
public class HTTPClient {

    public static Response simpleRequest(Request request) throws InternalServiceException {
        if (request.getHeader("Host") == null) {
            throw new InternalServiceException("No host header specified. This header is required to make HTTP requests.");
        }
        
        try {
            Response response;
            try (Socket connection = new Socket(request.getHeader("Host"), 80)) {
                PrintWriter outbound = new PrintWriter(connection.getOutputStream(), false);
                outbound.print(request.toString());
                outbound.flush();
                
                BufferedReader inbound = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                response = new Response(inbound.readLine());
                String headerLine = null;
                
                while (headerLine == null || headerLine.length() > 0) {
                    headerLine = inbound.readLine();
                    
                    if (headerLine == null) {
                        break;
                    } else if (headerLine.length() > 0) {
                        response.parseRawHeader(headerLine);
                    }
                }
                
                int bufferLength = 1024;
                char[] buffer = new char[bufferLength];
                
                while (true) {
                    int length = inbound.read(buffer, 0, bufferLength);
                    
                    if (length == -1) {
                        break;
                    } else if (length != bufferLength) {
                        char[] shorterBuffer = new char[length];
                        System.arraycopy(buffer, 0, shorterBuffer, 0, length);
                        response.addContent(new String(shorterBuffer));
                    } else {
                        response.addContent(new String(buffer));
                    }
                }
            }
            
            return response;
        } catch (HTTPMessageException e) {
            System.err.println("Bad HTTP Dialogue: " + e);
            throw new InternalServiceException("Bad HTTP dialogue");
        } catch (IOException e) {
            System.err.println("Error retrieving data: " + e);
            throw new InternalServiceException("Error retrieving data");
        }
    }
}
