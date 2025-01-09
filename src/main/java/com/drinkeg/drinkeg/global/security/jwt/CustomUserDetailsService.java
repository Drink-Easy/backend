package com.drinkeg.drinkeg.global.security.jwt;

import com.drinkeg.drinkeg.domain.member.converter.MemberConverter;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.UserDTO;
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

        UserDTO userDTO = UserDTO.create(userData);

        if (userDTO != null) {

            return new PrincipalDetail(userDTO);
        }

        return null;
    }
}