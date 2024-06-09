package com.sayanstr.httpserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Worker thread to handle HTTP connections.
 */
public class HttpConnectionWorkerThread extends Thread {

    private Socket socket;
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);

    public HttpConnectionWorkerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            String html = "<html><head><title>Simple Java HTTP Server</title></head><body><h1>Hello, World! I'm served from the simple Java HTTP server!</h1></body></html>";
            final String CRLF = "\r\n"; // CR: Carriage Return, LF: Line Feed
            String response = "HTTP/1.1 200 OK\r\n" + // Status line: HTTP_VERSION STATUS_CODE STATUS_MESSAGE
                    "Content-Length: " + html.length() + CRLF +
                    "Content-Type: text/html" + CRLF +
                    "Connection: close\r\n\r\n" + // 2 times CRLF: end of headers
                    html +
                    CRLF + CRLF; // 2 times CRLF: end of response
            outputStream.write(response.getBytes());

            inputStream.close();
            outputStream.close();
            socket.close();
            LOGGER.info(" * Connection processing finished *");
        } catch (IOException e) {
            LOGGER.error("Problem with communication", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                }
            }
            if(socket != null && !socket.isClosed())
                try {
                    socket.close();
                } catch (IOException e) {
                }
        }
    }
}
