package com.drinkeg.drinkeg.domain.member.service;

import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Member getMemberById(Long memberId) {

        return memberRepository.findById(memberId).orElseThrow(()
                -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
    }

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
        memberRepository.deleteByUsername(username);
    }
}
