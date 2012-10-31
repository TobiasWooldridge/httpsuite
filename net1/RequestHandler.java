package net1;

import httpclient.HTTPClient;
import httpfoundation.InternalServiceError;
import httpfoundation.Request;
import httpfoundation.Response;
import httpserver.BaseRequestHandler;

class RequestHandler extends BaseRequestHandler {

    /**
     * Accept a Request from a client and somehow generate a Response to be
     * sent back to the client.
     * 
     * @param request
     * @return response
     * @throws InternalServiceError
     */
    @Override
    public Response generateResponse(Request request) throws InternalServiceError {
        Response response;

        if (request.getMethod().equals("GET") && request.getPath().equals("/")) {
            String pageContent;

            try {
                FileReader reader = new FileReader("./README.md");
                pageContent = reader.read();
            }
            catch (Exception e) {
                throw new InternalServiceError("File could not be read");
            }

            response = new Response(200);
            response.setHeader("Content-Type", "text/plain");
            response.setContent(pageContent);
        }

        else {
            response = new Response(404);
            response.setHeader("Content-Type", "text/html");
            response.setContent("<!doctype HTML><html><body><h1>File not found</h1></body></html>");
        }
        return response;
    }
    
}