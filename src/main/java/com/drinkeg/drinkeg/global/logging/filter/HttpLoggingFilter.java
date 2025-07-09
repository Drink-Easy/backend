package com.drinkeg.drinkeg.global.logging.filter;

import com.drinkeg.drinkeg.global.aop.util.LoggingUtil;
import com.drinkeg.drinkeg.global.logging.dto.HttpRequestLogInfo;
import com.drinkeg.drinkeg.global.logging.dto.HttpResponseLogInfo;
import com.drinkeg.drinkeg.global.logging.filter.wrapper.RequestWrapper;
import com.drinkeg.drinkeg.global.logging.filter.wrapper.ResponseWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.UUID;

import static com.drinkeg.drinkeg.global.aop.util.LoggingUtil.*;
import static org.springframework.web.multipart.support.MultipartResolutionDelegate.isMultipartRequest;

@Slf4j
public class HttpLoggingFilter extends OncePerRequestFilter {

    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return true;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        setTraceId(UUID.randomUUID().toString());

        String uri = request.getRequestURI();
        if (uri.startsWith("/actuator/")) {
            filterChain.doFilter(request, response);
            MDC.clear();
            return;
        }

        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
        } else if (isMultipartRequest(request)) {
            filterChain.doFilter(request, response);
        } else {
            doFilterWrapped(
                    new RequestWrapper(request), new ResponseWrapper(response), filterChain);
        }
        clearMDC();
    }

    protected void doFilterWrapped(
            RequestWrapper request, ContentCachingResponseWrapper response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            logRequest(request);
            filterChain.doFilter(request, response);
        } finally {
            logResponse(response);
            response.copyBodyToResponse();
        }
    }

    private static void logRequest(RequestWrapper request) {
        HttpRequestLogInfo httpLogInfo = HttpRequestLogInfo.from(request);
        log.info(httpLogInfo.toString());
    }

    private static void logResponse(ContentCachingResponseWrapper response) throws IOException {
        HttpResponseLogInfo httpLogInfo = HttpResponseLogInfo.from(response);
        log.info(httpLogInfo.toString());
    }
}
