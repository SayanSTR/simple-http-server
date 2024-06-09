package com.sayanstr.http;

public class HttpParsingException extends Exception {

    private final HttpStatus errorCode;

    public HttpParsingException(HttpStatus errorCode) {
        super(errorCode.MESSAGE);
        this.errorCode = errorCode;
    }

    public HttpStatus getErrorCode() {
        return errorCode;
    }
}
