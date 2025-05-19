package com.cambofreelance.authenticationservice.configs.logger;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingFilter extends OncePerRequestFilter {
    private final AppLogger appLogger;

    private String getStringValue(byte[] contentAsByteArray, String characterEncoding) {
        try {
            return new String(contentAsByteArray, characterEncoding);
        } catch (Exception e) {
            appLogger.setParam("Error in LoggingFilter.getStringValue()", e.getMessage());
        }
        return new String(contentAsByteArray);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestBody, responseBody;
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        String uri = request.getRequestURI();
        List<String> unTrackUri = Arrays.asList("/actuator/health/readiness", "/actuator/health/liveness");
        requestBody = getStringValue(requestWrapper.getContentAsByteArray(), request.getCharacterEncoding());
        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } catch (Exception ex) {
            appLogger.setException(ex);
            throw ex;
        } finally {
            responseBody = getStringValue(responseWrapper.getContentAsByteArray(), response.getCharacterEncoding());
            appLogger.setUrl(request.getRequestURI());
            appLogger.setHttpMethod(request.getMethod());
            appLogger.setResponse(responseBody);
            appLogger.setRequest(requestBody);
            appLogger.setRemoteAddress(request.getRemoteAddr());
            if (unTrackUri.stream().noneMatch(uri::equals)) log.info(appLogger.toString());
            responseWrapper.copyBodyToResponse();
        }
    }
}
