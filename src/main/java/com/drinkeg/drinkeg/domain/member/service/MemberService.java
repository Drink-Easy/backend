package com.drinkeg.drinkeg.domain.member.service;

import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.dto.MemberInfoResponse;
import com.drinkeg.drinkeg.domain.member.dto.MemberUpdateRequest;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;

public interface MemberService {

    public Member getMemberById(Long memberId);

    public Member loadMemberByPrincipalDetail(PrincipalDetail principalDetail);

    public void deleteMemberByUsername(String username);

    public MemberInfoResponse showMemberInfo(String username);

    public boolean isNicknameAvailable(String nickname);

    public void updateMemberInfo(PrincipalDetail principalDetail, MemberUpdateRequest memberUpdateRequest);
}
