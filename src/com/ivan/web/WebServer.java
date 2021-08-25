package com.ivan.web;

import com.ivan.logic.ServerController;
import com.ivan.web.handlers.*;
import com.sun.net.httpserver.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;

public class WebServer {
    public static final Properties props = new Properties();

    public static void main(String[] args) throws IOException {
        FileInputStream in = new FileInputStream("res/properties.configuration");
        props.load(in);
        in.close();

        final ServerController controller = ServerController.getInstance();
        HttpServer server = HttpServer.create(
                new InetSocketAddress(Integer.parseInt(props.getProperty("server.port"))),
                Integer.parseInt(props.getProperty("server.backlog"))
        );

        server.createContext("/api/info",
                new InfoHandler(props.getProperty("server.path"), props.getProperty("server.filesystemPath")));
        server.createContext("/api/authorization", new AuthorizationHandler(controller));
        server.createContext("/api/registration", new RegistrationHandler(controller));
        server.createContext("/api/send_message", new SendMessageHandler(controller));
        server.createContext("/api/get_message", new GetMessageHandler(controller));
        server.createContext("/api/get_previous_message", new GetPreviousMessageHandler(controller));
        server.createContext("/api/get_next_message", new GetNextMessageHandler(controller));
        server.createContext("/api/get_last_message", new GetLastMessageHandler(controller));

        server.start();
    }
}