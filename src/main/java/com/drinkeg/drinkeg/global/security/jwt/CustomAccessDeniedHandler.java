package com.drinkeg.drinkeg.global.security.jwt;

import com.drinkeg.drinkeg.global.apipayLoad.code.ReasonDTO;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setStatus(ErrorStatus.FORBIDDEN.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();

        ReasonDTO errorResponse = ReasonDTO.builder()
                .isSuccess(false)
                .httpStatus(HttpStatus.FORBIDDEN)
                .code(ErrorStatus.FORBIDDEN.getCode())
                .message(ErrorStatus.FORBIDDEN.getMessage())
                .build();

        try {
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);
            response.getWriter().write(jsonResponse);
        } catch (IOException e) {
            throw new AuthenticationServiceException("Failed to write authentication response body", e);
        }
    }
}
