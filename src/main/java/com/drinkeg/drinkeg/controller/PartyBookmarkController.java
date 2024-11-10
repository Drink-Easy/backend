package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.service.partyBookmarkService.PartyBookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
            @PathVariable Long partyId) {

        partyBookmarkService.cancelBookmark(principalDetail, partyId);
        return ApiResponse.onSuccess("북마크 취소 완료");
    }
}