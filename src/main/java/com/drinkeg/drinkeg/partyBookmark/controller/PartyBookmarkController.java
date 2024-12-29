package com.drinkeg.drinkeg.partyBookmark.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.party.dto.PartyResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.partyBookmark.service.PartyBookmarkService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "북마크 생성", description = "모임 id로 북마크 생성")
    public ApiResponse<String> createBookmark(
            @AuthenticationPrincipal PrincipalDetail principalDetail,
            @RequestParam("partyId") Long partyId) {

        partyBookmarkService.createBookmark(principalDetail, partyId);
        return ApiResponse.onSuccess("북마크 생성 완료");
    }

    // 북마크 취소
    @DeleteMapping("/{partyId}")
    @Operation(summary = "북마크 취소", description = "모임 id로 북마크 하드 삭제")
    public ApiResponse<String> cancelBookmark(
            @AuthenticationPrincipal PrincipalDetail principalDetail,
            @PathVariable("partyId") Long partyId) {

        partyBookmarkService.cancelBookmark(principalDetail, partyId);
        return ApiResponse.onSuccess("북마크 취소 완료");
    }

    // 멤버가 북마크한 모임들을 get
    @GetMapping("/partyBookmark")
    @Operation(summary = "북마크 조회", description = "로그인된 사용자가 북마크한 모임들을 PartyResponseDTO로 조회")
    public ApiResponse<List<PartyResponseDTO>> getMemberBookmarks(
            @AuthenticationPrincipal PrincipalDetail principalDetail) {

        List<PartyResponseDTO> bookmarkedParties = partyBookmarkService.getMemberBookmarks(principalDetail);
        return ApiResponse.onSuccess(bookmarkedParties);
    }
}