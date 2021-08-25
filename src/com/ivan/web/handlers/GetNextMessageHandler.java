package com.ivan.web.handlers;

import com.ivan.db.Message;
import com.ivan.logic.ServerController;
import com.ivan.web.RequestParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.OutputStream;

public record GetNextMessageHandler(ServerController controller) implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String request = RequestParser.getRequestBody(exchange);
        String token = (String) RequestParser.getObject(request, "token");
        int currentId = Integer.parseInt((String)RequestParser.getObject(request, "current_id"));

        JSONObject jsonObject = new JSONObject();
        Message message = controller.getNextMessage(token, currentId);
        if (message == null ) {
            jsonObject.put("status", "failed");
        } else {
            jsonObject.put("status", "successful");
            jsonObject.put("id", message.id);
            jsonObject.put("sender", message.sender);
            jsonObject.put("content", message.content);
        }

        String response = jsonObject.toJSONString();

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
