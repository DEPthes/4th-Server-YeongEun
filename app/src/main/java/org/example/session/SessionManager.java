package org.example.session;

import org.example.listener.SessionListener;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static final String SESSION_COOKIE_NAME = "SESSIONID";
    private final Map<String, Session> sessionStore = new ConcurrentHashMap<>();
    private final List<SessionListener> listeners = new ArrayList<>();

    public void addListener(SessionListener listener) {
        listeners.add(listener);
    }

    public Session createSession() {
        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(sessionId);
        sessionStore.put(sessionId, session);

        // 🔥 리스너 호출
        for (SessionListener listener : listeners) {
            listener.sessionCreated(session);
        }

        return session;
    }

    public Session getSession(String sessionId) {
        return sessionStore.get(sessionId);
    }

    public void removeSession(String sessionId) {
        Session session = sessionStore.remove(sessionId);
        if (session != null) {
            for (SessionListener listener : listeners) {
                listener.sessionDestroyed(session);
            }
        }
    }

    public static String getSessionIdFromCookie(String cookieHeader) {
        if (cookieHeader == null) return null;
        for (String cookie : cookieHeader.split(";")) {
            String[] parts = cookie.trim().split("=");
            if (parts.length == 2 && parts[0].equals(SESSION_COOKIE_NAME)) {
                return parts[1];
            }
        }
        return null;
    }

    public static String getSetCookieHeader(String sessionId) {
        return SESSION_COOKIE_NAME + "=" + sessionId + "; Path=/; HttpOnly";
    }
}