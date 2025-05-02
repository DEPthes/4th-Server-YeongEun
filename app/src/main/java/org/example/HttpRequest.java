package org.example;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HttpRequest {
    private final String method;
    private final String path;
    private final Map<String, String> headers;
    private final String body;
    private final Map<String, String> queryParams;

    public HttpRequest(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line = reader.readLine(); // 예: "POST /login?name=aaa&gender=male HTTP/1.1"

        if (line == null || line.isEmpty()) {
            throw new IOException("Empty request line");
        }

        String[] requestLine = line.split(" ");
        this.method = requestLine[0];

        String fullPath = requestLine[1];
        this.headers = new HashMap<>();
        this.queryParams = new HashMap<>();

        // path 와 query string 분리
        if (fullPath.contains("?")) {
            String[] parts = fullPath.split("\\?", 2);
            this.path = parts[0];

            String query = parts[1];
            for (String pair : query.split("&")) {
                String[] kv = pair.split("=", 2);
                if (kv.length == 2) {
                    queryParams.put(
                            URLDecoder.decode(kv[0], StandardCharsets.UTF_8),
                            URLDecoder.decode(kv[1], StandardCharsets.UTF_8)
                    );
                }
            }
        } else {
            this.path = fullPath;
        }

        // 헤더 읽기
        while (!(line = reader.readLine()).isEmpty()) {
            String[] headerParts = line.split(": ", 2);
            if (headerParts.length == 2) {
                headers.put(headerParts[0], headerParts[1]);
            }
        }

        // 바디 읽기
        int contentLength = 0;
        if (headers.containsKey("Content-Length")) {
            contentLength = Integer.parseInt(headers.get("Content-Length"));
        }

        char[] bodyChars = new char[contentLength];
        reader.read(bodyChars);
        this.body = new String(bodyChars);
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }
}