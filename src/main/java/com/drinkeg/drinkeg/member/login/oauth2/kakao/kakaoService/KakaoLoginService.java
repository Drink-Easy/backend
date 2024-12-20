package com.drinkeg.drinkeg.member.login.oauth2.kakao.kakaoService;


import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.member.converter.MemberConverter;
import com.drinkeg.drinkeg.member.domain.Member;
import com.drinkeg.drinkeg.member.login.oauth2.dto.LoginResponseDTO;
import com.drinkeg.drinkeg.member.login.oauth2.kakao.kakaoLoginDTO.KakaoLoginRequestDTO;
import com.drinkeg.drinkeg.member.repostitory.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoLoginService {

    private final MemberRepository memberRepository;
    private final MemberConverter memberConverter;

    public LoginResponseDTO kakaoLogin(KakaoLoginRequestDTO kakaoLoginRequestDTO){

        String kakaoname = kakaoLoginRequestDTO.getKakaoName();
        String kakaoEmail = kakaoLoginRequestDTO.getKakaoEmail();

        if(memberRepository.existsByEmail(kakaoEmail)){
            throw new GeneralException(ErrorStatus.MEMBER_ALREADY_EXIST);
        }

        Member member = memberConverter.toKakaoMember(kakaoname,kakaoEmail);

        memberRepository.save(member);

        return buildLoginResponseDTO(member);

    }

    private LoginResponseDTO buildLoginResponseDTO(Member member) {
        return LoginResponseDTO.builder()
                .username(member.getUsername())
                .role(member.getRole())
                .isFirst(member.getIsFirst())
                .build();

    }

}
