package com.drinkeg.drinkeg.domain.notice.service.request;

import com.drinkeg.drinkeg.domain.notice.domain.Notice;
import com.drinkeg.drinkeg.domain.notice.domain.NoticeTag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NoticeServiceRequest {
    private String title;
    private String tag;
    private String contentUrl;

    @Builder
    public NoticeServiceRequest(String title, String tag, String contentUrl) {
        this.title = title;
        this.tag = tag;
        this.contentUrl = contentUrl;
    }

    public Notice toEntity() {
        return Notice.builder()
                .title(title)
                .tag(NoticeTag.of(tag))
                .contentUrl(contentUrl)
                .build();
    }
}
