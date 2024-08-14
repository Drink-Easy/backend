package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.PartyDTO.PartyRequestDTO;
import com.drinkeg.drinkeg.dto.PartyDTO.PartyResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.jwtDTO.PrincipalDetail;
import com.drinkeg.drinkeg.service.memberService.MemberService;
import com.drinkeg.drinkeg.service.partyService.PartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/parties")
public class PartyController {


    private final PartyService partyService;
    private final MemberService memberService;

    // 모임 생성
    @PostMapping
    public ApiResponse<String> createParty(@AuthenticationPrincipal PrincipalDetail principalDetail, @RequestBody PartyRequestDTO partyRequestDTO) {

        // 현재 로그인한 사용자 정보 가져오기
        String username = principalDetail.getUsername();
        Member foundMember = memberService.findMemberByUsername(username);

        partyService.createParty(partyRequestDTO, foundMember);
        return ApiResponse.onSuccess("파티 생성 완료");
    }


    // 모임 전체조회
    @GetMapping
    public ApiResponse<List<PartyResponseDTO>> getAllParties() {
        List<PartyResponseDTO> partyResponseDTOS = partyService.getAllParties();
        return ApiResponse.onSuccess(partyResponseDTOS);
    }

    // 모임 단건 조회
    @GetMapping("/{id}")
    public ApiResponse<PartyResponseDTO> getParty(@AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable("id") Long id) {
        String username = principalDetail.getUsername();
        Member foundMember = memberService.findMemberByUsername(username);

        PartyResponseDTO partyResponseDTO = partyService.getParty(id);
        return ApiResponse.onSuccess(partyResponseDTO);
    }

    // 모임 수정
    @PutMapping("/{id}")
    public ApiResponse<String> updateParty(
            @PathVariable("id") Long id,
            @RequestBody PartyRequestDTO partyRequestDTO,
            @AuthenticationPrincipal PrincipalDetail principalDetail) {

        // 서비스로 모임 수정 요청을 보냄
        String memberName = principalDetail.getUsername();
        Long memberId = memberService.findMemberByUsername(memberName).getId();

        partyService.updateParty(id, partyRequestDTO, memberId);
        return ApiResponse.onSuccess("모임 수정 완료");
    }


    // 모임 삭제
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteParty(@AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable("id") Long id) {
        String username = principalDetail.getUsername();
        Long foundMemberId = memberService.findMemberByUsername(username).getId();

        partyService.deleteParty(id, foundMemberId);
        return ApiResponse.onSuccess("모임 삭제 완료");
    }





    //모임 메인페이지 조회

    //모임 검색

}
