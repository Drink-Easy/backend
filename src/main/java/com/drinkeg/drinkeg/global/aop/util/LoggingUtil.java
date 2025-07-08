package com.drinkeg.drinkeg.global.aop.util;

import org.slf4j.MDC;
import java.lang.reflect.Method;

public class LoggingUtil {

    public static final String TRACE_ID = "traceId";
    public static final String MEMBER_ID = "memberId";

    public static void setTraceId(String traceId) {
        if (isNotEmpty(traceId)) MDC.put(TRACE_ID, traceId);
    }

    public static String getTraceId() {
        return MDC.get(TRACE_ID);
    }

    public static void setMemberId(String memberId) {
        if (isNotEmpty(memberId)) MDC.put(MEMBER_ID, memberId);
    }

    public static String getMemberId() {
        return MDC.get(MEMBER_ID);
    }

    public static void clearMDC() {
        MDC.clear();
    }

    private static boolean isNotEmpty(String value) {
        return value != null && !value.isEmpty();
    }

    public static String getMethodSignature(Method method) {
        return method.getDeclaringClass().getSimpleName() + "." + method.getName();
    }

    public static long calculateDuration(long startTime) {
        return System.currentTimeMillis() - startTime;
    }

    public static String getShortErrorMessage(String errorMessage) {
        return errorMessage != null && errorMessage.contains(":")
                ? errorMessage.substring(errorMessage.lastIndexOf(":") + 1).trim()
                : errorMessage;
    }
}