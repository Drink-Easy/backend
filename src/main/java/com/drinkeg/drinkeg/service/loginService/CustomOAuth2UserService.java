package com.drinkeg.drinkeg.service.loginService;

import com.drinkeg.drinkeg.converter.MemberConverter;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.UserDTO;
import com.drinkeg.drinkeg.dto.loginDTO.oauth2DTO.*;
import com.drinkeg.drinkeg.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;



    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);


        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        System.out.println("userRequest clientRegistration : " + userRequest.getClientRegistration());
        // token을 통해 응답받은 회원정보
        System.out.println("oAuth2User : " + oAuth2User);

        // 카카오 소셜 로그인
        if (registrationId.equals("kakao")) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        }
        else {

            return null;
        }

        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        Optional<Member> existData = memberRepository.findByUsername(username);

        if (existData.isEmpty()){

            Member userEntity = MemberConverter.toOauth2Member(username, oAuth2Response);

            memberRepository.save(userEntity);

            UserDTO userDTO = MemberConverter.toUserDTO(userEntity);

            return new PrincipalDetail(userDTO);
        }
        else{

            Member userEntity = existData.get();

            userEntity.updateEmail(oAuth2Response.getEmail());
            userEntity.updateName(oAuth2Response.getName());

            memberRepository.save(userEntity);

            UserDTO userDTO = MemberConverter.toUserDTO(userEntity);

            return new PrincipalDetail(userDTO);

        }

    }
}