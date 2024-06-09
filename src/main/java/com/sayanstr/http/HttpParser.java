package com.sayanstr.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class HttpParser {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);

    private static final int SP = 0x20; // 32
    private static final int CR = 0x0D; // 13
    private static final int LF = 0x0A; // 10

    public HttpRequest parseHttpRequest(InputStream inputStream) throws HttpParsingException {
        LOGGER.info("Parsing HTTP request ...");
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);

        HttpRequest httpRequest = new HttpRequest();
        try {
            parseRequestLine(inputStreamReader, httpRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        parseHeaders(inputStreamReader, httpRequest);
        parseBody(inputStreamReader, httpRequest);


        return httpRequest;
    }

    private void parseRequestLine(InputStreamReader inputStreamReader, HttpRequest httpRequest) throws IOException, HttpParsingException {
        LOGGER.info("Parsing request line ...");
        StringBuilder processingDataBuffer = new StringBuilder();
        boolean isMethodParsed = false;
        boolean isRequestTargetParsed = false;
        int _byte;
        while((_byte = inputStreamReader.read()) >= 0) {
            if(_byte == CR) {
                _byte = inputStreamReader.read();
                if(_byte == LF) {
                    LOGGER.debug("Request Line VERSION: {}", processingDataBuffer.toString());
                    if(!isRequestTargetParsed) {
                        throw new HttpParsingException(
                                HttpStatus.CLIENT_ERROR_400_BAD_REQUEST
                        );
                    }
                    try {
                        httpRequest.setHttpVersion(processingDataBuffer.toString());
                    } catch (BadHttpVersionException e) {
                        throw new HttpParsingException(HttpStatus.CLIENT_ERROR_400_BAD_REQUEST);
                    }
                    return;
                } else {
                    throw new HttpParsingException(
                            HttpStatus.CLIENT_ERROR_400_BAD_REQUEST
                    );
                }
            }

            if(_byte == SP) {
                if(!isMethodParsed) {
                    LOGGER.debug("Request Line METHOD: {}", processingDataBuffer.toString());
                    httpRequest.setMethod(processingDataBuffer.toString());
                    isMethodParsed = true;
                } else if(!isRequestTargetParsed) {
                    LOGGER.debug("Request Line REQUEST TARGET: {}", processingDataBuffer.toString());
                    httpRequest.setRequestTarget(processingDataBuffer.toString());
                    isRequestTargetParsed = true;
                } else {
                    throw new HttpParsingException(
                            HttpStatus.CLIENT_ERROR_400_BAD_REQUEST
                    );
                }
                //LOGGER.debug("Request token: {}", processingDataBuffer.toString());
                processingDataBuffer.delete(0, processingDataBuffer.length());
            } else {
                processingDataBuffer.append((char) _byte);
                if(!isMethodParsed) {
                    if(processingDataBuffer.length() > HttpMethod.LONGEST_METHOD_LENGTH) {
                        throw new HttpParsingException(
                                HttpStatus.SERVER_ERROR_501_NOT_IMPLEMENTED
                        );
                    }
                }
            }
        }
    }

    private void parseHeaders(InputStreamReader inputStreamReader, HttpRequest httpRequest) {
        LOGGER.info("Parsing headers ...");
    }

    private void parseBody(InputStreamReader inputStreamReader, HttpRequest httpRequest) {
        LOGGER.info("Parsing body ...");
    }
}
