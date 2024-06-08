package com.sayanstr.httpserver;

import com.sayanstr.httpserver.config.Configuration;
import com.sayanstr.httpserver.config.ConfigurationManager;

/**
 * Driver class for the HTTP server.
 */
public class HttpServer {
    public static void main(String[] args) {
        System.out.println("Server starting ...");
        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
        Configuration conf = ConfigurationManager.getInstance().getCurrentConfiguration();
        System.out.println("Server started on port " + conf.getPort() + " with webroot " + conf.getWebroot() + " ...");
    }
}