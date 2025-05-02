package org.example.filter;

import org.example.HttpRequest;
import org.example.HttpResponse;
import org.example.Log;
import org.example.session.SessionManager;

public class SessionLoggingFilter implements Filter {
    @Override
    public void doFilter(HttpRequest req, HttpResponse res, FilterChain chain) {
        Log.info("[Filter] SessionLoggingFilter 실행됨");
        String cookieHeader = req.getHeaders().get("Cookie");
        String sessionId = SessionManager.getSessionIdFromCookie(cookieHeader);

        if (sessionId != null) {
            Log.info("[Filter] 세션 있음: " + sessionId);
        } else {
            Log.info("[Filter] 세션 없음");
        }

        chain.doFilter(req, res); // 다음 필터 or 서블릿으로 진행
    }
}