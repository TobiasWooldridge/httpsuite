package httpserver;

import httpfoundation.InternalServiceError;
import httpfoundation.HTTPDialogueError;
import httpfoundation.Request;
import httpfoundation.Response;
import java.io.*;
import java.net.*;
import java.lang.Runnable;

/**
 * ConnectionThread is passed a socket from HTTPServer. It reads in a request
 * from the socket, calls the passed RequestHandler to generate a response and
 * then sends the response on the socket.
 */
class ConnectionThread implements Runnable {

    Socket connection;
    BaseRequestHandler requestHandler;

    public ConnectionThread(Socket connection, BaseRequestHandler requestHandler) {
        this.connection = connection;
        this.requestHandler = requestHandler;
    }

    public void run() {
        try {
            PrintWriter outbound = new PrintWriter(connection.getOutputStream(), true);
            BufferedReader inbound = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            // Let the response default to a 500 error if nothing is generated later
            Response response = new Response(500);

            try {
                // Read request line
                Request request = new Request(inbound.readLine());
                String input;

                // Read headers
                do {
                    input = inbound.readLine();

                    if (input == null) {
                        throw new SocketException("Connection closed by remote host");
                    } else if (input.length() > 0) {
                        request.parseRawHeader(input);
                    }
                } while (input.length() > 0);

                response = requestHandler.generateResponse(request);

                // Forcefully set content length
                response.setHeader("Content-Length", Integer.toString(response.getContent().length()));
            } catch (HTTPDialogueError e) {
                System.out.println(e);
                response = new Response(400);
            }  catch (InternalServiceError e) {
                System.out.println(e);
                response = new Response(500);
            } finally {
                outbound.print(response);
                outbound.flush();

                // HTTP 1.0 only ever handles one connection per request, close the connection after we send the response
                connection.close();
            }
        } catch (IOException e) {
            System.err.println("Error on connection " + connection + ": " + e);
        }
    }
}