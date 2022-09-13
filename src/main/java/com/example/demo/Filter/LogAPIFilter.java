package com.example.demo.Filter;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogAPIFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(requestWrapper, responseWrapper);

        LogAPI(request, response);
        LogBody(requestWrapper, responseWrapper);

        responseWrapper.copyBodyToResponse();
    }

    private void LogAPI(HttpServletRequest request, HttpServletResponse response) {

        int httpStatus = response.getStatus();
        String httpMethod = request.getMethod();
        String uri = request.getRequestURI();
        String params = request.getQueryString();

        if (params != null) {
            uri += "?" + params;
        }

        System.out.println(String.join(" ", String.valueOf(httpStatus), httpMethod, uri));
    }

    private void LogBody(ContentCachingRequestWrapper requestWrapper, ContentCachingResponseWrapper responseWrapper) {
        String requsetBody = getContent(requestWrapper.getContentAsByteArray());
        System.out.println("Request: " + requsetBody);

        String responseBody = getContent(responseWrapper.getContentAsByteArray());
        System.out.println("Response: " + responseBody);
    }

    private String getContent(byte[] content) {
        String body = new String(content);
        return body;
    }
}
