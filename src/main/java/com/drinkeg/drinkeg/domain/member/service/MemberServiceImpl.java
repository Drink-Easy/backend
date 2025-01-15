package com.drinkeg.drinkeg.domain.member.service;

import com.drinkeg.drinkeg.domain.member.dto.MemberInfoResponse;
import com.drinkeg.drinkeg.domain.member.dto.MemberUpdateRequest;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.NameCheckResponse;
import com.drinkeg.drinkeg.domain.tastingNote.event.RemoveTastingNoteMemberEvent;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import com.drinkeg.drinkeg.infra.storage.StoragePathName;
import com.drinkeg.drinkeg.infra.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final StorageService storageService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Member loadMemberByPrincipalDetail(PrincipalDetail principalDetail) {
        // 현재 로그인한 사용자 정보 가져오기
        String username = principalDetail.getUsername();

        return memberRepository.findByUsername(username).orElseThrow(()
                -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
    }
  
    @Override
    @Transactional
    public void deleteMemberByUsername(String username){
        eventPublisher.publishEvent(new RemoveTastingNoteMemberEvent(username));
        memberRepository.deleteByUsername(username);
    }

    @Override
    public MemberInfoResponse showMemberInfo(String username){
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        return MemberInfoResponse.of(member);
    }

    @Override
    public boolean isNicknameAvailable(String nickname){
        return !memberRepository.existsByName(nickname);
    }

    @Override
    @Transactional
    public void updateMemberInfo(MemberUpdateRequest memberUpdateRequest, String username){
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        member.updateMemberInfo(memberUpdateRequest);
        memberRepository.save(member);

    }

    @Override
    public String uploadProfileImage(MultipartFile profileImg, String username) {
        String profileImgUrl = storageService.uploadFile(profileImg, StoragePathName.MEMBER_PROFILE);
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        if (member.getImageUrl() != null)
            storageService.deleteFile(member.getImageUrl());
        member.updateImageUrl(profileImgUrl);
        return profileImgUrl;
    }
}
