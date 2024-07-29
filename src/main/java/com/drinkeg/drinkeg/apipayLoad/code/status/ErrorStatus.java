package com.drinkeg.drinkeg.apipayLoad.code.status;

import com.drinkeg.drinkeg.apipayLoad.code.BaseCode;
import com.drinkeg.drinkeg.apipayLoad.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseCode {

    // Member Error
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "회원이 없습니다."),

    // Wine Error
    WINE_NOT_FOUND(HttpStatus.BAD_REQUEST, "WINE4001", "와인이 없습니다."),

    // Note Error
    TASTING_NOTE_NOT_FOUND(HttpStatus.BAD_REQUEST, "NOTE4001", "테이스팅 노트가 없습니다."),
    INVALID_SUGAR_CONTENT(HttpStatus.BAD_REQUEST, "NOTE4001", "당도가 범위 밖입니다."),
    INVALID_ACIDITY(HttpStatus.BAD_REQUEST, "NOTE4001", "산도가 범위 밖입니다."),
    INVALID_TANNIN(HttpStatus.BAD_REQUEST, "NOTE4001", "타닌이 범위 밖입니다."),
    INVALID_BODY(HttpStatus.BAD_REQUEST, "NOTE4001", "바디가 범위 밖입니다."),
    INVALID_ALCOHOL(HttpStatus.BAD_REQUEST, "NOTE4001", "알코올이 범위 밖입니다."),

    // WineNote Error
    WINE_NOTE_NOT_FOUND(HttpStatus.BAD_REQUEST, "WINE_NOTE4001", "와인 노트가 없습니다."),
    NOT_INVALID_SCENT(HttpStatus.BAD_REQUEST, "WINE_NOTE4001", "해당 이름의 향이 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .isSuccess(false)
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .build();
    }
}
