package com.drinkeg.drinkeg.domain.member.service;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.dto.MemberInfoResponse;
import com.drinkeg.drinkeg.domain.member.dto.MemberUpdateRequest;
import com.drinkeg.drinkeg.domain.member.enums.Provider;
import com.drinkeg.drinkeg.domain.member.enums.Role;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.tastingNote.dto.request.TastingNoteRequest;
import com.drinkeg.drinkeg.domain.tastingNote.repository.TastingNoteRepository;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class MemberServiceTest extends IntegrationTestSupport {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    TastingNoteRepository tastingNoteRepository;

    @Autowired
    WineRepository wineRepository;

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
//                        MemberInfoResponse::getCity,     // memberInfoResponse.getCity() 호출
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
//                        MemberInfoResponse::getCity,     // memberInfoResponse.getCity() 호출
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
                        null                );
    }


    @DisplayName("성공적으로 회원을 삭제한다.")
    @Test
    void deleteMemberByUsername(){

        // given
        Member member =  memberRepository.save(createMember("user1", "윤다영", "55azaz@naver.com", "서울", "Drinkeg", true," https://drinkeg-bucket-1.s3.ap-northeast-2.amazonaws.com/member/profile/70af4bd2-71"));

        // when
        memberService.deleteMemberByUsername(member.getUsername());

        // then
        Optional<Member> deletedMember = memberRepository.findByUsername(member.getUsername());
        assertThat(deletedMember).isEmpty();

    }
    @DisplayName("존재하지 않는 username으로 삭제 시 GeneralException을 반환한다")
    @Test
    void deleteMemberByUsername_ThrowsException() {
        // given
        memberRepository.save(createMember("user1", "윤다영", "55azaz@naver.com", "서울", "Drinkeg", true," https://drinkeg-bucket-1.s3.ap-northeast-2.amazonaws.com/member/profile/70af4bd2-717c-48d8-93d5-1a563de091a2"));

        // when & then
        assertThatThrownBy(() -> memberService.deleteMemberByUsername("user2"))
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorStatus.MEMBER_NOT_FOUND.getMessage());
    }


    @DisplayName("회원 정보 업데이트를 성공적으로 수행한다.")
    @Test
    void updateMemberInfo_Success() {

        // given
        Member member = memberRepository.save(createMember("user1","주민영","44azaz@naver.com","광주","Kakao", true ,null));
        MemberUpdateRequest updateRequest = new MemberUpdateRequest("newName");

        // when
        memberService.updateMemberInfo(updateRequest, member.getUsername());

        // then
        Optional<Member> OptimalMember = memberRepository.findByUsername(member.getUsername());

        Member updatedMember = OptimalMember.get();
        assertThat(updatedMember)
                .extracting(Member::getName, Member::getRegion)
                .containsExactly(updateRequest.getName());
    }

    @DisplayName("존재하지 않는 회원의 정보를 업데이트하려고 하면 예외가 발생한다.")
    @Test
    void updateMemberInfo_ThrowsException() {

        // given
        String username = "nonexistentUser";
        MemberUpdateRequest updateRequest = new MemberUpdateRequest("newName");

        // when & then
        assertThatThrownBy(() -> memberService.updateMemberInfo(updateRequest,username))
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorStatus.MEMBER_NOT_FOUND.getMessage());
    }

    @DisplayName("memberUpdateRequest에 값이 없을 경우 정보가 업데이트 되지 않는다.")
    @Test
    void updateMemberInfo_is_null_Test() {
        // given
        Member member = memberRepository.save(createMember("user1","주민영","44azaz@naver.com","광주","Kakao", true ,null));
        MemberUpdateRequest updateRequest = new MemberUpdateRequest("newName");

        // when
        memberService.updateMemberInfo(updateRequest, member.getUsername());

        // then
        Optional<Member> OptimalMember = memberRepository.findByUsername(member.getUsername());
        Member updatedMember = OptimalMember.get();
        assertThat(updatedMember)
                .extracting(Member::getName, Member::getRegion)
                .containsExactly(updateRequest.getName(), "광주");
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

    private TastingNoteRequest createTastingNoteRequest(Wine wine) {
        List<String> noseList = List.of("오렌지", "시트러스", "건포도", "흙", "아몬드");

        return TastingNoteRequest.builder()
                .wineId(wine.getId())
                .color("레드")
                .tasteDate(LocalDate.parse("2025-01-01"))
                .sweetness(10)
                .acidity(10)
                .tannin(10)
                .body(10)
                .alcohol(10)
                .nose(noseList)
                .rating(4.5f)
                .review("좋아요").build();
    }
}
