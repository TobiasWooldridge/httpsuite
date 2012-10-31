package httpserver;

import httpfoundation.Request;
import httpfoundation.Response;
import httpfoundation.InternalServiceError;

public abstract class BaseRequestHandler {
	abstract public Response generateResponse(Request request) throws InternalServiceError;
}