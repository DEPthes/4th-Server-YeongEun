package org.example;


public class HelloServlet implements Servlet {
    @Override
    public void init() {
        Log.info("HelloServlet 초기화 완료");
    }

    @Override
    public void doGet(HttpRequest req, HttpResponse resp) {
        resp.setStatus(200);
        resp.setBody("Hello from doGet()");
    }

    @Override
    public void destroy() {
        Log.info("HelloServlet 종료됨");
    }
}