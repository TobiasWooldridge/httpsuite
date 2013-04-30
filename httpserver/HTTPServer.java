package httpserver;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

/**
 * HTTPServer listens for connections to the server opening and dispatches them
 * to ConnectionThreads.
 */
public class HTTPServer {

    private int port;
    private ServerSocket listeningSocket;
    private RequestHandler requestHandler;
    private static int timeout = 60000;
    private static ExecutorService threadPool;

    public HTTPServer(int port, RequestHandler requestHandler, int threads) throws IOException {
        this.port = port;
        this.requestHandler = requestHandler;

        try {
            listeningSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new IOException("Unable to start server, port unavailable", e);
        }

        threadPool = Executors.newFixedThreadPool(threads);
    }

    public void setTimeout(int newTimeout) {
        timeout = newTimeout;
    }

    // This accepts connections and dispatches them to new threads
    public void listen() {
        try {
            while (true) {
                Socket connection = listeningSocket.accept();
                connection.setSoTimeout(timeout);
                
                System.out.println("New connection " + connection);
                
                ConnectionResponder connectionThread = new ConnectionResponder(connection, requestHandler);

                threadPool.submit(connectionThread);
            }
        } catch (IOException e) {
            System.err.println("Unable to accept connections on port " + port);
            System.exit(1);
        }
    }
}