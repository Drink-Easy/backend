package com.drinkeg.drinkeg.domain.notice.service.response;

import com.drinkeg.drinkeg.domain.notice.domain.Notice;
import com.drinkeg.drinkeg.domain.notice.domain.NoticeTag;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class NoticeResponse {
    private Long id;
    private String title;
    private NoticeTag tag;
    private String contentUrl;
    private LocalDate createdAt;

    @Builder
    public NoticeResponse(Long id, String title, NoticeTag tag, String contentUrl, LocalDate createdAt) {
        this.id = id;
        this.title = title;
        this.tag = tag;
        this.contentUrl = contentUrl;
        this.createdAt = createdAt;
    }

    public static NoticeResponse of(Notice notice) {
        return NoticeResponse.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .tag(notice.getTag())
                .contentUrl(notice.getContentUrl())
                .createdAt(LocalDate.from(notice.getCreatedAt()))
                .build();
    }
}
