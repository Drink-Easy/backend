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
    INVALID_PARTY_REQUEST_LIMITMEMBERNUM(HttpStatus.BAD_REQUEST, "PARTY4003", "현재 참가자보다 적은 제한인원을 설정할 수 없습니다."),
    NOT_YOUR_PARTY(HttpStatus.BAD_REQUEST, "PARTY4004", "모임 개설자가 아닙니다."),
    PARTY_FULL(HttpStatus.BAD_REQUEST, "PARTY4006", "모임 인원이 가득 찼습니다."),
    INVALID_SORT_TYPE(HttpStatus.BAD_REQUEST, "PARTY4007", "정렬방식이 유효하지 않습니다."),

    // Comment Error
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT4001", "댓글이 없습니다."),
    COMMENT_HAS_RECOMMENTS(HttpStatus.BAD_REQUEST, "COMMENT4002", "대댓글이 있는 댓글은 완전 삭제할 수 없습니다."),
    COMMENT_HAS_NO_RECOMMENTS(HttpStatus.BAD_REQUEST, "COMMENT4003", "대댓글이 없는 댓글입니다."),


    RECOMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "RECOMMENT4001", "대댓글을 찾을 수 없습니다."),
    NOT_YOUR_COMMENT(HttpStatus.BAD_REQUEST,"COMMENT4004", "댓글 작성자가 아닙니다."), // 대댓글의 경우에도 사용

    // PartyJoinMember Error
    NOT_FOUND_PARTY_JOIN(HttpStatus.BAD_REQUEST, "JOIN4001", "모임에 참가한 기록이 없습니다."),
    EXIST_IN_PARTY(HttpStatus.BAD_REQUEST, "JOIN4002", "이미 참가한 모임입니다."),
    HOST_CANNOT_LEAVE(HttpStatus.BAD_REQUEST,"JOIN4003", "호스트는 모임을 탈퇴할 수 없습니다."),



    // Wine class Error
    WINE_CLASS_NOT_FOUND(HttpStatus.BAD_REQUEST, "WINE_CLASS4001", "와인 클래스가 없습니다."),
    WINE_CLASS_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "WINE_CLASS4002", "권한이 없는 와인클래스입니다."),

    //WineClassBookMark Error
    WINE_CLASS_BOOKMARK_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "WINE_CLASS_BOOKMARK4001", "권한이 없는 북마크입니다."),
    WINE_CLASS_BOOKMARK_NOT_FOUND(HttpStatus.BAD_REQUEST, "WINE_CLASS_BOOKMARK4001", "와인 클래스 북마크가 없습니다."),
    WINE_CLASS_BOOKMARK_DUPLICATED(HttpStatus.BAD_REQUEST, "WINE_CLASS_BOOKMARK4002", "이미 존재하는 북마크입니다."),


    // Member Error
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "회원이 없습니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "MEMBER4001", "로그인 과정에서 오류가 발생했습니다."),
    SESSION_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "MEMBER4001", "유효하지 않은 세션입니다."),
    AUTH_REQUEST_BODY_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "Authentication Request Body를 읽지 못했습니다."),
    USERNAME_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "유저 아이디가 없습니다."),
    PASSWORD_NOT_FUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "유저 비밀번호가 없습니다."),
    MEMBER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4001", "이미 존재하는 아이디입니다."),

    // Token Error
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "ACCESS_TOKEN4001", "유효하지 않은 엑세스 토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "REFRESH_TOKEN4001", "리프레쉬 토큰이 없습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "REFRESH_TOKEN4001", "리프레쉬 토큰이 만료되었습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "REFRESH_TOKEN4001", "유효하지 않은 리프레쉬 토큰입니다."),

    // Redis Error
    REDIS_NOT_FOUND(HttpStatus.BAD_REQUEST, "REDIS4001", "Redis 설정에 오류가 발생했습니다."),

    // appleLogin Error
    MATCH_PUBLIC_KEY_NOR_FOUND(HttpStatus.BAD_REQUEST, "APPLE4001", "일치하는 공개키를 찾을 수 없습니다"),
    IDENTITY_TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "APPLE4002", "아이덴티티 토큰을 찾을 수 없습니다."),

    // WineWishlist Error
    WINE_WISHLIST_NOT_FOUND(HttpStatus.BAD_REQUEST, "WINE_WISHLIST4001", "와인 위시리스트가 없습니다."),
    WINE_WISHLIST_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "WINE_WISHLIST4002", "권한이 없는 위시리스트입니다."),
    WINE_WISHLIST_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "WINE_WISHLIST4003", "이미 존재하는 위시리스트입니다."),
  
    // WineLecture Error
    WINE_LECTURE_NOT_FOUND(HttpStatus.BAD_REQUEST, "WINE_LECTURE4001", "와인 강의가 없습니다."),
    WINE_LECTURE_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "WINE_LECTURE4002", "권한이 없는 강의입니다.");



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