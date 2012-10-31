package httpserver;

import httpfoundation.InternalServiceError;
import httpfoundation.Request;
import httpfoundation.Response;
import java.io.*;
import java.net.*;

/**
 * ConnectionThread is passed a socket from HTTPServer. It reads in a request
 * from the socket, calls the passed RequestHandler to generate a response and
 * then sends the response on the socket.
 */
class ConnectionThread extends Thread {

    Socket connection;
    BaseRequestHandler requestHandler;

    public static void dispatch(Socket connection, BaseRequestHandler requestHandler) {
        new ConnectionThread(connection, requestHandler).start();
    }

    public ConnectionThread(Socket connection, BaseRequestHandler requestHandler) {
        super(connection.toString());

        this.connection = connection;
        this.requestHandler = requestHandler;
    }

    @Override
    public void run() {
        try {
            PrintWriter outbound = new PrintWriter(connection.getOutputStream(), true);
            BufferedReader inbound = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            // Let the response default to a 500 error if nothing is generated later
            Response response = new Response(500);

            try {
                Request request = new Request(inbound.readLine());
                String input;

                // Read request line
                do {
                    input = inbound.readLine();

                    if (input == null) {
                        throw new SocketException("Connection closed by remote host");
                    } else if (input.length() > 0) {
                        request.parseRawHeader(input);
                    }
                } while (input.length() > 0);

                // TODO (one day..): accept data

                response = requestHandler.generateResponse(request);
            } catch (InternalServiceError e) {
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