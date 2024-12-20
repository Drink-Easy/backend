package com.drinkeg.drinkeg.member.service;

import com.drinkeg.drinkeg.member.domain.Member;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;

public interface MemberService {

    public Member getMemberById(Long memberId);

    public Member loadMemberByPrincipalDetail(PrincipalDetail principalDetail);

}
