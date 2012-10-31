package net1;

import com.sun.security.auth.module.UnixSystem;
import httpserver.HTTPServer;

class net1 {
    // ServerTimeout defines after how long idle connections are closed
    public static final int ServerTimeout = 60000;

    public static int getPort() {
        // return 15945;
        UnixSystem sys = new UnixSystem();
        long uid = sys.getUid();
        int port = 0;

        if (uid <= 1024) {
            port = (int) uid + 40000;
        } else {
            try {
                if (uid > 65535) {
                    throw new Exception("Cannot use UID as a port");
                } else {
                    port = (int) uid;
                }
            } catch (Exception e) {
                System.out.println(e);
                System.exit(1);
            }
        }

        return port;
    }

    public static void main(String[] args) {
        int port = getPort();

        System.out.println("Starting HTTP server on port " + port);

        RequestHandler requestHandler = new RequestHandler();

        HTTPServer server = new HTTPServer(port, requestHandler);
        server.setTimeout(ServerTimeout);
        server.listen();
    }
}