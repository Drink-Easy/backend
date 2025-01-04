package com.drinkeg.drinkeg.domain.member.controller;

import com.drinkeg.drinkeg.domain.tastingNote.service.TastingNoteService;
import com.drinkeg.drinkeg.global.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.global.security.jwt.TokenService;
import com.drinkeg.drinkeg.domain.member.dto.JoinDTO;
import com.drinkeg.drinkeg.domain.member.dto.MemberRequestDTO;
import com.drinkeg.drinkeg.domain.member.dto.MemberResponseDTO;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.domain.member.service.JoinService;
import com.drinkeg.drinkeg.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authorization", description = "스프링 시큐리티 관련 API")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final JoinService joinService;
    private final MemberService memberService;
    private final TokenService tokenService;

    @PostMapping("/join")
    @Operation(summary = "회원가입", description = "username과 password를 입력받아 회원가입을 진행합니다.")
    public ApiResponse<?> joinProcess(@RequestBody JoinDTO joinDTO) {

        joinService.join(joinDTO);
        return ApiResponse.onSuccess("회원가입 성공");
    }


    @PatchMapping("/member")
    @Operation(summary = "사용자 초기 정보 추가", description = "첫 로그인 여부에 따라 isFirst 속성이 true인 경우 사용자 초기 정보를 추가합니다.")
    public ApiResponse<MemberResponseDTO> addMemberDetail(@RequestBody MemberRequestDTO memberRequestDTO, @AuthenticationPrincipal PrincipalDetail principalDetail) {

        MemberResponseDTO memberResponseDTO = joinService.addMemberDetail(memberRequestDTO, principalDetail.getUsername());
        return ApiResponse.onSuccess(memberResponseDTO);
    }

    @DeleteMapping("/member/delete")
    @Operation(summary = "사용자 탈퇴", description = "사용자 정보를 삭제합니다.")
    public ApiResponse<?> deleteProcess(@AuthenticationPrincipal PrincipalDetail principalDetail,  HttpServletResponse response){
            memberService.deleteMemberByUsername(principalDetail.getUsername());
            tokenService.deleteRefreshTokenAndAccessToken(response, principalDetail.getUsername());

            return ApiResponse.onSuccess("회원 탈퇴 성공");


    }




}
