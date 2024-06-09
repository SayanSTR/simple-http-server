package com.sayanstr.http;

public enum HttpMethod {
    GET, HEAD, POST, PUT, DELETE, CONNECT, OPTIONS, TRACE, PATCH;
    public static final int LONGEST_METHOD_LENGTH;

    static {
        int longest = -1;
        for(HttpMethod method : HttpMethod.values()) {
            if(method.name().length() > longest) {
                longest = method.name().length();
            }
        }
        LONGEST_METHOD_LENGTH = longest;
    }
}
