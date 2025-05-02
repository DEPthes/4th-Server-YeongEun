package org.example;

import org.example.filter.SessionLoggingFilter;
import org.example.listener.LoggingSessionListener;
import org.example.servlet.LoginServlet;
import org.example.servlet.UserServlet;
import org.example.session.SessionManager;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class HttpServer {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(10); // 최대 10개 동시 처리
        Dispatcher dispatcher = new Dispatcher();

        SessionManager sessionManager = new SessionManager();
        sessionManager.addListener(new LoggingSessionListener());

        dispatcher.addFilter(new SessionLoggingFilter());
        dispatcher.addServlet("/hello", new HelloServlet());
        dispatcher.addServlet("/login", new LoginServlet(sessionManager));
        dispatcher.addServlet("/user", new UserServlet(sessionManager));


        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Log.info("Shutdown Hook 실행됨! destroyAll() 호출");
            dispatcher.destroyAll();
            threadPool.shutdown();
        }));

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            Log.info("서버 시작: http://localhost:8080");

            while (true) {
                try {
                    Socket client = serverSocket.accept();
                    threadPool.submit(() -> handleClient(client, dispatcher));
                } catch (IOException e) {
                    Log.error("클라이언트 연결 실패: " + e.getMessage());
                }
            }

        } catch (IOException e) {
            Log.error("서버 시작 실패: " + e.getMessage());
        }
    }

    private static void handleClient(Socket client, Dispatcher dispatcher) {
        try (client) {
            InputStream in = client.getInputStream();
            OutputStream out = client.getOutputStream();

            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse();

            dispatcher.service(request, response);

            out.write(response.toByteArray());
            out.flush();
        } catch (Exception e) {
            Log.error("요청 처리 중 예외: " + e.getMessage());
        }
    }
}