package com.drinkeg.drinkeg.service.memberService;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;

public interface MemberService {

    public Member findMemberById(Long memberId);

    public Member findMemberByUsername(String username);

    public Member loadMemberByPrincipleDetail(PrincipalDetail principalDetail);
}
