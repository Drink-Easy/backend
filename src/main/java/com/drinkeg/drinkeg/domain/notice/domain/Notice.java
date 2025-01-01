package com.drinkeg.drinkeg.domain.notice.domain;

import com.drinkeg.drinkeg.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String contentUrl;

    @Enumerated(EnumType.STRING)
    private NoticeTag tag;
}
