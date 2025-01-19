package com.drinkeg.drinkeg.domain.banner.domain;

import com.drinkeg.drinkeg.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Banner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl; // 배너 이미지 url
    private String postUrl; // 배너 관련 게시글 url

    // 배너 생성
    public static Banner create(String imageUrl, String postUrl) {
        return Banner.builder()
                .imageUrl(imageUrl)
                .postUrl(postUrl)
                .build();
    }
}
