package org.example.listener;

import org.example.Log;
import org.example.session.Session;

public class LoggingSessionListener implements SessionListener {
    @Override
    public void sessionCreated(Session session) {
        Log.info("[Listener] 세션 생성됨: " + session.getId());
    }

    @Override
    public void sessionDestroyed(Session session) {
        Log.info("[Listener] 세션 삭제됨: " + session.getId());
    }
}
