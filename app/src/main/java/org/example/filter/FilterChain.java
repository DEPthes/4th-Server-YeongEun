package org.example.filter;

import org.example.HttpRequest;
import org.example.HttpResponse;
import org.example.Servlet;

import java.util.List;

public class FilterChain {
    private final List<Filter> filters;
    private final Servlet targetServlet;
    private int currentPosition = 0;

    public FilterChain(List<Filter> filters, Servlet servlet) {
        this.filters = filters;
        this.targetServlet = servlet;
    }

    public void doFilter(HttpRequest req, HttpResponse res) {
        if (currentPosition < filters.size()) {
            filters.get(currentPosition++).doFilter(req, res, this);
        } else {
            targetServlet.service(req, res);
        }
    }
}