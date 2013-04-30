package demo;

import http.InternalServiceException;
import http.NotFoundException;
import http.Request;
import http.Response;
import httpserver.RequestHandler;
import java.util.HashMap;
import java.util.Map;

class SimpleRequestHandler extends RequestHandler {
    private FileReader fileReader;
    private Map<String, String> fileRoutes;

    public SimpleRequestHandler() {
        fileReader = new FileReader();
        fileRoutes = new HashMap<>();
        
        fileRoutes.put("/", "./README.md");
    }


    /**
     * Accept a Request from a client and somehow generate a Response to be
     * sent back to the client.
     * 
     * @param request
     * @return response
     * @throws InternalServiceException
     */
    @Override
    public Response generateResponse(Request request) {
        Response response;

        try {
            if (request.getMethod().equals("GET") && fileRoutes.containsKey(request.getPath())) {
                String pageContent;

                try {
                    pageContent = fileReader.read(fileRoutes.get(request.getPath()));
                }
                catch (Exception e) {
                    System.err.println(e);
                    throw new NotFoundException("Unable to read routed file");
                }

                response = createResponse(200);
                response.setHeader("Content-Type", "text/plain");
                response.setContent(pageContent);

                return response;
            }
            else {
                throw new NotFoundException("Unrouted file requested");
            }
        }
        catch (NotFoundException e) {
            System.err.println(e);
            return createResponse(404);
        }
    }
}