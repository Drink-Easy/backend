package com.drinkeg.drinkeg.apipayLoad.code.status;

import com.drinkeg.drinkeg.apipayLoad.code.BaseCode;
import com.drinkeg.drinkeg.apipayLoad.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseCode {

    // Wine Error
    WINE_NOT_FOUND(HttpStatus.BAD_REQUEST, "WINE4001", "와인이 없습니다."),

    // Note Error
    TASTING_NOTE_NOT_FOUND(HttpStatus.BAD_REQUEST, "NOTE4001", "테이스팅 노트가 없습니다."),
    NOT_YOUR_NOTE(HttpStatus.BAD_REQUEST, "NOTE4002", "본인의 노트가 아닙니다."),

    // WineNote Error
    WINE_NOTE_NOT_FOUND(HttpStatus.BAD_REQUEST, "WINE_NOTE4001", "와인 노트가 없습니다."),
    NOT_INVALID_SCENT(HttpStatus.BAD_REQUEST, "WINE_NOTE4001", "해당 이름의 향이 없습니다."),


    // Party Error
    PARTY_NOT_FOUND(HttpStatus.NOT_FOUND, "PARTY4001", "모임이 없습니다."),
    INVALID_PARTY_REQUEST(HttpStatus.BAD_REQUEST, "PARTY4002", "모든 모임 정보가 입력되지 않았습니다."),
    NOT_YOUR_PARTY(HttpStatus.BAD_REQUEST, "PARTY4003", "모임 개설자가 아닙니다."),

    // Comment Error
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT4001", "댓글이 없습니다."),
    COMMENT_HAS_RECOMMENTS(HttpStatus.BAD_REQUEST, "COMMENT4002", "대댓글이 있는 댓글은 삭제할 수 없습니다."),
    COMMENT_HAS_NO_RECOMMENTS(HttpStatus.BAD_REQUEST, "COMMENT4003", "대댓글이 없는 댓글입니다."),
    RECOMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "RECOMMENT4001", "대댓글을 찾을 수 없습니다."),

    // Wine class Error
    WINE_CLASS_NOT_FOUND(HttpStatus.BAD_REQUEST, "WINECLASS4001", "와인 클래스가 없습니다."),
    // WineNews Error
    WINENEWS_NOT_FOUND(HttpStatus.BAD_REQUEST, "WINENEWS4001", "와인 뉴스가 없습니다."),

    //WineClassBookMark Error
    WINE_CLASS_BOOKMARK_NOT_FOUND(HttpStatus.BAD_REQUEST, "WINE_CLASS_BOOKMARK4001", "와인 클래스 북마크가 없습니다."),
    WINE_CLASS_BOOKMARK_DUPLICATED(HttpStatus.BAD_REQUEST, "WINE_CLASS_BOOKMARK4002", "이미 존재하는 북마크입니다."),

    // WineNewsBookMark Error
    WINE_NEWS_BOOKMARK_NOT_FOUND(HttpStatus.BAD_REQUEST, "WINENWSBOOKMARK4001", "와인 뉴스 북마크가 없습니다."),
    WINE_NEWS_BOOKMARK_DUPLICATED(HttpStatus.BAD_REQUEST, "WINENWSBOOKMARK4002", "이미 존재하는 북마크입니다."),


    // Member Error
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "회원이 없습니다."),
    SESSION_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "MEMBER4001", "유효하지 않은 세션입니다."),
    MEMBER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4001", "이미 존재하는 아이디입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "REFRESH_TOKEN4001", "리프레쉬 토큰이 없습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "REFRESH_TOKEN4001", "리프레쉬 토큰이 만료되었습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "REFRESH_TOKEN4001", "유효하지 않은 리프레쉬 토큰입니다."),

    // Redis Error
    REDIS_NOT_FOUND(HttpStatus.BAD_REQUEST, "REDIS4001", "Redis 설정에 오류가 발생했습니다.");


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
