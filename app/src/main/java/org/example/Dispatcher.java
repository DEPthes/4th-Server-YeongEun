package org.example;

import org.example.filter.Filter;
import org.example.filter.FilterChain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dispatcher {
    private final Map<String, Servlet> servletMap = new HashMap<>();
    private final List<Filter> filters = new java.util.ArrayList<>();

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

    public void addFilter(Filter filter) {
        filters.add(filter);
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void service(HttpRequest req, HttpResponse res) {
        Servlet servlet = getServlet(req.getPath());
        if (servlet == null) {
            res.setStatus(404);
            res.setBody("404 Not Found");
            return;
        }

        FilterChain chain = new FilterChain(filters, servlet);
        chain.doFilter(req, res); // 필터 실행 → 마지막에 서블릿 호출
    }
}