package com.drinkeg.drinkeg.service.memberService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public Member getMemberById(Long memberId) {

        return memberRepository.findById(memberId).orElseThrow(()
                -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
    }

    @Override
    public Member getMemberByUsername(String username) {
        return memberRepository.findByUsername(username).orElseThrow(()
                -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
    }

    @Override
    public Member loadMemberByPrincipleDetail(PrincipalDetail principalDetail) {
        // 현재 로그인한 사용자 정보 가져오기
        String username = principalDetail.getUsername();

        return memberRepository.findByUsername(username).orElseThrow(()
                -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
    }

}
