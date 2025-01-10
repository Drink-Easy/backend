package com.drinkeg.drinkeg.domain.member.repository;


import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.enums.Role;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MemberRepositoryTest extends IntegrationTestSupport {

    @Autowired
    MemberRepository memberRepository;

    @DisplayName("username을 통해서 존재하는 멤버를 조회한다.")
    @Test
    void findByUsernameTest(){

        //given
        Member member1 = memberRepository.save(createMember("user1","윤따" ));
        Member member2 = memberRepository.save(createMember("user2","찐따"));

        //when
        Optional<Member> optionalMember = memberRepository.findByUsername("user2");
        Member foundMember = optionalMember.get();

        //then
        assertThat(foundMember.getId()).isEqualTo(member2.getId());

    }

    @DisplayName("존재하지 않는 username으로 조회했을 경우 empty 값을 반환한다.")
    @Test
    void findByNoExistUsernameTest(){

        //given
        Member member1 = memberRepository.save(createMember("user1","윤따" ));
        Member member2 = memberRepository.save(createMember("user2","찐따"));

        //when
        Optional<Member> optionalMember = memberRepository.findByUsername("user3");

        //then
        assertThat(optionalMember).isEmpty();

    }

    @DisplayName("존재하는 username으로 조회 했을 때 유저가 존재하면 true를 반환한다.")
    @Test
    void existByusernameTest(){

        //given
        Member member1 = memberRepository.save(createMember("user1","윤따" ));

        //when
        Boolean result = memberRepository.existsByUsername("user1");

        //then
        assertThat(result).isTrue();

    }

    @DisplayName("존재하지 않는 username으로 조회 했을 때 유저가 존재하면 true를 반환한다.")
    @Test
    void notExistByusernameTest(){

        //given
        Member member1 = memberRepository.save(createMember("user1","윤따" ));

        //when
        Boolean result = memberRepository.existsByUsername("user2");

        //then
        assertThat(result).isFalse();

    }

    @DisplayName("존재하는 username으로 멤버를 삭제한다")
    @Test
    void deleteByUsername_existingUserTest() {

        // given
        Member member = memberRepository.save(createMember("user1", "윤따"));
        assertThat(memberRepository.existsByUsername("user1")).isTrue();


        // when
        memberRepository.deleteByUsername("user1");

        // then
        assertThat(memberRepository.existsByUsername("user1")).isFalse();
    }





    private Member createMember(String username,String name) {
        return Member.builder()
                .username(username)
                .name(name)
                .role(Role.ROLE_USER)
                .isFirst(false)
                .build();
    }
}
