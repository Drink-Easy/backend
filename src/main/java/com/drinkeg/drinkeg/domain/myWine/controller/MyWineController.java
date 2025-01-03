package com.drinkeg.drinkeg.domain.myWine.controller;

import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.domain.myWine.controller.request.MyWineRequest;
import com.drinkeg.drinkeg.domain.myWine.controller.request.MyWineUpdateRequest;
import com.drinkeg.drinkeg.domain.myWine.dto.response.MyWineResponse;
import com.drinkeg.drinkeg.domain.myWine.service.MyWineService;
import com.drinkeg.drinkeg.global.apipayLoad.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/my-wine")
public class MyWineController {

    private final MyWineService myWineService;

    @PostMapping("")
    public ApiResponse<String> getMyWines(@RequestBody MyWineRequest myWineRequest,
                                          @AuthenticationPrincipal PrincipalDetail principalDetail){

        myWineService.saveMyWine(myWineRequest, principalDetail.getUsername());
        return ApiResponse.onSuccess("보유 와인 저장 성공");
    }

    @GetMapping("")
    public ApiResponse<List<MyWineResponse>> getMyWines(@AuthenticationPrincipal PrincipalDetail principalDetail){

        List<MyWineResponse> myWineResponseList = myWineService.getMyWinesByUsername(principalDetail.getUsername());
        return ApiResponse.onSuccess(myWineResponseList);
    }


    @Operation(summary = "보유 와인 수정", description = "wineId와 MyWineUpdateRequest 로 보유 와인을 수정한다.")
    @PatchMapping("/{wineWishlistId}")
    public ApiResponse<String>  updateWineWishlist(@PathVariable("wineWishlistId") Long wineWishlistId,
                                                   @RequestBody MyWineUpdateRequest myWineUpdateRequest,
                                                   @AuthenticationPrincipal PrincipalDetail principalDetail) {
        myWineService.updateMyWine(wineWishlistId, myWineUpdateRequest, principalDetail.getUsername());
        return ApiResponse.onSuccess("와인 위시리스트 수정 완료");
    }
}
