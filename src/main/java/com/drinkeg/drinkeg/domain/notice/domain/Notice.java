package com.drinkeg.drinkeg.domain.notice.domain;

import com.drinkeg.drinkeg.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String contentUrl;

    @Enumerated(EnumType.STRING)
    private NoticeTag tag;

    @Builder
    public Notice(String title, String contentUrl, NoticeTag tag) {
        this.title = title;
        this.contentUrl = contentUrl;
        this.tag = tag;
    }

    public Notice update(String title, String contentUrl, NoticeTag tag) {
        this.title = title;
        this.contentUrl = contentUrl;
        this.tag = tag;
        return this;
    }

    public static Notice create(String title, String contentUrl, NoticeTag tag) {
        return Notice.builder()
                .title(title)
                .contentUrl(contentUrl)
                .tag(tag).build();
    }
}
