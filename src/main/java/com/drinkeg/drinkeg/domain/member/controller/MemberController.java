package com.drinkeg.drinkeg.domain.member.controller;

import com.drinkeg.drinkeg.domain.member.dto.*;
import com.drinkeg.drinkeg.global.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.global.security.jwt.TokenService;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.domain.member.service.JoinService;
import com.drinkeg.drinkeg.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Authorization", description = "스프링 시큐리티 관련 API")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final JoinService joinService;
    private final MemberService memberService;
    private final TokenService tokenService;

    @PostMapping("/join")
    @Operation(summary = "회원가입", description = "username과 password를 입력받아 회원가입을 진행합니다.")
    public ApiResponse<?> joinProcess(@RequestBody JoinRequest joinRequest) {

        joinService.join(joinRequest);
        return ApiResponse.onSuccess("회원가입 성공");
    }


    @PatchMapping("/member")
    @Operation(summary = "사용자 초기 정보 추가", description = "첫 로그인 여부에 따라 isFirst 속성이 true인 경우 사용자 초기 정보를 추가합니다.")
    public ApiResponse<?> addMemberDetail(@RequestPart(value = "multipartFile", required = false) MultipartFile multipartFile,  @RequestPart("memberRequest")  MemberRequestDTO memberRequestDTO, @AuthenticationPrincipal PrincipalDetail principalDetail) {

        MemberResponseDTO memberResponseDTO = joinService.addMemberDetail(memberRequestDTO, principalDetail.getUsername(),multipartFile);
        return ApiResponse.onSuccess("사용자 초기 정보 추가 완료");
    }

    @DeleteMapping("/member/delete")
    @Operation(summary = "사용자 탈퇴", description = "사용자 정보를 삭제합니다.")

    public ApiResponse<?> deleteProcess(@AuthenticationPrincipal PrincipalDetail principalDetail,  HttpServletResponse response){
            memberService.deleteMemberByUsername(principalDetail.getUsername());
            tokenService.deleteRefreshTokenAndAccessToken(response, principalDetail.getUsername());
        return ApiResponse.onSuccess("회원 탈퇴 성공");


    }

    @GetMapping("/member/info")
    @Operation(summary = "마이페이지 ", description = "유저 정보를 불러옵니다.")
    public ApiResponse<MemberInfoResponse> getMemberInfo(@AuthenticationPrincipal PrincipalDetail principalDetail) {
        return ApiResponse.onSuccess(memberService.showMemberInfo(principalDetail.getUsername()));
    }

    @PostMapping("/member/{nickname}")
    @Operation(summary = "마이페이지내에 닉네임 중복 검사 ", description = "중복된 닉네임이면 False, 사용 가능한 닉네임이면 True를 반환합니다.")
    public ApiResponse<?> checkNickname(@AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable String nickname){
        return ApiResponse.onSuccess(memberService.isNicknameAvailable(nickname));
    }

    @PatchMapping("/member/info")
    @Operation(summary = "마이페이지 정보 수정 ", description = "마이페이지의 정보를 수정합니다.")
    public ApiResponse<?> updateMemberInfo(@AuthenticationPrincipal PrincipalDetail principalDetail, @RequestPart(value = "multipartFile", required = false) MultipartFile multipartFile,  @RequestPart("memberUpdateRequest")  MemberUpdateRequest memberUpdateRequest){

        memberService.updateMemberInfo(principalDetail,memberUpdateRequest, multipartFile);
        return ApiResponse.onSuccess("정보 수정 성공");
    }

    @PostMapping("/join/check")
    @Operation(summary = "이메일 중복 검사", description = "이메일(username) 중복 여부를 반환합니다.")
    public ApiResponse<UsernameCheckResponse> checkUsername(@RequestBody UsernameCheckRequest usernameCheckRequest) {
        UsernameCheckResponse usernameCheckResponse = joinService.isDuplicatedUsername(usernameCheckRequest);
        return ApiResponse.onSuccess(usernameCheckResponse);
    }
}
