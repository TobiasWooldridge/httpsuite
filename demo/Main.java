package demo;

import httpserver.HTTPServer;
import java.io.IOException;

class Main {
    // ServerTimeout defines after how long idle connections are closed
    public static final int SERVER_TIMEOUT = 60000;

    public static int getThreadCount() {
        return 4 * Runtime.getRuntime().availableProcessors();
    }

    public static void main(String[] args) {
        int port = 80;
        SimpleRequestHandler requestHandler = new SimpleRequestHandler();
        int threadCount = getThreadCount();

        System.out.println("Starting HTTP server on port " + port + " with " + threadCount + " threads.");

        try {
            HTTPServer server = new HTTPServer(port, requestHandler, threadCount);
            server.listen();
        } catch (IOException ex) {
            System.err.println("Unable to start HTTP server. Is something else bound to port 80?");
        }
    }
}