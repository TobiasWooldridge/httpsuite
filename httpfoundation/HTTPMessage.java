package httpfoundation;

import java.util.HashMap;

import java.lang.StringBuilder;

/**
 * Base class which implements the common features of HTTP requests and
 * responses
 */
public abstract class HTTPMessage {

    protected HashMap<String, String> headers = new HashMap<String, String>();
    protected StringBuilder content;
    protected double protocol = 1.0;

    HTTPMessage() {
        content = new StringBuilder();
    }

    /**
     * Convert a HTTP header line to a header attached to this message.
     *
     * @param rawHeader
     * @throws InternalServiceError
     */
    public void parseRawHeader(String rawHeader) throws InternalServiceError {
        String expandedHeader[] = rawHeader.split(": ?", 2);

        if (expandedHeader.length != 2) {
            throw new InternalServiceError("Invalid header " + rawHeader);
        }

        setHeader(expandedHeader[0], expandedHeader[1]);
    }

    public void setHeader(String header, int value) {
        setHeader(header, Integer.toString(value));
    }

    public void setHeader(String header, String value) {
        headers.put(header, value);
    }

    public void stripHeader(String header) {
        headers.remove(header);
    }

    public String getHeader(String header) {
        return headers.get(header);
    }

    public void setProtocol(double protocol) {
        this.protocol = protocol;
    }

    public double getProtocol() {
        return protocol;
    }

    public void setContent(String content) {
        this.content = new StringBuilder(content);
    }

    public void addContent(String content) {
        this.content.append(content);
    }

    public String getContent() {
        return content.toString();
    }
}