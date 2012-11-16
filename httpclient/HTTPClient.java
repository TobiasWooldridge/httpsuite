package httpclient;

import java.net.*;
import java.io.*;

import httpfoundation.Request;
import httpfoundation.Response;
import httpfoundation.InternalServiceError;
import httpfoundation.HTTPDialogueError;

/**
 * HTTPClient connects to remote servers, sends requests, and parses responses.
 */
public class HTTPClient {

    public static Response simpleRequest(Request request) throws InternalServiceError {
        if (request.getHeader("Host") == null) {
            throw new InternalServiceError("No host header specified. This header is required to make HTTP requests.");
        }
        
        try {
            // Send request
            Socket connection = new Socket(request.getHeader("Host"), 80);
            
            PrintWriter outbound = new PrintWriter(connection.getOutputStream(), false);
            
            outbound.print(request.toString());
            outbound.flush();

            
            // Handle response
            BufferedReader inbound = new BufferedReader(new InputStreamReader(connection.getInputStream()));           
            
            // Get response line
            Response response = new Response(inbound.readLine());

            // Get headers
            String headerLine = null;
            while (headerLine == null || headerLine.length() > 0) {
                headerLine = inbound.readLine();
                
                if (headerLine == null) {
                    break;
                } else if (headerLine.length() > 0) {
                    response.parseRawHeader(headerLine);
                }
            }
            
            // Get content
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
            
            
            
            connection.close();
            
            return response;
        } catch (HTTPDialogueError e) {
            System.err.println("Bad HTTP Dialogue: " + e);
            throw new InternalServiceError("Bad HTTP dialogue");
        } catch (IOException e) {
            System.err.println("Error retrieving data: " + e);
            throw new InternalServiceError("Error retrieving data");
        }
    }
}
