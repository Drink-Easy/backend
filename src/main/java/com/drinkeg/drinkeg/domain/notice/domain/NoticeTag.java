package com.drinkeg.drinkeg.domain.notice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NoticeTag {
    NOTICE("공지사항"),
    EVENT("이벤트");

    private final String text;
}
