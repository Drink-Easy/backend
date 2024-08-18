package com.drinkeg.drinkeg.service.loginService;

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

    public LoginResponse appleLogin(AppleLoginRequestDTO appleLoginRequestDTO, HttpServletResponse response)throws AuthenticationException, NoSuchAlgorithmException, InvalidKeySpecException,
            JsonProcessingException {

        System.out.println("--------------apple Login Start---------------");

        String identityToken = appleLoginRequestDTO.getIdentityToken();

        // identity Token에서 헤더 추출
        final Map<String, String> appleTokenHeader = tokenService.parseHeaders(appleLoginRequestDTO.getIdentityToken());

        // 애플 서버에서 publicKey 받아온 후에 identity 토큰의 헤더와 일치하는 publicKey 만들기
        PublicKey publicKey = applePublicKeyGenerator.generatePublicKey(appleTokenHeader,
                appleAuthClient.getAppleAuthPublicKey());

        // identity Token에서 claims 추출
        Claims claims = tokenService.getTokenClaims(identityToken, publicKey);

        // 회원 가입 된 사용자인지 확인하기
        String username = "apple "+ claims.getSubject();
        Optional<Member> existData = memberRepository.findByUsername(username);


        if (existData.isEmpty()){

            Member member = Member.builder()
                    .username(username)
                    .email(claims.get("email", String.class))
                    .role("ROLE_USER")
                    .isFirst(true)
                    .build();
            memberRepository.save(member);

            System.out.println("첫 로그인임");

            String accessToken = jwtUtil.createJwt("access",member.getUsername(), member.getRole(), 60000000000L); // 임의로 10000배로 해놓았음. 나중에 수정 필요.
            String refreshToken = jwtUtil.createJwt("refresh",member.getUsername(), member.getRole(),864000000L);

            // 토큰을 쿠키에 저장하여 응답 (access 의 경우 추후 프론트와 협의하여 헤더에 넣어서 반환할 예정)
            response.addCookie(tokenService.createCookie("accessToken", accessToken));
            response.addCookie(tokenService.createCookie("refreshToken", refreshToken));
            response.setStatus(HttpStatus.OK.value());

            // redis에 refresh 토큰 저장
            redisClient.setValue(username, refreshToken, 864000000L);

            return LoginResponse.builder()
                    .username(username)
                    .role(member.getRole())
                    .isFirst(member.getIsFirst())
                    .build();

        }
        else{

            Member member = existData.get();
            member.updateEmail(claims.get("email", String.class));

            System.out.println("첫 로그인아님");
            memberRepository.save(member);

            String accessToken = jwtUtil.createJwt("access",member.getUsername(), member.getRole(), 60000000000L); // 임의로 10000배로 해놓았음. 나중에 수정 필요.
            String refreshToken = jwtUtil.createJwt("refresh",member.getUsername(), member.getRole(),864000000L);

            // 토큰을 쿠키에 저장하여 응답 (access 의 경우 추후 프론트와 협의하여 헤더에 넣어서 반환할 예정)
            response.addCookie(tokenService.createCookie("accessToken", accessToken));
            response.addCookie(tokenService.createCookie("refreshToken", refreshToken));
            response.setStatus(HttpStatus.OK.value());

            // redis에 refresh 토큰 저장
            redisClient.setValue(username, refreshToken, 864000000L);

            return LoginResponse.builder()
                    .username(username)
                    .role(member.getRole())
                    .isFirst(member.getIsFirst())
                    .build();
        }


    }

}
