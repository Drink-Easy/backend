package com.drinkeg.drinkeg.domain.wine.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PageResponse<T> {

    private List<T> content;
    private int pageNumber;
    private int totalPages;

    @Builder
    public PageResponse(List<T> content, int pageNumber, int totalPages) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.totalPages = totalPages;
    }

    public static PageResponse of(Page page) {
        return PageResponse.builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .totalPages(page.getTotalPages())
                .build();
    }
}
