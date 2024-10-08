package com.drinkeg.drinkeg.apipayLoad.code.status;

import com.drinkeg.drinkeg.apipayLoad.code.BaseCode;
import com.drinkeg.drinkeg.apipayLoad.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    // 성공 응답
    OK(HttpStatus.OK, "COMMON200", "OK");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .isSuccess(true)
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .build();
    }
}