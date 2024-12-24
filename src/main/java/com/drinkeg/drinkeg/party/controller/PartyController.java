package com.drinkeg.drinkeg.party.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.party.dto.PartyRequestDTO;
import com.drinkeg.drinkeg.party.dto.PartyResponseDTO;
import com.drinkeg.drinkeg.party.service.PartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/parties")
public class PartyController {

    private final PartyService partyService;

    // 모임 생성
    @PostMapping
    public ApiResponse<String> createParty(
            @AuthenticationPrincipal PrincipalDetail principalDetail,
            @RequestBody PartyRequestDTO partyRequestDTO) {

        partyService.createParty(partyRequestDTO, principalDetail);

        return ApiResponse.onSuccess("파티 생성 완료");
    }


    // 모임 전체조회
    @GetMapping
    public ApiResponse<List<PartyResponseDTO>> getAllParties(
            @AuthenticationPrincipal PrincipalDetail principalDetail) {

        List<PartyResponseDTO> partyResponseDTOS = partyService.getAllParties(principalDetail);

        return ApiResponse.onSuccess(partyResponseDTOS);
    }


    // 정렬 기준에 따른 모임 조회
    // 최신순 /parties/sorted?sortType=recent
    // 마감 임박순 /parties/sorted?sortType=deadline
    // 인원이 많이 모인 순 /parties/sorted?sortType=popular
    // 가격순 /parties/sorted?sortType=price
    // 거리순 /parties/sorted?sortType=distance
    @GetMapping("/sorted")
    public ApiResponse<Page<PartyResponseDTO>> getSortedParties(
            @AuthenticationPrincipal PrincipalDetail principalDetail,
            @RequestParam("sortType") String sortType,
            @PageableDefault(size = 5) Pageable pageable) {

        // 서비스로 정렬방식 전달
        Page<PartyResponseDTO> sortedParties = partyService.getSortedParties(sortType, principalDetail, pageable);

        return ApiResponse.onSuccess(sortedParties);
    }

    // 모임 단건 조회
    @GetMapping("/{id}")
    public ApiResponse<PartyResponseDTO> getParty(
            @AuthenticationPrincipal PrincipalDetail principalDetail,
            @PathVariable("id") Long id) {

        PartyResponseDTO partyResponseDTO = partyService.getParty(id);

        return ApiResponse.onSuccess(partyResponseDTO);
    }

    // 모임 수정
    @PutMapping("/{id}")
    public ApiResponse<String> updateParty(
            @AuthenticationPrincipal PrincipalDetail principalDetail,
            @PathVariable("id") Long id,
            @RequestBody PartyRequestDTO partyRequestDTO) {

        // 서비스로 모임 수정 요청을 보냄
        partyService.updateParty(id, partyRequestDTO, principalDetail);

        return ApiResponse.onSuccess("모임 수정 완료");
    }


    // 모임 삭제
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteParty(
            @AuthenticationPrincipal PrincipalDetail principalDetail,
            @PathVariable("id") Long id) {

        partyService.deleteParty(id, principalDetail);

        return ApiResponse.onSuccess("모임 삭제 완료");
    }




    //모임 검색
    @GetMapping("/search")
    public ApiResponse<List<PartyResponseDTO>> searchPartiesByName(
            @AuthenticationPrincipal PrincipalDetail principalDetail,
            @RequestParam("searchName") String searchName) {

        // 모임 제목으로 검색된 결과 리스트를 반환
        List<PartyResponseDTO> searchPartyResponseDTOS = partyService.searchPartiesByName(searchName, principalDetail);

        return ApiResponse.onSuccess(searchPartyResponseDTOS);
    }

}
