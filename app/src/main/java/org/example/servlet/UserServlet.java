package org.example.servlet;

import org.example.HttpRequest;
import org.example.HttpResponse;
import org.example.Servlet;
import org.example.session.Session;
import org.example.session.SessionManager;
import org.example.model.UserDb;

import java.util.Map;

public class UserServlet implements Servlet {
    private final SessionManager sessionManager;

    public UserServlet(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void doGet(HttpRequest req, HttpResponse resp) {
        String cookieHeader = req.getHeaders().get("Cookie");
        String sessionId = SessionManager.getSessionIdFromCookie(cookieHeader);

        if (sessionId == null) {
            resp.setStatus(401);
            resp.setBody("세션이 없습니다");
            return;
        }

        Session session = sessionManager.getSession(sessionId);
        if (session == null) {
            resp.setStatus(401);
            resp.setBody("유효하지 않은 세션입니다");
            return;
        }

        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            resp.setStatus(404);
            resp.setBody("세션에 유저 ID가 없습니다");
            return;
        }

        Map<String, Object> user = UserDb.getInstance().find(userId);
        if (user == null) {
            resp.setStatus(404);
            resp.setBody("유저 정보를 찾을 수 없습니다");
            return;
        }

        resp.setStatus(200);
        resp.setBody("유저: " + user.get("name") + " (" + user.get("gender") + ")");
    }
}