package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.WineStoreDTO.request.WineStoreRequestDTO;
import com.drinkeg.drinkeg.dto.WineStoreDTO.response.WineStoreResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.jwtDTO.PrincipalDetail;
import com.drinkeg.drinkeg.service.memberService.MemberService;
import com.drinkeg.drinkeg.service.wineStoreService.WineStoreService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wine-store")
public class WineStoreController {
    private final WineStoreService wineStoreService;
    private final MemberService memberService;

    @GetMapping("")
    public ApiResponse<List<WineStoreResponseDTO>> getAllWineStores() {
        List<WineStoreResponseDTO> wineStoreResponseDTOS = wineStoreService.getAllWineStores();
        return ApiResponse.onSuccess(wineStoreResponseDTOS);
    }

    @GetMapping("/{wineStoreId}")
    public ApiResponse<WineStoreResponseDTO> getWineStoreById(@PathVariable Long wineStoreId) {
        WineStoreResponseDTO wineStoreResponseDTO = wineStoreService.getWineStoreById(wineStoreId);
        return ApiResponse.onSuccess(wineStoreResponseDTO);
    }

    @Transactional
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // *추후에 ROLE_OWNER 추가 후 인증절차 필요.
    public ApiResponse<String> createWineStore(@AuthenticationPrincipal PrincipalDetail principalDetail, @RequestBody @Valid WineStoreRequestDTO wineStoreRequestDTO) {
        // 유저 정보 불러오기
        Member owner = memberService.findMemberByUsername(principalDetail.getUsername());

        // 와인 스토어 저장
        wineStoreService.saveWineStore(wineStoreRequestDTO, owner);

        return ApiResponse.onSuccess("와인 스토어 생성 완료");
    }

    @PutMapping("/{wineStoreId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // *추후에 ROLE_OWNER 추가 후 인증절차 필요.
    public ApiResponse<WineStoreResponseDTO> updateWineStore(@AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable Long wineStoreId, @RequestBody @Valid WineStoreRequestDTO wineStoreRequestDTO) {
        // 유저 정보 불러오기
        Member owner = memberService.findMemberByUsername(principalDetail.getUsername());

        WineStoreResponseDTO wineStoreResponseDTO = wineStoreService.updateWineStore(wineStoreId, wineStoreRequestDTO, owner);
        return ApiResponse.onSuccess(wineStoreResponseDTO);
    }

    @DeleteMapping("/{wineStoreId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // *추후에 ROLE_OWNER 추가 후 인증절차 필요.
    public ApiResponse<String> deleteWineStore(@AuthenticationPrincipal PrincipalDetail principalDetail, @PathVariable Long wineStoreId) {
        // 유저 정보 불러오기
        Member owner = memberService.findMemberByUsername(principalDetail.getUsername());

        wineStoreService.deleteWineStore(wineStoreId, owner);
        return ApiResponse.onSuccess("와인 스토어 삭제 완료");
    }
}
