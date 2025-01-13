package com.drinkeg.drinkeg.domain.member.service;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.dto.MemberInfoResponse;
import com.drinkeg.drinkeg.domain.member.dto.MemberUpdateRequest;
import com.drinkeg.drinkeg.domain.member.enums.Provider;
import com.drinkeg.drinkeg.domain.member.enums.Role;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class MemberServiceTest extends IntegrationTestSupport {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @DisplayName("존재하는 username으로 사용자 정보를 불러온다.")
    @Test
    void findByUsernameTest(){

        //given
        Member member1 = memberRepository.save(createMember("user1", "윤다영","55azaz@naver.com","서울","Drinkeg", true,"https://drinkeg-bucket-1.s3.ap-northeast-2.amazonaws.com/member/profile/70af4bd2-717c-48d8-93d5-1a563de091a2" ));
        Member member2 = memberRepository.save(createMember("user2","주민영","44azaz@naver.com","광주","Kakao", true ,null));


        //when
        MemberInfoResponse memberInfoResponse = memberService.showMemberInfo("user1");

        //then
        assertThat(memberInfoResponse).isNotNull();
        assertThat(memberInfoResponse)
                .extracting(
                        MemberInfoResponse::getUsername, // memberInfoResponse.getUsername() 호출
                        MemberInfoResponse::getEmail,    // memberInfoResponse.getEmail() 호출
                        MemberInfoResponse::getCity,     // memberInfoResponse.getCity() 호출
                        MemberInfoResponse::getAuthType, // memberInfoResponse.getAuthType() 호출
                        MemberInfoResponse::isAdult,
                        MemberInfoResponse::getImageUrl
                )
                .containsExactly(
                        "윤다영",
                        "55azaz@naver.com",
                        "서울",
                        "Drinkeg",
                        true,
                        "https://drinkeg-bucket-1.s3.ap-northeast-2.amazonaws.com/member/profile/70af4bd2-717c-48d8-93d5-1a563de091a2"
                );
    }

    @DisplayName("존재하지 않는 username으로 조회 시 GeneralException을 반환한다")
    @Test
    void showMemberInfo_ThrowsException() {
        // given

        memberRepository.save(createMember("user1", "윤다영", "55azaz@naver.com", "서울", "Drinkeg", true," https://drinkeg-bucket-1.s3.ap-northeast-2.amazonaws.com/member/profile/70af4bd2-717c-48d8-93d5-1a563de091a2"));

        // when & then
        assertThatThrownBy(() -> memberService.showMemberInfo("user2"))
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorStatus.MEMBER_NOT_FOUND.getMessage());
    }

    @DisplayName("member의 해당 속성 값이 null일 경우 null을 그대로 반환한다.")
    @Test
    void memberInfoResponse_is_null_Test(){

        //given
        Member member2 = memberRepository.save(createMember("user1","주민영","44azaz@naver.com","광주","Kakao", true ,null));

        //when
        MemberInfoResponse memberInfoResponse = memberService.showMemberInfo("user1");

        //then
        assertThat(memberInfoResponse).isNotNull();
        assertThat(memberInfoResponse)
                .extracting(
                        MemberInfoResponse::getUsername, // memberInfoResponse.getUsername() 호출
                        MemberInfoResponse::getEmail,    // memberInfoResponse.getEmail() 호출
                        MemberInfoResponse::getCity,     // memberInfoResponse.getCity() 호출
                        MemberInfoResponse::getAuthType, // memberInfoResponse.getAuthType() 호출
                        MemberInfoResponse::isAdult,
                        MemberInfoResponse::getImageUrl
                )
                .containsExactly(
                        "주민영",
                        "44azaz@naver.com",
                        "광주",
                        "Kakao",
                        true,
                        null );
    }

    @Test
    @DisplayName("회원 정보가 정상적으로 업데이트된다.")
    void updateMemberInfo_success() {
        // Given
        String username = "itsme";
        Member existingMember = memberRepository.save(createMember("itsme","주민영","44azaz@naver.com","광주","Kakao", true ,null));


        MemberUpdateRequest updateRequest = new MemberUpdateRequest("윤다영", "서울");

        // When
        memberService.updateMemberInfo(updateRequest, username);

        // Then
        Member updatedMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new AssertionError("Member not found after update"));

        assertThat(updatedMember.getName()).isEqualTo("윤다영");
        assertThat(updatedMember.getRegion()).isEqualTo("서울");
    }

    @Test
    @DisplayName("존재하지 않는 회원 업데이트 시 MEMBER_NOT_FOUND 에러가 발생한다.")
    void updateMemberInfo_fail() {
        // Given
        String username = "nonexistent";
        MemberUpdateRequest updateRequest = new MemberUpdateRequest("윤다영", "서울");

        // When & Then
        assertThatThrownBy(() -> memberService.updateMemberInfo(updateRequest, username))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.MEMBER_NOT_FOUND.getMessage());
    }

    private Member createMember(
            String username,
            String name,
            String email,
            String city,
            String authType,
            boolean adult,
            String imageUrl
    ) {
        return Member.builder()
                .username(username)
                .name(name)
                .email(email)
                .region(city)
                .provider(Provider.fromValue(authType))
                .isAdult(adult)
                .role(Role.ROLE_USER)
                .isFirst(false)
                .imageUrl(imageUrl)
                .build();
    }
}
