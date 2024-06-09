package com.sayanstr.http;

public enum HttpStatus {

    /* ---CLIENT ERRORS--- */
    CLIENT_ERROR_400_BAD_REQUEST(400, "Bad Request"),
    CLIENT_ERROR_401_NOT_ALLOWED(401, "Not Allowed"),
    CLIENT_ERROR_414_URI_TOO_LONG(400, "URI Too Long"),

    /* ---SERVER ERRORS--- */
    SERVER_ERROR_500_INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    SERVER_ERROR_501_NOT_IMPLEMENTED(501, "Not Implemented"),
    SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported");


    public final int STATUS_CODE;
    public final String MESSAGE;

    HttpStatus(int statusCode, String message) {
        this.STATUS_CODE = statusCode;
        this.MESSAGE = message;
    }

}
