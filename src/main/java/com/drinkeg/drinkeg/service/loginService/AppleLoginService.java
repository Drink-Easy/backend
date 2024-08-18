package com.drinkeg.drinkeg.service.loginService;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.AppleLoginDTO.AppleLoginRequestDTO;
import com.drinkeg.drinkeg.dto.loginDTO.jwtDTO.PrincipalDetail;
import com.drinkeg.drinkeg.dto.loginDTO.oauth2DTO.LoginResponse;
import com.drinkeg.drinkeg.dto.loginDTO.oauth2DTO.UserDTO;
import com.drinkeg.drinkeg.fegin.AppleAuthClient;
import com.drinkeg.drinkeg.repository.MemberRepository;
import com.drinkeg.drinkeg.utils.ApplePublicKeyGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
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
    public LoginResponse appleLogin(AppleLoginRequestDTO appleLoginRequestDTO)throws AuthenticationException, NoSuchAlgorithmException, InvalidKeySpecException,
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

            Member member = new Member();
            member.setUsername(username);
            member.setEmail(claims.get("email", String.class));
            member.setRole("ROLE_USER");

            memberRepository.save(member);

        }
        else{

            Member member = existData.get();
            member.setUsername(username);

            memberRepository.save(member);
        }

        return LoginResponse.builder()
                .username(username)
                .role(existData.get().getRole())
                .isFirst(existData.get().getIsFirst())
                .build();

    }

}
