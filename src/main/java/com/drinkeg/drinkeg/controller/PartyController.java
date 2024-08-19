package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.PartyDTO.PartyRequestDTO;
import com.drinkeg.drinkeg.dto.PartyDTO.PartyResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.service.memberService.MemberService;
import com.drinkeg.drinkeg.service.partyService.PartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
        Member foundMember = memberService.loadMemberByPrincipleDetail(principalDetail);

        partyService.createParty(partyRequestDTO, foundMember);
        return ApiResponse.onSuccess("파티 생성 완료");
    }


    // 모임 전체조회
    @GetMapping
    public ApiResponse<List<PartyResponseDTO>> getAllParties() {
        List<PartyResponseDTO> partyResponseDTOS = partyService.getAllParties();
        return ApiResponse.onSuccess(partyResponseDTOS);
    }


    // 정렬 기준에 따른 모임 조회
    // 최신순 /parties?sortType=recent
    // 마감 임박순 /parties?sortType=deadline
    // 인원이 많이 모인 순 /parties?sortType=popular
    // 가격순 /parties?sortType=price
    @GetMapping
    public ApiResponse<Page<PartyResponseDTO>> getSortedParties(
            @RequestParam String sortType,
            @PageableDefault(size = 5) Pageable pageable) {

        // 서비스로 정렬방식 전달
        Page<PartyResponseDTO> sortedParties = partyService.getSortedParties(sortType, pageable);
        return ApiResponse.onSuccess(sortedParties);
    }

    // 모임 단건 조회
    @GetMapping("/{id}")
    public ApiResponse<PartyResponseDTO> getParty(@AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable("id") Long id) {

        // 현재 로그인한 사용자 정보 가져오기
        Member foundMember = memberService.loadMemberByPrincipleDetail(principalDetail);

        PartyResponseDTO partyResponseDTO = partyService.getParty(id);
        return ApiResponse.onSuccess(partyResponseDTO);
    }

    // 모임 수정
    @PutMapping("/{id}")
    public ApiResponse<String> updateParty(
            @PathVariable("id") Long id,
            @RequestBody PartyRequestDTO partyRequestDTO,
            @AuthenticationPrincipal PrincipalDetail principalDetail) {

        // 현재 로그인한 사용자 정보 가져오기
        Member foundMember = memberService.loadMemberByPrincipleDetail(principalDetail);
        Long foundMemberId = foundMember.getId();

        // 서비스로 모임 수정 요청을 보냄
        partyService.updateParty(id, partyRequestDTO, foundMemberId);
        return ApiResponse.onSuccess("모임 수정 완료");
    }


    // 모임 삭제
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteParty(@AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable("id") Long id) {

        // 현재 로그인한 사용자 정보 가져오기
        Member foundMember = memberService.loadMemberByPrincipleDetail(principalDetail);
        Long foundMemberId = foundMember.getId();

        partyService.deleteParty(id, foundMemberId);
        return ApiResponse.onSuccess("모임 삭제 완료");
    }




    //모임 검색

}
