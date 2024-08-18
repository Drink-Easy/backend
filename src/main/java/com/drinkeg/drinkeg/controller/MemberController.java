package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.apipayLoad.handler.TempHandler;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.AppleLoginDTO.AppleLoginRequestDTO;
import com.drinkeg.drinkeg.dto.loginDTO.jwtDTO.JoinDTO;
import com.drinkeg.drinkeg.dto.loginDTO.oauth2DTO.LoginResponse;
import com.drinkeg.drinkeg.jwt.JWTUtil;
import com.drinkeg.drinkeg.service.loginService.AppleLoginService;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.MemberRequestDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.MemberResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.service.loginService.JoinService;
import com.drinkeg.drinkeg.service.loginService.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final JoinService joinService;
    private final TokenService tokenService;
    private final AppleLoginService appleLoginService;
    private final JWTUtil jwtUtil;

    @PostMapping("/join")
    public ApiResponse<?> joinProcess(@RequestBody JoinDTO joinDTO) {

        joinService.join(joinDTO);
        return ApiResponse.onSuccess("회원가입 성공");
    }

    @PostMapping("/reissue")
    public ApiResponse<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        tokenService.reissueRefreshToken(request, response);
        return ApiResponse.onSuccess("토큰 재발급 성공");
    }

    @PatchMapping("/member")
    public ApiResponse<MemberResponseDTO> addMemberDetail(@RequestBody MemberRequestDTO memberRequestDTO, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        return ApiResponse.onSuccess(joinService.addMemberDetail(memberRequestDTO, principalDetail.getUsername()));
    }

    @PostMapping("/login/apple")
    public ApiResponse<LoginResponse> appleLogin(@RequestBody AppleLoginRequestDTO appleLoginRequestDTO,HttpServletResponse response) throws Exception{

        System.out.println("=========start apple login controller============");

        if(appleLoginRequestDTO.getIdentityToken() == null){
            throw new TempHandler(ErrorStatus.IDENTITY_TOKEN_NOT_FOUND);
        }

        LoginResponse loginResponse = appleLoginService.appleLogin(appleLoginRequestDTO);

        String accessToken = jwtUtil.createJwt("access", loginResponse.getUsername(), loginResponse.getRole(), 60000000000L); // 임의로 10000배로 해놓았음. 나중에 수정 필요.
        String refreshToken = jwtUtil.createJwt("refresh", loginResponse.getUsername(), loginResponse.getRole(), 864000000L);

        response.addCookie(tokenService.createCookie("accessToken", accessToken));
        response.addCookie(tokenService.createCookie("refreshToken", refreshToken));
        response.setStatus(HttpStatus.OK.value());

        return ApiResponse.onSuccess(loginResponse);



    }



}
