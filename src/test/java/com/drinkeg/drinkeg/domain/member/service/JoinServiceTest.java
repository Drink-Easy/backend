package com.drinkeg.drinkeg.domain.member.service;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.dto.JoinRequest;
import com.drinkeg.drinkeg.domain.member.dto.MemberRequest;
import com.drinkeg.drinkeg.domain.member.dto.MemberResponseDTO;
import com.drinkeg.drinkeg.domain.member.enums.Role;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
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
        String username = "itsme";
        JoinRequest joinRequest = createJoinRequest(username, "22azaz1234", "22azaz1234");

        //when
        joinService.join(joinRequest);

        //then
        Optional<Member> savedMember = memberRepository.findByUsername("itsme");
        Member member = savedMember.get();
        assertThat(member)
                .extracting(
                        Member::getUsername,
                        Member::isAdult,
                        Member::getIsFirst)
                .containsExactly(
                        username,
                        false,
                        true
                );


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

    @DisplayName("회원 정보를 업데이트하고 응답 DTO를 반환한다.")
    @Test
    void addMemberDetail_Success() {
        // given
        String username = "itsme";
        memberRepository.save(createMember(username, "Password123@"));

        List<String> wineSort = List.of("Red", "White");
        List<String> wineArea = List.of("France", "Italy");
        List<String> wineVariety = List.of("Cabernet", "Merlot");
        MemberRequest memberRequest = createMemberRequest(
                "윤다영",
                true,
                50000L,
                wineSort,
                wineArea,
                wineVariety,
                "testRegion"
        );

        // when
        MemberResponseDTO responseDTO = joinService.addMemberDetail(memberRequest, username);

        // then
        Optional<Member> OptimalMember = memberRepository.findByUsername(username);
        Member updatedMember = OptimalMember.get();
        assertThat(updatedMember)
                .extracting(
                        Member::getName,
                        Member::getIsNewbie,  // 예시: 와인 애호가 여부
                        Member::getMonthPriceMax,
                        Member::getWineSort,
                        Member::getWineArea,
                        Member::getWineVariety,
                        Member::getRegion
                )
                .containsExactly(
                        "윤다영",
                        true,
                        50000L,
                        wineSort,
                        wineArea,
                        wineVariety,
                        "testRegion"
                );



    }
    @DisplayName("MemberRequest 속성 중 null 값이 있으면 업데이트 하지 않는다.")
    @Test
    void addMemberDetail_null() {
        // given
        String username = "itsme";
        memberRepository.save(createMember(username, "Password123@"));

        List<String> wineSort = List.of("Red", "White");
        List<String> wineArea = List.of("France", "Italy");
        List<String> wineVariety = List.of("Cabernet", "Merlot");
        MemberRequest memberRequest = createMemberRequest(
                null,
                true,
                50000L,
                wineSort,
                wineArea,
                wineVariety,
                "testRegion"
        );

        // when
        MemberResponseDTO responseDTO = joinService.addMemberDetail(memberRequest, username);

        // then
        Optional<Member> OptimalMember = memberRepository.findByUsername(username);
        Member updatedMember = OptimalMember.get();
        assertThat(updatedMember)
                .extracting(
                        Member::getName,
                        Member::getIsNewbie,  // 예시: 와인 애호가 여부
                        Member::getMonthPriceMax,
                        Member::getWineSort,
                        Member::getWineArea,
                        Member::getWineVariety,
                        Member::getRegion
                )
                .containsExactly(
                        null,
                        true,
                        50000L,
                        wineSort,
                        wineArea,
                        wineVariety,
                        "testRegion"
                );

    }


    @DisplayName("유저 정보 업데이트 시에 해당하는 username이 없으면 예외를 반환한다.")
    @Test
    void addMemberDetail_ThrowException() {
        // given
        String username = "itsme";

        List<String> wineSort = List.of("Red", "White");
        List<String> wineArea = List.of("France", "Italy");
        List<String> wineVariety = List.of("Cabernet", "Merlot");
        MemberRequest memberRequest = createMemberRequest(
                "윤다영",
                true,
                50000L,
                wineSort,
                wineArea,
                wineVariety,
                "testRegion"
        );


        // When & Then
        assertThatThrownBy(() -> joinService.addMemberDetail(memberRequest,username))
                .isInstanceOf(GeneralException.class)
                .hasMessageContaining(ErrorStatus.MEMBER_NOT_FOUND.getMessage());



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

    private MemberRequest createMemberRequest(String name, Boolean isNewbie, Long monthPrice, List<String> wineSort, List<String> wineArea, List<String> wineVariety, String region){
        return new MemberRequest(name,isNewbie,monthPrice,wineSort,wineArea,wineVariety,region);
    }

}
