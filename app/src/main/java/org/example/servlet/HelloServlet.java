package org.example.servlet;


import org.example.HttpRequest;
import org.example.HttpResponse;
import org.example.Log;
import org.example.Servlet;

public class HelloServlet implements Servlet {
    private Log log;

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