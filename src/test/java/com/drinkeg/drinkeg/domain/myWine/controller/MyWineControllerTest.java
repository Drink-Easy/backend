package com.drinkeg.drinkeg.domain.myWine.controller;

import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.UserDTO;
import com.drinkeg.drinkeg.domain.myWine.controller.request.MyWineRequest;
import com.drinkeg.drinkeg.domain.myWine.controller.request.MyWineUpdateRequest;
import com.drinkeg.drinkeg.domain.myWine.dto.response.MyWineResponse;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MyWineControllerTest extends MyWineControllerTestSupport {
    @BeforeEach
    void setUp() {
        PrincipalDetail principalDetail = new PrincipalDetail(UserDTO.builder().username("user").build());
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principalDetail, "password", principalDetail.getAuthorities())
        );
    }

    @DisplayName("보유 와인 추가 요청이 들어오면 보유 와인에 추가한다.")
    @Test
    void saveMyWine() throws Exception {
        // given
        MyWineRequest myWineRequest = createMyWineRequest(1L, LocalDate.parse("2025-01-01"), 100000);
        when(myWineService.saveMyWine(myWineRequest, "user"))
                .thenReturn(1L);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/my-wine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(myWineRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("COMMON200"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value("보유 와인 저장 성공"));
    }

    @DisplayName("와인 아이디 없이 보유 와인 추가 요청이 들어오면 예외가 발생한다.")
    @Test
    void saveMyWineWithoutWineId() throws Exception {
        // given
        MyWineRequest myWineRequest = createMyWineRequest(null, LocalDate.parse("2025-01-01"), 100000);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/my-wine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(myWineRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("와인 id는 null 일 수 없습니다."));
    }

    @DisplayName("구매 일자 없이 보유 와인 추가 요청이 들어오면 예외가 발생한다.")
    @Test
    void saveMyWineWithoutPurchaseDate() throws Exception {
        // given
        MyWineRequest myWineRequest = createMyWineRequest(1L, null, 100000);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/my-wine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(myWineRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("구매 날짜 입력은 필수입니다."));
    }

    @DisplayName("구매 가격 없이 보유 와인 추가 요청이 들어오면 정상적으로 저장한다.")
    @Test
    void saveMyWineWithoutPurchasePrice() throws Exception {
        // given
        MyWineRequest myWineRequest = createMyWineRequest(1L, LocalDate.parse("2025-01-01"), null);
        when(myWineService.saveMyWine(myWineRequest, "user"))
                .thenReturn(1L);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/my-wine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(myWineRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value("보유 와인 저장 성공"));
    }

    @DisplayName("없는 사용자에 대해 보유 와인 추가 요청이 들어오면 예외가 발생한다.")
    @Test
    void saveMyWineWithWrongUser() throws Exception {
        // given
        MyWineRequest myWineRequest = createMyWineRequest(1L, LocalDate.parse("2025-01-01"), 100000);
        when(myWineService.saveMyWine(refEq(myWineRequest), eq("user")))
                .thenThrow(new GeneralException(ErrorStatus.WINE_NOT_FOUND));

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/my-wine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(myWineRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("WINE4001"))
                .andExpect(jsonPath("$.message").value(ErrorStatus.WINE_NOT_FOUND.getMessage()));
    }

    @DisplayName("없는 와인에 대해 보유 와인 추가 요청이 들어오면 예외가 발생한다.")
    @Test
    void saveMyWineWithWrongWine() throws Exception {
        // given
        MyWineRequest myWineRequest = createMyWineRequest(-1L, LocalDate.parse("2025-01-01"), 100000);
        when(myWineService.saveMyWine(refEq(myWineRequest), eq("user")))
                .thenThrow(new GeneralException(ErrorStatus.WINE_NOT_FOUND));

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/my-wine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(myWineRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("WINE4001"))
                .andExpect(jsonPath("$.message").value(ErrorStatus.WINE_NOT_FOUND.getMessage()));
    }

    @DisplayName("보유 와인 조회 요청이 들어오면 해당 보유 와인을 반환한다.")
    @Test
    void getMyWine() throws Exception {
        // given
        Long myWineId = 1L;
        when(myWineService.getMyWineById(myWineId, "user"))
                .thenReturn(createMyWineResponse(myWineId, 1L, LocalDate.parse("2025-01-01"), 100000));

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.get("/my-wine/{myWineId}", myWineId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result.myWineId").value(myWineId))
                .andExpect(jsonPath("$.result.wineId").value(1L))
                .andExpect(jsonPath("$.result.purchaseDate").value("2025-01-01"))
                .andExpect(jsonPath("$.result.purchasePrice").value(100000));
    }

    @DisplayName("없는 보유 와인 조회 요청이 들어오면 예외가 발생한다.")
    @Test
    void getMyWineWithWrongMyWine() throws Exception {
        // given
        Long myWineId = -1L;
        when(myWineService.getMyWineById(myWineId, "user"))
                .thenThrow(new GeneralException(ErrorStatus.MY_WINE_NOT_FOUND));

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.get("/my-wine/{myWineId}", myWineId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("MY_WINE4001"))
                .andExpect(jsonPath("$.message").value(ErrorStatus.MY_WINE_NOT_FOUND.getMessage()));
    }

    @DisplayName("다른 사용자의 보유 와인 조회 요청을 하면 예외가 발생한다.")
    @Test
    void getMyWineWithUnauthorized() throws Exception {
        // given
        Long myWineId = 1L;
        when(myWineService.getMyWineById(myWineId, "user"))
                .thenThrow(new GeneralException(ErrorStatus.MY_WINE_UNAUTHORIZED));

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.get("/my-wine/{myWineId}", myWineId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("MY_WINE4002"))
                .andExpect(jsonPath("$.message").value(ErrorStatus.MY_WINE_UNAUTHORIZED.getMessage()));
    }

    @DisplayName("보유 와인 목록 조회 요청이 들어오면 전체 보유 와인을 반환한다.")
    @Test
    void getMyWineList() throws Exception {
        // given
        when(myWineService.getMyWinesByUsername("user"))
                .thenReturn(List.of(
                        createMyWineResponse(1L, 1L, LocalDate.parse("2025-01-01"), 100000),
                        createMyWineResponse(2L, 2L, LocalDate.parse("2025-01-02"), 200000)
                ));

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.get("/my-wine")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result[0].myWineId").value(1L))
                .andExpect(jsonPath("$.result[0].wineId").value(1L))
                .andExpect(jsonPath("$.result[0].purchaseDate").value("2025-01-01"))
                .andExpect(jsonPath("$.result[0].purchasePrice").value(100000))
                .andExpect(jsonPath("$.result[1].myWineId").value(2L))
                .andExpect(jsonPath("$.result[1].wineId").value(2L))
                .andExpect(jsonPath("$.result[1].purchaseDate").value("2025-01-02"))
                .andExpect(jsonPath("$.result[1].purchasePrice").value(200000));
    }

    @DisplayName("없는 사용자의 보유 와인 목록 조회 요청이 들어오면 예외가 발생한다.")
    @Test
    void getMyWineListWithWrongUser() throws Exception {
        // given
        when(myWineService.getMyWinesByUsername("user"))
                .thenThrow(new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.get("/my-wine")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("MEMBER4001"))
                .andExpect(jsonPath("$.message").value(ErrorStatus.MEMBER_NOT_FOUND.getMessage()));
    }

    @DisplayName("보유 와인 수정 요청이 들어오면 보유 와인을 수정한다.")
    @Test
    void updateMyWine() throws Exception {
        // given
        MyWineUpdateRequest myWineUpdateRequest = createMyWineUpdateRequest(LocalDate.parse("2025-01-01"), 100000);

        // when
        mockMvc.perform(MockMvcRequestBuilders.patch("/my-wine/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(myWineUpdateRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value("보유 와인 수정 완료"));

        // then
        verify(myWineService).updateMyWine(eq(1L), refEq(myWineUpdateRequest), eq("user"));
    }

    @DisplayName("없는 보유 와인 수정 요청이 들어오면 예외가 발생한다.")
    @Test
    void updateMyWineWithWrongMyWine() throws Exception {
        // given
        MyWineUpdateRequest myWineUpdate = createMyWineUpdateRequest(LocalDate.parse("2025-01-01"), 100000);
        doThrow(new GeneralException(ErrorStatus.MY_WINE_NOT_FOUND))
                .when(myWineService).updateMyWine(eq(1L), refEq(myWineUpdate), eq("user"));

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.patch("/my-wine/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(myWineUpdate))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("MY_WINE4001"))
                .andExpect(jsonPath("$.message").value(ErrorStatus.MY_WINE_NOT_FOUND.getMessage()));

    }

    @DisplayName("다른 사용자의 보유 와인 수정 요청을 하면 예외가 발생한다.")
    @Test
    void updateMyWineWithUnauthorized() throws Exception {
        // given
        MyWineUpdateRequest myWineUpdate = createMyWineUpdateRequest(LocalDate.parse("2025-01-01"), 100000);
        doThrow(new GeneralException(ErrorStatus.MY_WINE_UNAUTHORIZED))
                .when(myWineService).updateMyWine(eq(1L), refEq(myWineUpdate), eq("user"));

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.patch("/my-wine/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(myWineUpdate))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("MY_WINE4002"))
                .andExpect(jsonPath("$.message").value(ErrorStatus.MY_WINE_UNAUTHORIZED.getMessage()));
    }

    @DisplayName("보유 와인 삭제 요청이 들어오면 보유 와인을 삭제한다.")
    @Test
    void deleteMyWine() throws Exception {
        // given
        Long myWineId = 1L;

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/my-wine/{id}", myWineId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value("보유 와인 삭제 완료"));

        verify(myWineService).deleteMyWineById(eq(myWineId), eq("user"));
    }

    @DisplayName("없는 사용자의 보유 와인 삭제 요청이 들어오면 예외가 발생한다.")
    @Test
    void deleteMyWineWithWrongUser() throws Exception {
        // given
        Long myWineId = 1L;
        doThrow(new GeneralException(ErrorStatus.MEMBER_NOT_FOUND))
                .when(myWineService).deleteMyWineById(eq(myWineId), eq("user"));

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/my-wine/{id}", myWineId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("MEMBER4001"))
                .andExpect(jsonPath("$.message").value(ErrorStatus.MEMBER_NOT_FOUND.getMessage()));
    }

    @DisplayName("없는 보유 와인 삭제 요청이 들어오면 예외가 발생한다.")
    @Test
    void deleteMyWineWithWrongMyWine() throws Exception {
        // given
        Long myWineId = -1L;
        doThrow(new GeneralException(ErrorStatus.MY_WINE_NOT_FOUND))
                .when(myWineService).deleteMyWineById(eq(myWineId), eq("user"));

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/my-wine/{id}", myWineId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("MY_WINE4001"))
                .andExpect(jsonPath("$.message").value(ErrorStatus.MY_WINE_NOT_FOUND.getMessage()));
    }

    @DisplayName("다른 사용자의 보유 와인 삭제 요청을 하면 예외가 발생한다.")
    @Test
    void deleteMyWineWithUnauthorized() throws Exception {
        // given
        Long myWineId = 1L;
        doThrow(new GeneralException(ErrorStatus.MY_WINE_UNAUTHORIZED))
                .when(myWineService).deleteMyWineById(eq(myWineId), eq("user"));

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/my-wine/{id}", myWineId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("MY_WINE4002"))
                .andExpect(jsonPath("$.message").value(ErrorStatus.MY_WINE_UNAUTHORIZED.getMessage()));
    }

    private MyWineRequest createMyWineRequest(Long wineId, LocalDate purchaseDate, Integer purchasePrice) {
        if(purchasePrice == null) {
            return MyWineRequest.builder()
                    .wineId(wineId)
                    .purchaseDate(purchaseDate)
                    .build();
        }
        return MyWineRequest.builder()
                .wineId(wineId)
                .purchaseDate(purchaseDate)
                .purchasePrice(purchasePrice)
                .build();
    }

    private MyWineResponse createMyWineResponse(Long myWineId, Long wineId, LocalDate purchaseDate, Integer purchasePrice) {
        return MyWineResponse.builder()
                .myWineId(myWineId)
                .wineId(wineId)
                .purchaseDate(purchaseDate)
                .purchasePrice(purchasePrice)
                .build();
    }

    private MyWineUpdateRequest createMyWineUpdateRequest(LocalDate purchaseDate, Integer purchasePrice) {
        return MyWineUpdateRequest.builder()
                .purchaseDate(purchaseDate)
                .purchasePrice(purchasePrice)
                .build();
    }
}