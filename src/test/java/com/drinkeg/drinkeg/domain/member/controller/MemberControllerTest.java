package com.drinkeg.drinkeg.domain.member.controller;

import com.drinkeg.drinkeg.MockMember;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.dto.*;
import com.drinkeg.drinkeg.domain.member.enums.Role;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNoteWineSort;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;


import java.util.Arrays;
import java.util.List;

import static com.drinkeg.drinkeg.domain.member.enums.Role.ROLE_USER;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@AutoConfigureMockMvc(addFilters = false)
public class MemberControllerTest extends MemberControllerTestSupport{

    @DisplayName("회원가입 성공")
    @Test
    void joinProcess_Success() throws Exception {
        // given
        JoinRequest joinRequest = new JoinRequest("testUser", "password123@","password123@");

        doNothing().when(joinService).join(refEq(joinRequest));

        // when & then: API 호출 및 응답 검증
        mockMvc.perform(post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(joinRequest)) // 요청 데이터를 JSON으로 변환
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk()) // HTTP 상태 코드 200 검증
                .andExpect(jsonPath("$.code").value("COMMON200")) // 응답 코드 검증
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value("회원가입 성공"));
    }

    @DisplayName("회원 가입 시 username이 없으면 예외가 발생한다.")
    @Test
    void joinProcess_without_username() throws Exception {
        //given
        JoinRequest joinRequest = new JoinRequest(null, "password123@","password123@");
        //when //then
        mockMvc.perform(post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(joinRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Username은 필수입니다."));
    }

    @DisplayName("회원 가입 시 password가 없으면 예외가 발생한다.")
    @Test
    void joinProcess_without_password() throws Exception {
        //given
        JoinRequest joinRequest = new JoinRequest("user1", null,"password123@");
        //when //then
        mockMvc.perform(post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(joinRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Password는 필수입니다."));
    }
    @DisplayName("회원 가입 시 password에 특수문자가 없으면 예외가 발생한다.")
    @Test
    void joinProcess_invalid_password() throws Exception {
        //given
        JoinRequest joinRequest = new JoinRequest("user1", "password1","password1");
        //when //then
        mockMvc.perform(post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(joinRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Password는 최소 하나의 영문자, 숫자, 특수문자를 포함해야 합니다."));
    }

    @DisplayName("회원 가입 시 password에 숫자가 없으면 예외가 발생한다.")
    @Test
    void joinProcess_not_num_password() throws Exception {
        //given
        JoinRequest joinRequest = new JoinRequest("user1", "password@","password@");
        //when //then
        mockMvc.perform(post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(joinRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Password는 최소 하나의 영문자, 숫자, 특수문자를 포함해야 합니다."));
    }

    @DisplayName("회원 가입 시 password에 영문자가 없으면 예외가 발생한다.")
    @Test
    void joinProcess_not_Al_password() throws Exception {
        //given
        JoinRequest joinRequest = new JoinRequest("user1", "123@","123@");
        //when //then
        mockMvc.perform(post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(joinRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Password는 최소 하나의 영문자, 숫자, 특수문자를 포함해야 합니다."));
    }

    @DisplayName("존재하는 Username으로 회원가입 시 예외가 발생한다.")
    @Test
    void joinProcess_already_exist_username() throws Exception {

        //given
        JoinRequest joinRequest = new JoinRequest("existingUser", "password123@", "password123@");
        doThrow(new GeneralException(ErrorStatus.MEMBER_ALREADY_EXIST))
                .when(joinService).join(refEq(joinRequest));

        // when // then
        mockMvc.perform(post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(joinRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorStatus.MEMBER_ALREADY_EXIST.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorStatus.MEMBER_ALREADY_EXIST.getMessage()));


    }

    @DisplayName("회원 탈퇴 성공")
    @Test
    @MockMember
    void deleteProcess_Success() throws Exception {

        //given
        String username = "user";
        doNothing().when(memberService).deleteMemberByUsername(username);
        doNothing().when(tokenService).deleteRefreshTokenAndAccessToken(any(HttpServletResponse.class), eq(username));


        // when & then
        mockMvc.perform(delete("/member/delete")
                        .with(csrf())
                )
                .andDo(print()) // 요청과 응답 출력
                .andExpect(status().isOk()) // HTTP 상태 코드 200 검증
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value("회원 탈퇴 성공"));

    }

    @DisplayName("존재하지 않는 username으로 삭제 시 예외를 반환한다.")
    @Test
    @MockMember
    void deleteProcess_fail() throws Exception {

        //given
        String username = "user";
        doThrow(new GeneralException(ErrorStatus.MEMBER_NOT_FOUND))
                .when(memberService).deleteMemberByUsername(eq(username));

        // when & then
        mockMvc.perform(delete("/member/delete")
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorStatus.MEMBER_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorStatus.MEMBER_NOT_FOUND.getMessage()));

    }

    @DisplayName("사용자 초기 정보 추가 성공")
    @Test
    @MockMember
    void addMemberDetail_Success() throws Exception {
        // given
        List<String> wineSort = List.of("Red", "White");
        List<String> wineArea = List.of("France", "Italy");
        List<String> wineVariety = List.of("Cabernet", "Merlot");
        MemberRequest memberRequest = createMemberRequest(
                "testName",
                true,
                50000L,
                wineSort,
                wineArea,
                wineVariety,
                "testRegion"
        );


        String username = "user";
        MemberResponseDTO response =  createMemberResponse();
        when(joinService.addMemberDetail(refEq(memberRequest), eq(username))).thenReturn(response);

        // when & then
        mockMvc.perform(patch("/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk()) // HTTP 상태 코드 200 검증
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value("사용자 초기 정보 추가 완료"));

    }

    @DisplayName("존재하지 않는 username으로 정보 추가 시 예외를 반환한다.")
    @Test
    @MockMember
    void addMemberDetail_ThrowException() throws Exception {

        // given
        List<String> wineSort = List.of("Red", "White");
        List<String> wineArea = List.of("France", "Italy");
        List<String> wineVariety = List.of("Cabernet", "Merlot");
        MemberRequest memberRequest = createMemberRequest(
                "testName",
                true,
                50000L,
                wineSort,
                wineArea,
                wineVariety,
                "testRegion"
        );

        String username = "user";
        doThrow(new GeneralException(ErrorStatus.MEMBER_NOT_FOUND))
                .when(joinService).addMemberDetail(refEq(memberRequest),eq(username));

        // when & then
        mockMvc.perform(patch("/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("MEMBER4001"))
                .andExpect(jsonPath("$.message").value(ErrorStatus.MEMBER_NOT_FOUND.getMessage()));

    }


    @Test
    @DisplayName("마이페이지 정보를 성공적으로 가져온다.")
    @MockMember
    void getMemberInfo_Success() throws Exception {
        // given
        String username = "user";
        MemberInfoResponse memberInfoResponse = createMemberInfoResponse();

        when(memberService.showMemberInfo(eq(username))).thenReturn(memberInfoResponse);

        // when & then
        mockMvc.perform(get("/member/info")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result.imageUrl").value("http://test.image.url"))
                .andExpect(jsonPath("$.result.username").value("user"))
                .andExpect(jsonPath("$.result.email").value("user@test.com"))
                .andExpect(jsonPath("$.result.city").value("Seoul"))
                .andExpect(jsonPath("$.result.authType").value("ROLE_USER"))
                .andExpect(jsonPath("$.result.adult").value(false));


        // verify
        verify(memberService).showMemberInfo(username);
    }

    @Test
    @DisplayName("존재하지 않는 username으로 유저 정보 조회 시 예외를 반환한다.")
    @MockMember
    void getMemberInfo_ThrowException() throws Exception {
        // given
        String username = "user";
        when(memberService.showMemberInfo( eq(username)))
                .thenThrow(new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        // when & then
        mockMvc.perform(get("/member/info")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("MEMBER4001"))
                .andExpect(jsonPath("$.message").value(ErrorStatus.MEMBER_NOT_FOUND.getMessage()));

        // verify
        verify(memberService).showMemberInfo(username);
    }

    @Test
    @DisplayName("사용 가능한 닉네임으로 중복 검사 시에는 True를 반환한다.")
    @MockMember
    void checkNickname_Successful() throws Exception {
        // given
        String nickname = "uniqueNickname";
        when(memberService.isNicknameAvailable(eq(nickname))).thenReturn(true);

        // when & then
        mockMvc.perform(post("/member/{nickname}", nickname)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value(true));

        // verify
        verify(memberService).isNicknameAvailable(eq(nickname));
    }

    @Test
    @DisplayName("사용 불가능한 닉네임으로 중복 검사 시에는 False를 반환한다.")
    void checkNickname_Duplicate() throws Exception {
        // given
        String nickname = "duplicateNickname";
        when(memberService.isNicknameAvailable(eq(nickname))).thenReturn(false);

        // when & then
        mockMvc.perform(post("/member/{nickname}", nickname)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value(false));

        // verify
        verify(memberService).isNicknameAvailable(eq(nickname));
    }

    @Test
    @DisplayName("마이페이지 정보 수정 성공")
    @MockMember
    void updateMemberInfo_Success() throws Exception {
        // given
        String username = "user";

        MemberUpdateRequest updateRequest = createMemberUpdateRequest("newName","서울");
        doNothing().when(memberService).updateMemberInfo(refEq(updateRequest), eq(username));

        // when & then
        mockMvc.perform(patch("/member/info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value("정보 수정 성공"));

        // verify
        verify(memberService).updateMemberInfo(refEq(updateRequest), eq(username));
    }

    private Member createMember(String username, String password) {
        return Member.builder()
                .username(username)
                .password(password)
                .isFirst(false)
                .build();
    }

    private MemberRequest createMemberRequest(String name, Boolean isNewbie, Long monthPrice, List<String> wineSort,  List<String> wineArea,List<String> wineVariety,String region){
        return new MemberRequest(name,isNewbie,monthPrice,wineSort,wineArea,wineVariety,region);
    }

    private MemberUpdateRequest createMemberUpdateRequest(String name, String city){
        return new MemberUpdateRequest(name,city);
    }

    private MemberResponseDTO createMemberResponse(){
        return  MemberResponseDTO.builder()
                .id(1L)
                .name("testName")
                .username("user")
                .role(ROLE_USER)
                .isNewbie(true)
                .isFirst(true)
                .monthPriceMax(100000L)
                .wineSort(Arrays.asList("Red", "White"))
                .wineArea(Arrays.asList("France", "Italy"))
                .region("testRegion")
                .imageUrl("http://test.image.url")
                .build();
    }

    private MemberInfoResponse createMemberInfoResponse() {
        return MemberInfoResponse.builder()
                .imageUrl("http://test.image.url")
                .username("user")
                .email("user@test.com")
                .city("Seoul")
                .authType("ROLE_USER")
                .isAdult(false) // 성인 여부 설정
                .build();
    }

}
