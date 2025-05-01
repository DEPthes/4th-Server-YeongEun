package org.example;

import java.io.*;
import java.net.*;

public class HttpServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        Log.info("서버 시작: http://localhost:8080");

        // 서블릿 매핑
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.addServlet("/hello", new HelloServlet());

        // 서버 종료시
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Log.info("서버 종료 중 - 서블릿 destroy 호출");
            dispatcher.destroyAll();
        }));

        while (true) {
            try (Socket client = serverSocket.accept()) {
                InputStream in = client.getInputStream();
                OutputStream out = client.getOutputStream();

                HttpRequest request = new HttpRequest(in);
                HttpResponse response = new HttpResponse();

                Servlet servlet = dispatcher.getServlet(request.getPath());
                if (servlet != null) {
                    servlet.service(request, response);
                } else {
                    response.setStatus(404);
                    response.setBody("404 Not Found");
                }

                out.write(response.toByteArray());
                out.flush();
            } catch (Exception e) {
                Log.error("Error handling request: " + e.getMessage());
            }
        }
    }
}