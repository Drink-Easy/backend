package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.drinkeg.drinkeg.service.memberService.MemberService;
import com.drinkeg.drinkeg.service.partyJoinMemberService.PartyJoinMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/partyJoin")
public class PartyJoinMemberController {

    private final PartyJoinMemberService partyJoinMemberService;

    // 특정 멤버가 특정 모임에 참가하는 API
    @PostMapping("/{partyId}")
    public ApiResponse<String> participateInParty(
            @AuthenticationPrincipal PrincipalDetail principalDetail,
            @PathVariable("partyId") Long partyId) {

        // 서비스에서 멤버의 모임 참가 처리
        partyJoinMemberService.participateInParty(principalDetail, partyId);

        return ApiResponse.onSuccess("참가 완료");
    }

    // 모임 참가 취소 API
    @DeleteMapping("/partyJoin/{partyId}")
    public ApiResponse<String> cancelPartyJoin(
            @AuthenticationPrincipal PrincipalDetail principalDetail,
            @PathVariable("partyId") Long partyId) {

        partyJoinMemberService.cancelPartyJoin(principalDetail, partyId);

        return ApiResponse.onSuccess("모임 참가가 취소되었습니다.");
    }
}
