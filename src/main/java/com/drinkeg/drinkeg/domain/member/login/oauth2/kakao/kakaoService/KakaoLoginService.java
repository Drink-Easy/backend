package com.drinkeg.drinkeg.domain.member.login.oauth2.kakao.kakaoService;


import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.domain.member.converter.MemberConverter;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.login.oauth2.dto.LoginResponseDTO;
import com.drinkeg.drinkeg.domain.member.login.oauth2.kakao.kakaoLoginDTO.KakaoLoginRequestDTO;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import com.drinkeg.drinkeg.jwt.TokenService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoLoginService {

    private final MemberRepository memberRepository;
    private final MemberConverter memberConverter;
    private final TokenService tokenService;

    public LoginResponseDTO kakaoLogin(KakaoLoginRequestDTO kakaoLoginRequestDTO, HttpServletResponse response){


        String kakaoname = null;
        String kakaoEmail = null;


        if (kakaoLoginRequestDTO != null) {
            kakaoname = kakaoLoginRequestDTO.getKakaoName();
            kakaoEmail = kakaoLoginRequestDTO.getKakaoEmail();
        } else {

            if (kakaoname == null) {
                throw new GeneralException(ErrorStatus.USERNAME_NOT_FOUND);
            }
            if (kakaoEmail == null) {
                throw new GeneralException(ErrorStatus.EMAIL_NOT_FOUND);
            }
        }

        Optional<Member> existData = memberRepository.findByUsername(kakaoname);

        Member member;

        if (existData.isEmpty()){

            member = memberConverter.toKakaoMember(kakaoname,kakaoEmail);
            memberRepository.save(member);
            System.out.println("첫 로그인임");
            tokenService.jwtProvider(member, response);

        }
        else{

            member = existData.get();
            System.out.println("첫 로그인아님");
            tokenService.jwtProvider(member, response);

        }

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
