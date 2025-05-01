package org.example;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String method;
    private String path;
    private Map<String, String> headers = new HashMap<>();

    public HttpRequest(InputStream inputStream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = reader.readLine(); // 첫 줄: GET /hello HTTP/1.1

            if (line != null && !line.isEmpty()) {
                String[] parts = line.split(" ");
                this.method = parts[0];
                this.path = parts[1];

                // 헤더 읽기
                while (!(line = reader.readLine()).isEmpty()) {
                    String[] header = line.split(": ");
                    if (header.length == 2) {
                        headers.put(header[0], header[1]);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse HTTP request", e);
        }
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
}