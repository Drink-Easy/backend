package com.drinkeg.drinkeg.domain.member.service;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.dto.JoinRequest;
import com.drinkeg.drinkeg.domain.member.dto.MemberRequest;
import com.drinkeg.drinkeg.domain.member.dto.MemberResponseDTO;
import com.drinkeg.drinkeg.domain.member.dto.UsernameCheckRequest;
import com.drinkeg.drinkeg.domain.member.enums.Role;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.drinkeg.drinkeg.domain.member.domain.Member.createMember;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
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
        JoinRequest joinRequest = createJoinRequest("itsme", "22azaz1234", "22azaz1234");

        //when
        joinService.join(joinRequest);

        //then
        Optional<Member> savedMember = memberRepository.findByUsername("itsme");
        Member member = savedMember.get();
        assertThat(member.getUsername()).isEqualTo("itsme");
        assertThat(member.getPassword()).isNotEqualTo("22azaz1234");//bCryptPasswordEncoder는 매번 다른 값을 생성
        assertThat(member.getEmail()).isEqualTo("itsme");
        assertThat(member.isAdult()).isFalse();


    }

    @Test
    @DisplayName("중복된 사용자 이름으로 회원가입 시 예외를 반환한다.")
    void joinMember_DuplicateUsername() {
        // Given
        memberRepository.save(createMember("itsme", "notPassword",false));
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


    @Test
    @DisplayName("정상적으로 회원 정보를 업데이트한다.")
    void addMemberDetailSuccess() {

        // Given
        memberRepository.save(createMember("itsme", "Password1234",true));
        List<String> wineSort = Arrays.asList("Red", "White");
        List<String> wineArea = Arrays.asList("France", "Italy");
        List<String> wineVariety = Arrays.asList("Cabernet Sauvignon", "Chardonnay");
        MemberRequest memberRequest = createMemberRequest("윤다영", true, 10000L,wineSort, wineArea, wineVariety,"서울");

        // When
        joinService.addMemberDetail(memberRequest, "itsme");

        // Then
        Optional<Member> OptionalMember = memberRepository.findByUsername("itsme");
        Member updateMember = OptionalMember.get();

        assertThat(updateMember)
                .extracting(
                        Member::getName,
                        Member::getRegion,
                        Member::getIsFirst,
                        Member::getWineSort,
                        Member::getWineArea,
                        Member::getWineVariety,
                        Member::getMonthPriceMax
                )
                .containsExactly(
                        "윤다영",
                        "서울",
                        false,
                        Arrays.asList("Red", "White"),
                        Arrays.asList("France", "Italy"),
                        Arrays.asList("Cabernet Sauvignon", "Chardonnay"),
                        10000L);

    }

    @Test
    @DisplayName("없는 회원이 member 정보를 업데이트하면 MEMBER_NOT_FOUND 에러가 발생한다.")
    void addMemberDetail_ThrowsException(){

        // Given
        List<String> wineSort = Arrays.asList("Red", "White");
        List<String> wineArea = Arrays.asList("France", "Italy");
        List<String> wineVariety = Arrays.asList("Cabernet Sauvignon", "Chardonnay");
        MemberRequest memberRequest = createMemberRequest("윤다영", true, 10000L,wineSort, wineArea, wineVariety,"서울");


        assertThatThrownBy(() -> joinService.addMemberDetail(memberRequest, "user1"))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.MEMBER_NOT_FOUND.getMessage());



    }

    @Test
    @DisplayName("이미 존재하는 이메일이 중복으로 확인된다.")
    void isDuplicatedUsername_True() {
        // Given
        String existingUsername = "itsme";
        memberRepository.save(createMember(existingUsername, "Password1234", true));

        UsernameCheckRequest usernameCheckRequest = new UsernameCheckRequest(existingUsername);

        // When
        boolean isDuplicated = joinService.isDuplicatedEmail(usernameCheckRequest);

        // Then
        AssertionsForClassTypes.assertThat(isDuplicated).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 이메일이 중복으로 확인되지 않는다.")
    void isDuplicatedUsername_False() {
        // Given
        String nonExistingUsername = "newUser";
        UsernameCheckRequest usernameCheckRequest = new UsernameCheckRequest(nonExistingUsername);

        // When
        boolean isDuplicated = joinService.isDuplicatedEmail(usernameCheckRequest);

        // Then
        AssertionsForClassTypes.assertThat(isDuplicated).isFalse();
    }


    private JoinRequest createJoinRequest(String username, String password, String rePassword){
        return JoinRequest.builder()
                .username(username)
                .password(password)
                .rePassword(rePassword)
                .build();


    }

    private MemberRequest createMemberRequest(String name, Boolean isNewbie, Long monthPrice, List<String> wineSort, List<String> wineArea, List<String> wineVariety, String region) {
        return MemberRequest.builder()
                .name(name)
                .isNewbie(isNewbie)
                .monthPrice(monthPrice)
                .wineSort(wineSort != null ? wineSort : new ArrayList<>())
                .wineArea(wineArea != null ? wineArea : new ArrayList<>())
                .wineVariety(wineVariety != null ? wineVariety : new ArrayList<>())
                .region(region)
                .build();
    }


}
