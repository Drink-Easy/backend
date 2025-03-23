package com.drinkeg.drinkeg.domain.wine.dto.response;

import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminWineResponse {

    private AdminWinePreviewResponse adminWinePreviewResponse;
    private AdminWineDetailResponse adminWineDetailResponse;


    @Builder
    public AdminWineResponse(AdminWinePreviewResponse adminWinePreviewResponse, AdminWineDetailResponse adminWineDetailResponse) {
        this.adminWinePreviewResponse = adminWinePreviewResponse;
        this.adminWineDetailResponse = adminWineDetailResponse;

    }

    public static AdminWineResponse of(Wine wine) {
        return AdminWineResponse.builder()
                .adminWinePreviewResponse(AdminWinePreviewResponse.of(wine))
                .adminWineDetailResponse(AdminWineDetailResponse.of(wine))
                .build();


    }

}
