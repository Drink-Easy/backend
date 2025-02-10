package com.drinkeg.drinkeg.global.security.jwt;

import com.drinkeg.drinkeg.global.apipayLoad.code.ReasonDTO;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;

import java.io.IOException;

public class JWTException {

    public static void jwtExceptionHandler(HttpServletResponse response, ErrorStatus errorStatus) {


        response.setStatus(errorStatus.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();

        ReasonDTO errorResponse = ReasonDTO.builder()
                .isSuccess(false)
                .httpStatus(HttpStatus.UNAUTHORIZED)
                .code(errorStatus.getCode())
                .message(errorStatus.getMessage())
                .build();

        try {
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);
            response.getWriter().write(jsonResponse);
        } catch (IOException e) {
            throw new AuthenticationServiceException("Failed to write authentication response body", e);
        }
    }
}
