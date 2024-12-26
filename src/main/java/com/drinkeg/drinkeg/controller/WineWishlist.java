package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.wineWishlist.dto.request.WineWishlistRequestDTO;
import com.drinkeg.drinkeg.wineWishlist.dto.response.WineWishlistResponseDTO;
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

    @PostMapping("")
    public ApiResponse<WineWishlistResponseDTO> createWineWishlist(@RequestBody WineWishlistRequestDTO wineWishlistRequestDTO, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        WineWishlistResponseDTO wineWishlistResponseDTO = wineWishlistService.createWineWishlist(wineWishlistRequestDTO, principalDetail.getUsername());
        return ApiResponse.onSuccess(wineWishlistResponseDTO);
    }

    @GetMapping("")
    public ApiResponse<List<WineWishlistResponseDTO>> getWineWishlist(@AuthenticationPrincipal PrincipalDetail principalDetail) {
        List<WineWishlistResponseDTO> wineWishlistResponseDTOS = wineWishlistService.getAllWineWishlistByMember(principalDetail.getUsername());
        return ApiResponse.onSuccess(wineWishlistResponseDTOS);
    }

    @DeleteMapping("/{wineWishlistId}")
    public ApiResponse<String>  deleteWineWishlist(@PathVariable("wineWishlistId") Long wineWishlistId, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        wineWishlistService.deleteWineWishlistById(wineWishlistId, principalDetail.getUsername());
        return ApiResponse.onSuccess("와인 위시리트스 삭제 완료");
    }


}
