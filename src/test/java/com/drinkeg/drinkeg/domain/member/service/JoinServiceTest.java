package com.drinkeg.drinkeg.domain.member.service;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.dto.JoinRequest;
import com.drinkeg.drinkeg.domain.member.enums.Role;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

public class JoinServiceTest extends IntegrationTestSupport {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JoinService joinService;

    @DisplayName("새로운 멤버 정보를 저장한다.")
    @Test
    void joinMember(){

        //given
        JoinRequest joinRequest = createJoinRequest("itsme", "22azaz1234", "22azaz1234");

        //when
        joinService.join(joinRequest);

        //then
        Optional<Member> savedMember = memberRepository.findByUsername("itsme");
        Member member = savedMember.get();
        assertThat(member.getUsername()).isEqualTo("itsme");
        assertThat(member.getPassword()).isNotEqualTo("22azaz1234"); //bCryptPasswordEncoder는 매번 다른 값을 생성
        assertThat(member.isAdult()).isFalse();


    }

    @Test
    @DisplayName("중복된 사용자 이름으로 회원가입 시 예외를 반환한다.")
    void joinMember_DuplicateUsername() {
        // Given
        memberRepository.save(createMember("itsme", "notPassword"));
        JoinRequest joinRequest = createJoinRequest("itsme", "22azaz1234", "22azaz1234");

        // When & Then
        assertThatThrownBy(() -> joinService.join(joinRequest))
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorStatus.MEMBER_ALREADY_EXIST.getMessage());
    }

    @Test
    @DisplayName("비밀번호와 비밀번호 확인이 일치하지 않을 경우 예외를 반환한다.")
    void joinMember_PasswordMismatch() {
        // Given
        JoinRequest joinRequest = createJoinRequest("itsme", "22azaz1234", "differentPassword");

        // When & Then
        assertThatThrownBy(() -> joinService.join(joinRequest))
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorStatus.PASSWORD_NOT_MATCH.getMessage());
    }

    private JoinRequest createJoinRequest(String username, String password, String rePassword){
        return JoinRequest.builder()
                .username(username)
                .password(password)
                .rePassword(rePassword)
                .build();


    }

    private Member createMember(String username,String password) {
        return Member.builder()
                .username(username)
                .password(password)
                .isFirst(false)
                .build();
    }

}
