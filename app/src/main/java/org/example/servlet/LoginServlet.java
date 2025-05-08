package org.example.servlet;

import org.example.HttpRequest;
import org.example.HttpResponse;
import org.example.Log;
import org.example.Servlet;
import org.example.model.UserDb;
import org.example.session.Session;
import org.example.session.SessionManager;

import java.util.Map;
import java.util.UUID;

public class LoginServlet implements Servlet {
    private SessionManager sessionManager;

    public LoginServlet() {}

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void doPost(HttpRequest req, HttpResponse resp) {
        Log.info("[Servlet] /login 요청 처리 시작");
        Map<String, String> params = req.getQueryParams();
        String name = params.get("name");
        String gender = params.get("gender");

        if (name == null || gender == null) {
            resp.setStatus(400);
            resp.setBody("name과 gender 파라미터가 필요합니다");
            return;
        }

        String cookieHeader = req.getHeaders().get("Cookie");
        String sessionId = SessionManager.getSessionIdFromCookie(cookieHeader);
        Session session = sessionId != null ? sessionManager.getSession(sessionId) : null;

        if (session == null) {
            session = sessionManager.createSession();
            sessionId = session.getId();
            resp.setHeader("Set-Cookie", SessionManager.getSetCookieHeader(sessionId));
            Log.info("[Servlet] 새로운 세션 생성: " + sessionId);
        } else{
            Log.info("[Servlet] 기존 세션 사용: " + sessionId);
        }

        String userId = UUID.randomUUID().toString(); // userId를 매번 새로 발급
        session.setAttribute("userId", userId);

        UserDb.getInstance().save(userId, name, gender);

        resp.setStatus(200);
        resp.setBody("로그인 성공! 유저 저장됨");
        Log.info("[Servlet] User 저장 완료: name=" + name + ", gender=" + gender);
    }
}