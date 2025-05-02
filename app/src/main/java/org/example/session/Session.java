package org.example.session;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class Session {
    private final String id;
    private final Map<String, Object> data = new HashMap<>();
    private final Instant createdAt = Instant.now();

    public Session(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setAttribute(String key, Object value) {
        data.put(key, value);
    }

    public Object getAttribute(String key) {
        return data.get(key);
    }

    public void removeAttribute(String key) {
        data.remove(key);
    }

    public void invalidate() {
        data.clear();
    }
}