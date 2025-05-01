package org.example;

public interface Servlet {
    // 서블릿 초기화 메서드
    default void init() {}

    // 서블릿 종료 메서드
    default void destroy() {}

    // GET 요청처리
    default void doGet(HttpRequest request, HttpResponse response) {
        response.setStatus(405);
        response.setBody("GET not supported");
    }

    // POST 요청처리
    default void doPost(HttpRequest request, HttpResponse response) {
        response.setStatus(405);
        response.setBody("POST not supported");
    }

    // DELETE 요청처리
    default void doDelete(HttpRequest request, HttpResponse response) {
        response.setStatus(405);
        response.setBody("DELETE not supported");
    }

    // HTTP 요청을 적절한 메서드로 분기
    default void service(HttpRequest request, HttpResponse response) {
        switch (request.getMethod()) {
            case "GET" -> doGet(request, response);
            case "POST" -> doPost(request, response);
            case "DELETE" -> doDelete(request, response);
            default -> {
                response.setStatus(405);
                response.setBody("Method Not Allowed: " + request.getMethod());
            }
        }
    }
}