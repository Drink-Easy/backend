package com.drinkeg.drinkeg.domain.notice.dto;

import com.drinkeg.drinkeg.domain.notice.domain.Notice;
import com.drinkeg.drinkeg.domain.notice.domain.NoticeTag;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NoticeRequest {
    private String title;
    private NoticeTag tag;
    private String contentUrl;

    @Builder
    public NoticeRequest(String title, NoticeTag tag, String contentUrl) {
        this.title = title;
        this.tag = tag;
        this.contentUrl = contentUrl;
    }

    public Notice toEntity() {
        return Notice.builder()
                .title(title)
                .tag(tag)
                .contentUrl(contentUrl)
                .build();
    }
}
