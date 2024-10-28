package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.apipayLoad.handler.TempHandler;
import com.drinkeg.drinkeg.dto.AppleLoginDTO.AppleLoginRequestDTO;
import com.drinkeg.drinkeg.dto.loginDTO.oauth2DTO.LoginResponseDTO;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.service.loginService.AppleLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Tag(name = "Authorization", description = "스프링 시큐리티 관련 API")
@RestController
@RequiredArgsConstructor
public class OAuth2Controller {

    private  final AppleLoginService appleLoginService;
    @PostMapping("/login/apple")
    @Operation(summary = "애플로그인", description = "클라이언트에게 Identity Token을 전달 받아 유저 정보를 저장합니다.")
    public ApiResponse<LoginResponseDTO> appleLogin(@RequestBody AppleLoginRequestDTO appleLoginRequestDTO, HttpServletResponse response) throws Exception{

        System.out.println("=========start apple login controller============");
        LoginResponseDTO loginResponseDTO = appleLoginService.appleLogin(appleLoginRequestDTO, response);
        return ApiResponse.onSuccess(loginResponseDTO);
    }
}
