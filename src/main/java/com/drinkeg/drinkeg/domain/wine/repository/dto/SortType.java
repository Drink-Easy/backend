package com.drinkeg.drinkeg.domain.wine.repository.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SortType {
    LATEST("최신순"), OLDEST("오래된 순"), HIGH_RATING("별점 높은 순"), LOW_RATING("별점 낮은 순");
    private final String value;

    public static SortType of(String value) {
        for (SortType sortType : SortType.values()) {
            if (sortType.getValue().equals(value)) {
                return sortType;
            }
        }
        throw new IllegalArgumentException("유효하지 않은 정렬 타입입니다.");
    }
}
