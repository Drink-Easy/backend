package com.drinkeg.drinkeg.domain.notice.controller.request;

import com.drinkeg.drinkeg.domain.notice.domain.Notice;
import com.drinkeg.drinkeg.domain.notice.domain.NoticeTag;
import com.drinkeg.drinkeg.domain.notice.service.request.NoticeServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NoticeRequest {
    @NotBlank(message = "제목은 필수입니다.")
    private String title;
    @NotBlank(message = "공지사항 태그는 필수입니다.")
    private String tag;
    @NotBlank(message = "공지사항 url은 필수입니다.")
    private String contentUrl;

    @Builder
    public NoticeRequest(String title, String tag, String contentUrl) {
        this.title = title;
        this.tag = tag;
        this.contentUrl = contentUrl;
    }

    public NoticeServiceRequest toServiceRequest() {
        return NoticeServiceRequest.builder()
                .title(title)
                .tag(tag)
                .contentUrl(contentUrl)
                .build();
    }
}
