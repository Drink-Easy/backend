package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.dto.PartyDTO.PartyResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.service.partyBookmarkService.PartyBookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/party-bookmark")
public class PartyBookmarkController {

    private final PartyBookmarkService partyBookmarkService;

    // 북마크 생성
    @PostMapping
    public ApiResponse<String> createBookmark(
            @AuthenticationPrincipal PrincipalDetail principalDetail,
            @RequestParam Long partyId) {

        partyBookmarkService.createBookmark(principalDetail, partyId);
        return ApiResponse.onSuccess("북마크 생성 완료");
    }

    // 북마크 취소
    @DeleteMapping("/{partyId}")
    public ApiResponse<String> cancelBookmark(
            @AuthenticationPrincipal PrincipalDetail principalDetail,
            @PathVariable("partyId") Long partyId) {

        partyBookmarkService.cancelBookmark(principalDetail, partyId);
        return ApiResponse.onSuccess("북마크 취소 완료");
    }

    // 멤버가 북마크한 모임들을 get
    @GetMapping("/partyBookmark")
    public ApiResponse<List<PartyResponseDTO>> getMemberBookmarks(
            @AuthenticationPrincipal PrincipalDetail principalDetail) {

        List<PartyResponseDTO> bookmarkedParties = partyBookmarkService.getMemberBookmarks(principalDetail);
        return ApiResponse.onSuccess(bookmarkedParties);
    }
}