package org.example.model;

import java.util.HashMap;
import java.util.Map;

public class UserDb {
    private static final UserDb instance = new UserDb(); // ✅ 유일한 인스턴스

    private final Map<String, Map<String, Object>> userDb = new HashMap<>();

    private UserDb() {
    }

    public static UserDb getInstance() {
        return instance;
    }

    public void save(String userId, String name, String gender) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", name);
        userInfo.put("gender", gender);
        userDb.put(userId, userInfo);
    }

    public Map<String, Object> find(String userId) {
        return userDb.get(userId);
    }

    public void delete(String userId) {
        userDb.remove(userId);
    }

    public boolean exists(String userId) {
        return userDb.containsKey(userId);
    }
}

