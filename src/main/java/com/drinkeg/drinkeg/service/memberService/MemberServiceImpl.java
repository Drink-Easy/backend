package com.drinkeg.drinkeg.service.memberService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public Member findMemberById(Long memberId) {

        return memberRepository.findById(memberId).orElseThrow(() -> {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        });
    }

}
