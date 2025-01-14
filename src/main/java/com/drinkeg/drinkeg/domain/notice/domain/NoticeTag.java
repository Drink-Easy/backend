package com.drinkeg.drinkeg.domain.notice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NoticeTag {
    NOTICE("공지사항"),
    EVENT("이벤트");

    private final String text;

    public static NoticeTag of(String text) {
        for (NoticeTag noticeTag : values()) {
            if (noticeTag.text.equals(text)) {
                return noticeTag;
            }
        }
        throw new IllegalArgumentException("존재하지 않는 공지사항 태그입니다.");
    }
}
