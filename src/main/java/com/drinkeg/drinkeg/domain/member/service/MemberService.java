package com.drinkeg.drinkeg.domain.member.service;

import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.dto.MemberInfoResponse;
import com.drinkeg.drinkeg.domain.member.dto.MemberUpdateRequest;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {
    public Member loadMemberByPrincipalDetail(PrincipalDetail principalDetail);

    public void deleteMemberByUsername(String username);

    public MemberInfoResponse showMemberInfo(String username);

    public boolean isNicknameAvailable(String nickname);

    public void updateMemberInfo(MemberUpdateRequest memberUpdateRequest, String username);

    public String uploadProfileImage(MultipartFile multipartFile, String username);

    public String showMemberName(String username);

    public void deleteProfileImage(String username);
}
