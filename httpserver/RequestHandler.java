package httpserver;

import http.Request;
import http.Response;

public abstract class RequestHandler {
    public abstract Response generateResponse(Request request);
    
    public Response createResponse(int status) {
        Response response = new Response(status);
        response.setHeader("Content-Type", "text/html");
        response.setContent("<!doctype HTML><html><body><h1>" + response.getStatus() + " " + response.getStatusToken() + "</h1></body></html>");
        
        return response;
    }
}