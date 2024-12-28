package com.drinkeg.drinkeg.domain.member.service;

import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;

public interface MemberService {

    public Member getMemberById(Long memberId);

    public Member loadMemberByPrincipalDetail(PrincipalDetail principalDetail);

    public void deleteMemberByUsername(String username);

}
