package com.ivan.web.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class InfoHandler  implements HttpHandler {

    private final String routePath;
    private final String fsPath;

    private static final String ROOT = "index.html";

    private final Map<String, String> headers = new HashMap<>() {{
        put("html", "text/html");
        put("css", "text/css");
        put("js", "text/javascript");
        put("json", "application/json");
        put("svg", "image/svg+xml");
    }};

    private final Map<String, String> sourcePaths = new HashMap<>() {{
        put("html", "");
        put("css", "/css");
        put("js", "/js");
        put("json", "/json");
        put("svg", "/svg");
    }};

    public InfoHandler(String path, String filesystemPath) {
        routePath = path;
        fsPath = filesystemPath;
    }

    @Override
    public void handle(HttpExchange http) throws IOException {

        OutputStream outputStream = http.getResponseBody();
        String request = http.getRequestURI().getRawPath();
        byte[] result;
        int code;
        try {
            try {
                String filename = request.substring(request.lastIndexOf("/"));
                String ext = request.substring(request.lastIndexOf(".") + 1);
                String path = fsPath + routePath + sourcePaths.get(ext) + filename;
                System.out.println(path);
                result = read(new FileInputStream(path)).toByteArray();
                if (headers.containsKey(ext))
                    http.getResponseHeaders().add("Content-Type", headers.get(ext));
                code = 200;
            } catch (IOException e) {
                result = (404 + " " + request).getBytes();
                code = 302;
                http.getResponseHeaders().add("Location",http.getRequestURI() + "/index.html" );

            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            result = sw.getBuffer().toString().getBytes();
            code = 500;
        }

        http.sendResponseHeaders(code, result.length);
        outputStream.write(result);
        outputStream.close();
    }

    static ByteArrayOutputStream read(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer;
    }

}