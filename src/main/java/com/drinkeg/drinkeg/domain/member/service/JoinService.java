package com.drinkeg.drinkeg.domain.member.service;

import com.drinkeg.drinkeg.domain.member.dto.*;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import com.drinkeg.drinkeg.infra.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class JoinService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final StorageService storageService;



    public void join(JoinRequest joinRequest) {

        String username = joinRequest.getUsername();
        String password = joinRequest.getPassword();
        String rePassword = joinRequest.getRePassword();

        if (memberRepository.existsByUsername(username)) {
            throw new GeneralException(ErrorStatus.MEMBER_ALREADY_EXIST);
        }
        if (!password.equals(rePassword)){
            throw new GeneralException(ErrorStatus.PASSWORD_NOT_MATCH);
        }

        Member member = Member.createMember( username,(bCryptPasswordEncoder.encode(password) ),true);
        memberRepository.save(member);

    }

    public MemberResponseDTO addMemberDetail(MemberRequest memberRequest, String username) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));


        // 회원이 입력한 정보로 update 하고 isFirst = false 로 변경
        member.updateFirstUser(memberRequest);

        memberRepository.save(member);


        return MemberResponseDTO.of(member);
    }


    @Transactional(readOnly = true)
    public boolean isDuplicatedUsername(UsernameCheckRequest usernameCheckRequest) {

        return memberRepository.existsByUsername(usernameCheckRequest.username());
    }
}