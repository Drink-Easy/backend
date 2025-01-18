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
    private int pageSize;
    private int totalPages;
    private boolean hasNext;

    @Builder
    public PageResponse(List<T> content, int pageNumber, int pageSize, int totalPages, boolean hasNext) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.hasNext = hasNext;
    }

    public static PageResponse of(Page page) {
        return PageResponse.builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .hasNext(page.hasNext())
                .build();
    }
}
