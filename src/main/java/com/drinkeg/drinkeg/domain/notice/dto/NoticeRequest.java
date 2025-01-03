package com.drinkeg.drinkeg.domain.notice.dto;

import com.drinkeg.drinkeg.domain.notice.domain.Notice;
import com.drinkeg.drinkeg.domain.notice.domain.NoticeTag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import static io.lettuce.core.pubsub.PubSubOutput.Type.message;

@Getter
public class NoticeRequest {
    @NotBlank(message = "제목은 필수입니다.")
    private String title;
    @NotNull(message = "공지사항 태그는 필수입니다.")
    private NoticeTag tag;
    @NotBlank(message = "공지사항 url은 필수입니다.")
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
