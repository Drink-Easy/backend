package com.drinkeg.drinkeg.domain.member.service;

import com.drinkeg.drinkeg.domain.member.dto.*;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import com.drinkeg.drinkeg.infra.storage.StoragePathName;
import com.drinkeg.drinkeg.infra.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final StorageService storageService;


    @Transactional
    public void join(JoinDTO joinDTO) {

        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();
        String rePassword = joinDTO.getRePassword();

        if (memberRepository.existsByUsername("drinkeg "+ username)) {
            throw new GeneralException(ErrorStatus.MEMBER_ALREADY_EXIST);
        }
        if (!password.equals(rePassword)){
            throw new GeneralException(ErrorStatus.PASSWORD_NOT_MATCH);
        }
        if(!isValidPassword(password)){
            throw new GeneralException(ErrorStatus.PASSWORD_NOT_INVALID);
        }

        Member member = Member.createMember( username,(bCryptPasswordEncoder.encode(password) ),true);

        memberRepository.save(member);
        System.out.println("Saved Member: " );

    }

    public MemberResponseDTO addMemberDetail(MemberRequestDTO memberRequestDTO, String username, MultipartFile multipartFile) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus.SESSION_UNAUTHORIZED));

        String profileImage = null;

        if (multipartFile != null ) {
            profileImage = storageService.uploadFile(multipartFile, StoragePathName.MEMBER_PROFILE);
        }

        // 회원이 입력한 정보로 update 하고 isFirst = false 로 변경
        member.updateFirstUser(memberRequestDTO.getName(), memberRequestDTO.getIsNewbie(), memberRequestDTO.getMonthPrice(),
                memberRequestDTO.getWineSort(), memberRequestDTO.getWineArea(), memberRequestDTO.getWineVariety(),memberRequestDTO.getRegion(),
                profileImage);

        memberRepository.save(member);


        return MemberResponseDTO.create(member);
    }

    public boolean isValidPassword(String password) {

        // 영문자, 숫자, 특수문자 각각에 대한 패턴
        String letterPattern = ".*[A-Za-z].*";
        String digitPattern = ".*\\d.*";

        boolean hasLetter = password.matches(letterPattern);
        boolean hasDigit = password.matches(digitPattern);

        // 세 가지 조건이 모두 충족되는지 확인
        return hasLetter && hasDigit;
    }

    public UsernameCheckResponse isDuplicatedUsername(UsernameCheckRequest usernameCheckRequest) {

        return new UsernameCheckResponse(memberRepository.existsByUsername(usernameCheckRequest.username()));
    }
}