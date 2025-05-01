package org.example;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private int status = 200;
    private Map<String, String> headers = new HashMap<>();
    private String body = "";

    public void setStatus(int status) {
        this.status = status;
    }

    public void setHeader(String key, String value) {
        headers.put(key, value);
    }

    public void setBody(String body) {
        this.body = body;
    }

    public byte[] toByteArray() {
        StringBuilder response = new StringBuilder();

        response.append("HTTP/1.1 ").append(status).append(" ").append(getStatusMessage(status)).append("\r\n");
        headers.put("Content-Length", String.valueOf(body.getBytes(StandardCharsets.UTF_8).length));
        headers.putIfAbsent("Content-Type", "text/plain; charset=utf-8");

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            response.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
        }

        response.append("\r\n");
        response.append(body);

        return response.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String getStatusMessage(int status) {
        return switch (status) {
            case 200 -> "OK";
            case 404 -> "Not Found";
            default -> "Unknown";
        };
    }
}