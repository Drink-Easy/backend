package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.apipayLoad.handler.TempHandler;
import com.drinkeg.drinkeg.dto.AppleLoginDTO.AppleLoginRequestDTO;
import com.drinkeg.drinkeg.dto.loginDTO.oauth2DTO.LoginResponse;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.service.loginService.AppleLoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class OAuth2Controller {

    private  final AppleLoginService appleLoginService;
    @PostMapping("/login/apple")
    public ApiResponse<LoginResponse> appleLogin(@RequestBody AppleLoginRequestDTO appleLoginRequestDTO, HttpServletResponse response) throws Exception{

        System.out.println("=========start apple login controller============");


        return ApiResponse.onSuccess(appleLoginService.appleLogin(appleLoginRequestDTO, response));
    }
}
