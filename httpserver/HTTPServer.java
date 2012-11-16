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
    private BaseRequestHandler requestHandler;
    private static int timeout = 60000;
    private static ExecutorService threadPool;

    public HTTPServer(int port, BaseRequestHandler requestHandler, int threads) {
        this.port = port;
        this.requestHandler = requestHandler;

        try {
            listeningSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Unable to listen on port " + port);
            System.exit(1);
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
                
                // Connections are handled within ConnectionThread
                ConnectionThread connectionThread = new ConnectionThread(connection, requestHandler);

                threadPool.submit(connectionThread);
            }
        } catch (IOException e) {
            System.err.println("Unable to accept connections on port " + port);
            System.exit(1);
        }
    }
}