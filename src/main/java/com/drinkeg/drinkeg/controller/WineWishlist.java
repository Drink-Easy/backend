package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.wine.dto.response.SearchWineResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.wineWishlist.service.WineWishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wine-wishlist")
public class WineWishlist {
    private final WineWishlistService wineWishlistService;

    @PostMapping("/{wineId}")
    public ApiResponse<String> createWineWishlist(@PathVariable("wineId") Long wineId, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        wineWishlistService.createWineWishlist(wineId, principalDetail.getUsername());

        return ApiResponse.onSuccess("와인 위시리스트 담기 성공");
    }

    @GetMapping("")
    public ApiResponse<List<SearchWineResponseDTO>> getWineWishlist(@AuthenticationPrincipal PrincipalDetail principalDetail) {
        List<SearchWineResponseDTO> allWineWishlistByMember = wineWishlistService.getAllWineWishlistByMember(principalDetail.getUsername());
        return ApiResponse.onSuccess(allWineWishlistByMember);
    }

    @DeleteMapping("/{wineId}")
    public ApiResponse<String>  deleteWineWishlist(@PathVariable("wineId") Long wineId, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        wineWishlistService.deleteWineWishlistById(wineId, principalDetail.getUsername());
        return ApiResponse.onSuccess("와인 위시리트스 삭제 완료");
    }


}
