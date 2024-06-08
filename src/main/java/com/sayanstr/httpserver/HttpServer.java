package com.sayanstr.httpserver;

import com.sayanstr.httpserver.config.Configuration;
import com.sayanstr.httpserver.config.ConfigurationManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Driver class for the HTTP server.
 */
public class HttpServer {
    public static void main(String[] args) {
        System.out.println("Server starting ...");
        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
        Configuration conf = ConfigurationManager.getInstance().getCurrentConfiguration();
        System.out.println("Server started on port " + conf.getPort() + " with webroot " + conf.getWebroot() + " ...");

        try {
            ServerSocket serverSocket = new ServerSocket(conf.getPort());
            Socket socket = serverSocket.accept();

            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            String html = "<html><head><title>Simple Java HTTP Server</title></head><body><h1>Hello, World! I'm served from the simple Java HTTP server!</h1></body></html>";
            final String CRLF = "\r\n"; // CR: Carriage Return, LF: Line Feed
            String response = "HTTP/1.1 200 OK\r\n" + // Status line: HTTP_VERSION STATUS_CODE STATUS_MESSAGE
                    "Content-Length: " + html.length() + CRLF +
                    "Content-Type: text/html" + CRLF +
                    "Connection: close\r\n\r\n" + // 2 times CRLF: end of headers
                    html +
                    CRLF + CRLF; // 2 times CRLF: end of response
            // TODO reading

            // TODO writing
            outputStream.write(response.getBytes());

            inputStream.close();
            outputStream.close();
            socket.close();
            serverSocket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}