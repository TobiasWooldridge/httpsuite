package httpfoundation;

import java.util.HashMap;
import java.util.Map;
import java.lang.StringBuilder;

/**
 * Response is a representation of an HTTP response
 */
public class Response extends HTTPMessage {

    private int status;

    /**
     *
     * @param rawResponseLine
     * @throws InternalServiceError
     */
    public Response(String rawResponseLine) throws InternalServiceError {
        if (rawResponseLine == null) {
            throw new InternalServiceError("Invalid response line");
        }

        String[] tokens = rawResponseLine.split(" ");

        status = Integer.parseInt(tokens[1]);

        content = new StringBuilder();
    }

    /**
     *
     * @param statusCode
     */
    public Response(int statusCode) {
        status = statusCode;
    }

    public String toString() {
        StringBuilder completeResponse = new StringBuilder();

        // Reconstitute response string
        completeResponse.append("HTTP/" + protocol + " " + status + " " + getStatusToken() + "\r\n");

        // Reconstitute headers
        for (Map.Entry<String, String> header : headers.entrySet()) {
            completeResponse.append(header.getKey() + ": " + header.getValue() + "\r\n");
        }
        completeResponse.append("\r\n");

        // Attach content
        if (content != null) {
            completeResponse.append(content);
        }

        return completeResponse.toString();
    }

    /**
     * Covert a status integer (e.g. 404) into a message, such as "File not
     * found"
     *
     * @return String status message
     */
    public String getStatusToken() {
        String token;
        switch (status) {
            case 200:
                token = "OK";
                break;
            case 201:
                token = "Created";
                break;
            case 202:
                token = "Accepted";
                break;
            case 203:
                token = "Non-Authorative Information";
                break;
            case 204:
                token = "No Content";
                break;
            case 205:
                token = "Reset Content";
                break;
            case 206:
                token = "Partial Content";
                break;

            case 300:
                token = "Multiple Choices";
                break;
            case 301:
                token = "Moved Permanently";
                break;
            case 302:
                token = "Found";
                break;
            case 303:
                token = "See Other";
                break;
            case 304:
                token = "Not Modified";
                break;
            case 305:
                token = "Use Proxy";
                break;
            case 307:
                token = "Temporary Redirect";
                break;

            case 400:
                token = "Bad Request";
                break;
            case 401:
                token = "Unauthorized";
                break;
            case 402:
                token = "Payment Required";
                break;
            case 403:
                token = "Forbidden";
                break;
            case 404:
                token = "Not Found";
                break;
            case 405:
                token = "Method Not Allowed";
                break;
            case 406:
                token = "Not Acceptable";
                break;
            case 407:
                token = "Proxy Authentication Required";
                break;
            case 408:
                token = "Request Timeout";
                break;
            case 409:
                token = "Conflict";
                break;
            case 410:
                token = "Gone";
                break;
            case 411:
                token = "Length Required";
                break;
            case 412:
                token = "Precondition Failed";
                break;
            case 413:
                token = "Request Entity Too Large";
                break;
            case 414:
                token = "Request URI Too Long";
                break;
            case 415:
                token = "Unsupported Media Type";
                break;
            case 416:
                token = "Requested Range Not Satisfiable";
                break;
            case 417:
                token = "Expectation Failed";
                break;

            case 500:
                token = "Internal Service Error";
                break;
            case 501:
                token = "Not Implemented";
                break;
            case 502:
                token = "Bad Gateway";
                break;
            case 503:
                token = "Service Unavailable";
                break;
            case 504:
                token = "Gateway Timeout";
                break;
            case 505:
                token = "HTTP Version Not Supported";
                break;
            default:
                token = "Internal Service Error";
        }

        return token;
    }
}