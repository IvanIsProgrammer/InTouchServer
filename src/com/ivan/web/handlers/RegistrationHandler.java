package com.ivan.web.handlers;

import com.ivan.logic.ServerController;
import com.ivan.web.RequestParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.OutputStream;

public record RegistrationHandler(ServerController controller) implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String request = RequestParser.getRequestBody(exchange);
        String name = (String) RequestParser.getObject(request, "login");
        String password = (String) RequestParser.getObject(request, "password");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", controller.registration(name, password));
        String response = jsonObject.toJSONString();

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
