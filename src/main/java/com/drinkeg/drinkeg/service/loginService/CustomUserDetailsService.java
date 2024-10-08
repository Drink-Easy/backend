package com.drinkeg.drinkeg.service.loginService;

import com.drinkeg.drinkeg.converter.MemberConverter;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.UserDTO;
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

        UserDTO userDTO = MemberConverter.toUserDTO(userData);

        if (userDTO != null) {

            return new PrincipalDetail(userDTO);
        }

        return null;
    }
}