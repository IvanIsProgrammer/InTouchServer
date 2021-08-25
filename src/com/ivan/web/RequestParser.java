package com.ivan.web;

import com.sun.net.httpserver.HttpExchange;
import org.json.simple.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RequestParser {
    public static String getRequestBody(HttpExchange exchange) {
        BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        int b;
        StringBuilder buf = new StringBuilder(1024);
        try {
            while ((b = br.read()) != -1) {
                buf.append((char) b);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buf.toString();
    }

    public static Object getObject(String request, String key) {
        JSONObject jsonObject = (JSONObject) JSONValue.parse(request);
        return jsonObject.get(key);
    }
}
