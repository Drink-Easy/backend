package com.drinkeg.drinkeg.service.loginService;

import com.drinkeg.drinkeg.converter.MemberConverter;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.AppleLoginDTO.AppleLoginRequestDTO;
import com.drinkeg.drinkeg.dto.loginDTO.oauth2DTO.LoginResponse;
import com.drinkeg.drinkeg.fegin.AppleAuthClient;
import com.drinkeg.drinkeg.jwt.JWTUtil;
import com.drinkeg.drinkeg.redis.RedisClient;
import com.drinkeg.drinkeg.repository.MemberRepository;
import com.drinkeg.drinkeg.utils.ApplePublicKeyGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.ClaimAccessor;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import javax.print.DocFlavor;
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

    public LoginResponse appleLogin(AppleLoginRequestDTO appleLoginRequestDTO, HttpServletResponse response)throws AuthenticationException, NoSuchAlgorithmException, InvalidKeySpecException,
            JsonProcessingException {

        System.out.println("--------------apple Login Start---------------");

        String identityToken = appleLoginRequestDTO.getIdentityToken();
        Claims claims = getClaimsFromIdentityToken(identityToken);

        // 회원 가입 된 사용자인지 확인하기
        String username = "apple "+ claims.getSubject();
        Optional<Member> existData = memberRepository.findByUsername(username);

        Member member;

        if (existData.isEmpty()){

            member = memberConverter.toAppleMember(username, claims);
            memberRepository.save(member);
            System.out.println("첫 로그인임");
            jwtProvider(member, response);

        }
        else{

            member = existData.get();
            member.updateEmail(claims.get("email", String.class));
            System.out.println("첫 로그인아님");
            memberRepository.save(member);
            jwtProvider(member, response);

        }

        return buildLoginResponse(member);
    }

    public void jwtProvider(Member member, HttpServletResponse response) {

        String accessToken = jwtUtil.createJwt("access",member.getUsername(), member.getRole(), 60000000000L); // 임의로 10000배로 해놓았음. 나중에 수정 필요.
        String refreshToken = jwtUtil.createJwt("refresh",member.getUsername(), member.getRole(),864000000L);

        // 토큰을 쿠키에 저장하여 응답
        response.addCookie(tokenService.createCookie("accessToken", accessToken));
        response.addCookie(tokenService.createCookie("refreshToken", refreshToken));
        response.setStatus(HttpStatus.OK.value());

        // redis에 refresh 토큰 저장
        redisClient.setValue(member.getUsername(), refreshToken, 864000000L);
    }

    private LoginResponse buildLoginResponse(Member member) {
        return LoginResponse.builder()
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
