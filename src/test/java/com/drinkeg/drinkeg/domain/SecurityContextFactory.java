package com.drinkeg.drinkeg.domain;

import com.drinkeg.drinkeg.MockMember;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.UserDTO;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class SecurityContextFactory implements WithSecurityContextFactory<MockMember> {

        @Override
        public SecurityContext createSecurityContext(MockMember mockMember) {
            SecurityContext context = SecurityContextHolder.createEmptyContext();

            UserDTO userDTO = UserDTO.builder()
                    .username(mockMember.username())
                    .password(mockMember.password())
                    .role(mockMember.role())
                    .build();

            PrincipalDetail principalDetail = new PrincipalDetail(userDTO);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principalDetail, null, principalDetail.getAuthorities());

            context.setAuthentication(authentication);
            return context;
        }
}
