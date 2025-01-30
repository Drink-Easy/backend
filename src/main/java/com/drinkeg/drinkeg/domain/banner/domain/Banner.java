package com.drinkeg.drinkeg.domain.banner.domain;

import com.drinkeg.drinkeg.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Banner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl; // 배너 이미지 url
    private String postUrl; // 배너 관련 게시글 url

    @Builder
    public Banner(String imageUrl, String postUrl) {
        this.imageUrl = imageUrl;
        this.postUrl = postUrl;
    }

    // 배너 생성
    public static Banner create(String imageUrl, String postUrl) {
        return Banner.builder()
                .imageUrl(imageUrl)
                .postUrl(postUrl)
                .build();
    }

    // 배너 업데이트
    public void update(String imageUrl, String postUrl) {
        if (imageUrl != null) this.imageUrl = imageUrl;
        if (postUrl != null) this.postUrl = postUrl;
    }
}
