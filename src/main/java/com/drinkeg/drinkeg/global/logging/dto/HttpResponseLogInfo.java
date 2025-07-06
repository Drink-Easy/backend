package com.drinkeg.drinkeg.global.logging.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public record HttpResponseLogInfo(String traceId, Object responseBody, Integer responseStatus) {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static HttpResponseLogInfo from(ContentCachingResponseWrapper response)
            throws IOException {
        String traceId = MDC.get("traceId");
        String rawBody = getContent(response.getContentType(), response.getContentInputStream());
        Integer responseStatus = response.getStatus();

        Object parsedBody;
        try {
            parsedBody = objectMapper.readValue(rawBody, Object.class);
        } catch (Exception e) {
            parsedBody = rawBody;
        }

        return new HttpResponseLogInfo(traceId, parsedBody, responseStatus);
    }

    private static String getContent(String contentType, InputStream inputStream)
            throws IOException {
        boolean visible =
                isVisible(
                        MediaType.valueOf(contentType == null ? "application/json" : contentType));
        if (visible) {
            byte[] content = StreamUtils.copyToByteArray(inputStream);
            if (content.length > 0) {
                return new String(content, 0, Math.min(content.length, 5120));
            } else {
                return "";
            }
        } else {
            return "BINARY";
        }
    }

    private static boolean isVisible(MediaType mediaType) {
        final List<MediaType> VISIBLE_TYPES =
                Arrays.asList(
                        MediaType.valueOf("text/*"),
                        MediaType.APPLICATION_FORM_URLENCODED,
                        MediaType.APPLICATION_JSON,
                        MediaType.APPLICATION_XML,
                        MediaType.valueOf("application/*+json"),
                        MediaType.valueOf("application/*+xml"),
                        MediaType.MULTIPART_FORM_DATA);

        return VISIBLE_TYPES.stream().anyMatch(visibleType -> visibleType.includes(mediaType));
    }

    public String toString() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
