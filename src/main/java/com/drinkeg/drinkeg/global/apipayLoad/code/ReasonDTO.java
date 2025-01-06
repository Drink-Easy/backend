package com.drinkeg.drinkeg.global.apipayLoad.code;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ReasonDTO {
    private final HttpStatus httpStatus;
    private final boolean isSuccess;
    private final String code;
    private final String message;

    @Builder
    public ReasonDTO(HttpStatus httpStatus, boolean isSuccess, String code, String message) {
        this.httpStatus = httpStatus;
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
