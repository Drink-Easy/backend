package com.drinkeg.drinkeg.domain.myWine.controller;

import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.domain.myWine.dto.request.MyWineRequest;
import com.drinkeg.drinkeg.domain.myWine.dto.response.MyWineResponse;
import com.drinkeg.drinkeg.domain.myWine.service.MyWineService;
import com.drinkeg.drinkeg.global.apipayLoad.ApiResponse;
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
}
