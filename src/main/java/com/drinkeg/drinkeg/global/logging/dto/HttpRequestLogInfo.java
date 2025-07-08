package com.drinkeg.drinkeg.global.logging.dto;

import com.drinkeg.drinkeg.global.aop.util.LoggingUtil;
import com.drinkeg.drinkeg.global.logging.filter.wrapper.RequestWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.MDC;

import static com.drinkeg.drinkeg.global.aop.util.LoggingUtil.*;

public record HttpRequestLogInfo(
        String traceId,
        String requestMethod,
        String requestUri,
        String xAmznTraceId,
        String userAgent) {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static HttpRequestLogInfo from(RequestWrapper requestWrapper) {
        String queryString = requestWrapper.getQueryString();
        String traceId = getTraceId();
        String requestMethod = requestWrapper.getMethod();
        String requestUri =
                requestWrapper.getRequestURI() + (queryString == null ? "" : "?" + queryString);
        String xAmznTraceId = requestWrapper.getHeader("x-amzn-trace-id");
        String userAgent = requestWrapper.getHeader("user-agent");

        return new HttpRequestLogInfo(traceId, requestMethod, requestUri, xAmznTraceId, userAgent);
    }

    public String toString() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
