package org.example;

import java.util.HashMap;
import java.util.Map;

public class Dispatcher {
    private final Map<String, Servlet> servletMap = new HashMap<>();

    public void addServlet(String path, Servlet servlet) {
        servlet.init();
        servletMap.put(path, servlet);
    }

    public Servlet getServlet(String path) {
        return servletMap.get(path);
    }

    public void destroyAll() {
        servletMap.values().forEach(Servlet::destroy);
    }
}