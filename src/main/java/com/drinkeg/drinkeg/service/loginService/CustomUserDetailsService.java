package com.drinkeg.drinkeg.service.loginService;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.securityDTO.jwtDTO.PrincipalDetail;
import com.drinkeg.drinkeg.dto.securityDTO.oauth2DTO.UserDTO;
import com.drinkeg.drinkeg.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> existData = memberRepository.findByUsername(username);
        Member userData = existData.get();

        UserDTO userDTO = UserDTO.builder()
                .username(userData.getUsername())
                .password(userData.getPassword())
                .role(userData.getRole())
                .build();

        if (userDTO != null) {

            return new PrincipalDetail(userDTO);
        }

        return null;
    }
}