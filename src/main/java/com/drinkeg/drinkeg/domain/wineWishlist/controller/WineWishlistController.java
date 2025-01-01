package com.drinkeg.drinkeg.domain.wineWishlist.controller;


import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.domain.wine.dto.response.WinePreviewResponse;
import com.drinkeg.drinkeg.domain.wineWishlist.service.WineWishlistService;
import com.drinkeg.drinkeg.global.apipayLoad.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "Wine Wishlist", description = "와인 위시리스트 CRUD API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/wine-wishlist")
public class WineWishlistController {
    private final WineWishlistService wineWishlistService;

    @Operation(summary = "와인 위시리스트에 추가", description = "wineId로 와인 위시리스트에 추가한다.")
    @PostMapping("/{wineId}")
    public ApiResponse<String> createWineWishlist(@PathVariable("wineId") Long wineId, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        wineWishlistService.createWineWishlist(wineId, principalDetail.getUsername());

        return ApiResponse.onSuccess("와인 위시리스트 담기 성공");
    }

    @Operation(summary = "위시리스트에 담긴 와인 전체 조회", description = "위시리스트에 담긴 와인 전체 조회한다.")
    @GetMapping("")
    public ApiResponse<List<WinePreviewResponse>> getWineWishlist(@AuthenticationPrincipal PrincipalDetail principalDetail) {
        List<WinePreviewResponse> allWineWishlistByMember = wineWishlistService.getAllWineWishlistByMember(principalDetail.getUsername());
        return ApiResponse.onSuccess(allWineWishlistByMember);
    }

    @Operation(summary = "위시리스트에 담긴 와인 삭제", description = "wineId로 위시리스트에 담긴 와인을 삭제한다.")
    @DeleteMapping("/{wineId}")
    public ApiResponse<String>  deleteWineWishlist(@PathVariable("wineId") Long wineId, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        wineWishlistService.deleteWineWishlistById(wineId, principalDetail.getUsername());
        return ApiResponse.onSuccess("와인 위시리트스 삭제 완료");
    }

}
