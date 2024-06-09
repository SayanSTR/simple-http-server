package com.sayanstr.http;

public class HttpRequest extends HttpMessage {
    private HttpMethod method;
    private String requestTarget;
    private String originalHttpVersion;
    private HttpVersion bestCompatibleHttpVersion;

    HttpRequest() {
    }

    public HttpMethod getMethod() {
        return method;
    }

    void setMethod(String method) throws HttpParsingException {
        for (HttpMethod httpMethod : HttpMethod.values()) {
            if (httpMethod.name().equals(method)) {
                this.method = httpMethod;
                return;
            }
        }
        throw new HttpParsingException(
                HttpStatus.SERVER_ERROR_501_NOT_IMPLEMENTED
        );
    }

    public String getRequestTarget() {
        return this.requestTarget;
    }

    void setRequestTarget(String requestTarget) throws HttpParsingException {
        if (requestTarget == null || requestTarget.isEmpty()) {
            throw new HttpParsingException(
                    HttpStatus.SERVER_ERROR_500_INTERNAL_SERVER_ERROR
            );
        }
        this.requestTarget = requestTarget;
    }

    public void setHttpVersion(String originalHttpVersion) throws BadHttpVersionException, HttpParsingException {
        this.originalHttpVersion = originalHttpVersion;
        this.bestCompatibleHttpVersion = HttpVersion.getBestCompatibleVersion(originalHttpVersion);
        if(this.bestCompatibleHttpVersion == null) {
            throw new HttpParsingException(
                    HttpStatus.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED
            );
        }
    }

    public HttpVersion getBestCompatibleHttpVersion() {
        return this.bestCompatibleHttpVersion;
    }

    public String getOriginalHttpVersion() {
        return this.originalHttpVersion;
    }
}
