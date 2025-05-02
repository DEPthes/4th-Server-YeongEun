package org.example.filter;


import org.example.HttpRequest;
import org.example.HttpResponse;

public interface Filter {
    void doFilter(HttpRequest request, HttpResponse response, FilterChain chain);
}