package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.dto.loginDTO.jwtDTO.PrincipalDetail;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

// 테스트용 컨드롤러
@Controller
public class MainController {

    @GetMapping("/maindy")
    @ResponseBody
    public String mainAPI() {

        return "main route";

    }

    @GetMapping("/main")
    public ApiResponse<?> mainP() {
        return ApiResponse.onSuccess("하윙");
    }

    @GetMapping("/home")
    public ApiResponse<?> home(@AuthenticationPrincipal PrincipalDetail principalDetail) {


        return ApiResponse.onSuccess("하윙");
    }
}
