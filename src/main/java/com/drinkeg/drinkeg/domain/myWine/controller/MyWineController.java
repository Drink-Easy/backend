package com.drinkeg.drinkeg.domain.myWine.controller;

import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.domain.myWine.dto.request.MyWineRequest;
import com.drinkeg.drinkeg.domain.myWine.dto.request.MyWineUpdateRequest;
import com.drinkeg.drinkeg.domain.myWine.dto.response.MyWineResponse;
import com.drinkeg.drinkeg.domain.myWine.service.MyWineService;
import com.drinkeg.drinkeg.global.apipayLoad.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/my-wine")
public class MyWineController {

    private final MyWineService myWineService;

    @Operation(summary = "보유 와인 추가", description = "MyWineRequest로 보유와인을 추가한다.")
    @PostMapping("")
    public ApiResponse<String> saveMyWine(@RequestBody @Valid MyWineRequest myWineRequest,
                                          @AuthenticationPrincipal PrincipalDetail principalDetail){

        myWineService.saveMyWine(myWineRequest, principalDetail.getUsername());
        return ApiResponse.onSuccess("보유 와인 저장 성공");
    }

    @Operation(summary = "보유 와인 조회", description = "보유 와인을 조회한다.")
    @GetMapping("/{myWineId}")
    public ApiResponse<MyWineResponse> getMyWine(@PathVariable("myWineId") Long myWineId, @AuthenticationPrincipal PrincipalDetail principalDetail){
        MyWineResponse myWineResponse = myWineService.getMyWineById(myWineId, principalDetail.getUsername(), LocalDate.now());
        return ApiResponse.onSuccess(myWineResponse);
    }

    @Operation(summary = "보유 와인 목록 조회", description = "보유 와인 목록을 조회한다.")
    @GetMapping("")
    public ApiResponse<List<MyWineResponse>> getMyWineList(@AuthenticationPrincipal PrincipalDetail principalDetail){

        List<MyWineResponse> myWineResponseList = myWineService.getMyWinesByUsername(principalDetail.getUsername(), LocalDate.now());
        return ApiResponse.onSuccess(myWineResponseList);
    }

    @Operation(summary = "보유 와인 수정", description = "wineId와 MyWineUpdateRequest 로 보유 와인을 수정한다.")
    @PatchMapping("/{myWindId}")
    public ApiResponse<String>  updateMyWine(@PathVariable("myWindId") Long wineWishlistId,
                                                   @RequestBody MyWineUpdateRequest myWineUpdateRequest,
                                                   @AuthenticationPrincipal PrincipalDetail principalDetail) {
        myWineService.updateMyWine(wineWishlistId, myWineUpdateRequest, principalDetail.getUsername());
        return ApiResponse.onSuccess("보유 와인 수정 완료");
    }

    @Operation(summary = "보유 와인 삭제", description = "보유 와인 Id로 보유 와인을 삭제한다.")
    @DeleteMapping("/{myWineId}")
    public ApiResponse<String>  deleteMyWine(@PathVariable("myWineId") Long myWineId, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        myWineService.deleteMyWineById(myWineId, principalDetail.getUsername());
        return ApiResponse.onSuccess("보유 와인 삭제 완료");
    }
}
