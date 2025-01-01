package com.drinkeg.drinkeg.domain.member.login.oauth2.apple.appleService;

import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.domain.member.converter.MemberConverter;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.login.oauth2.dto.LoginResponseDTO;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.member.login.oauth2.apple.AppleLoginDTO.AppleLoginRequestDTO;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import com.drinkeg.drinkeg.domain.member.login.oauth2.apple.fegin.AppleAuthClient;
import com.drinkeg.drinkeg.global.security.jwt.JWTUtil;
import com.drinkeg.drinkeg.infra.redis.RedisClient;
import com.drinkeg.drinkeg.global.security.jwt.TokenService;
import com.drinkeg.drinkeg.domain.member.login.oauth2.apple.utils.ApplePublicKeyGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppleLoginService {

    private final TokenService tokenService;
    private final AppleAuthClient appleAuthClient;
    private final ApplePublicKeyGenerator applePublicKeyGenerator;
    private final MemberRepository memberRepository;
    private final RedisClient redisClient;
    private final JWTUtil jwtUtil;
    private final MemberConverter memberConverter;


    public LoginResponseDTO appleLogin(AppleLoginRequestDTO appleLoginRequestDTO, HttpServletResponse response)throws AuthenticationException, NoSuchAlgorithmException, InvalidKeySpecException,
            JsonProcessingException {

        if(appleLoginRequestDTO.getIdentityToken() == null){
            throw new GeneralException(ErrorStatus.IDENTITY_TOKEN_NOT_FOUND);
        }

        System.out.println("--------------apple Login Start---------------");

        String identityToken = appleLoginRequestDTO.getIdentityToken();
        Claims claims = getClaimsFromIdentityToken(identityToken);

        // 회원 가입 된 사용자인지 확인하기
        String username = "apple "+ claims.getSubject();
        Optional<Member> existData = memberRepository.findByUsername(username);

        Member member;

        if (existData.isEmpty()){

            member = Member.createOAuthMember(username, (String) claims.get("email"),"Apple");
            memberRepository.save(member);
            System.out.println("첫 로그인임");
            tokenService.jwtProvider(member, response);

        }
        else{

            member = existData.get();
            member.updateEmail(claims.get("email", String.class));
            System.out.println("첫 로그인아님");
            memberRepository.save(member);
            tokenService.jwtProvider(member, response);

        }

        return buildLoginResponseDTO(member);
    }


    private LoginResponseDTO buildLoginResponseDTO(Member member) {
        return LoginResponseDTO.builder()
                .username(member.getUsername())
                .role(member.getRole())
                .isFirst(member.getIsFirst())
                .build();

    }

    private Claims getClaimsFromIdentityToken(String identityToken) throws InvalidKeySpecException, JsonProcessingException,AuthenticationException, NoSuchAlgorithmException{

        // identity Token에서 헤더 추출
        Map<String, String> appleTokenHeader = tokenService.parseHeaders(identityToken);

        // 애플 서버에서 publicKey 받아온 후에 identity 토큰의 헤더와 일치하는 publicKey 만들기
        PublicKey publicKey = applePublicKeyGenerator.generatePublicKey(appleTokenHeader,
                appleAuthClient.getAppleAuthPublicKey());

        // identity Token에서 claims 추출
        return tokenService.getTokenClaims(identityToken, publicKey);



    }

}
