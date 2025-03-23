package com.drinkeg.drinkeg.domain.tastingNote.controller;

import com.drinkeg.drinkeg.MockMember;
import com.drinkeg.drinkeg.domain.tastingNote.controller.request.TastingNoteRequest;
import com.drinkeg.drinkeg.domain.tastingNote.controller.request.TastingNoteUpdateRequest;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNoteNose;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNoteWineSort;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.AllTastingNoteResponse;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNotePreviewResponse;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNoteResponse;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNoteSortCountResponse;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.dto.PageResponse;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.*;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TastingNoteControllerTest extends TastingNoteControllerTestSupport {

    @DisplayName("테이스팅 노트를 저장한다.")
    @Test
    @MockMember
    void saveTastingNote() throws Exception {
        //given
        TastingNoteRequest tastingNoteRequest = createTastingNoteRequest(1L);
        when(tastingNoteService.saveTastingNote(refEq(tastingNoteRequest), eq("user")))
                .thenReturn(1L);

        //when //then
        mockMvc.perform(post("/tasting-note/new-note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tastingNoteRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value("테이스팅 노트 작성 완료"));
    }

    @DisplayName("없는 와인 아이디로 테이스팅 노트를 저장하면 예외가 발생한다.")
    @Test
    @MockMember
    void saveTastingNoteByWrongWineId() throws Exception {
        //given
        TastingNoteRequest tastingNoteRequest = createTastingNoteRequest(-1L);
        when(tastingNoteService.saveTastingNote(refEq(tastingNoteRequest), eq("user")))
                .thenThrow(new GeneralException(ErrorStatus.WINE_NOT_FOUND));

        //when //then
        mockMvc.perform(post("/tasting-note/new-note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tastingNoteRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("WINE4001"))
                .andExpect(jsonPath("$.message").value(ErrorStatus.WINE_NOT_FOUND.getMessage()));
    }

    @DisplayName("없는 사용자가 테이스팅 노트를 저장하면 예외가 발생한다.")
    @Test
    @MockMember
    void saveTastingNoteByWrongUser() throws Exception {
        //given
        TastingNoteRequest tastingNoteRequest = createTastingNoteRequest(1L);
        when(tastingNoteService.saveTastingNote(refEq(tastingNoteRequest), eq("user")))
                .thenThrow(new GeneralException(ErrorStatus.TASTING_NOTE_FORBIDDEN));

        //when //then
        mockMvc.perform(post("/tasting-note/new-note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tastingNoteRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("NOTE4002"))
                .andExpect(jsonPath("$.message").value(ErrorStatus.TASTING_NOTE_FORBIDDEN.getMessage()));
    }

    @DisplayName("테이스팅 노트 생성 시 와인 아이디가 없으면 예외가 발생한다.")
    @Test
    @MockMember
    void saveTastingNoteWithoutWineId() throws Exception {
        //given
        TastingNoteRequest tastingNoteRequest = createTastingNoteRequest(null);

        //when //then
        mockMvc.perform(post("/tasting-note/new-note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tastingNoteRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("와인 ID는 필수입니다."));
    }


    @DisplayName("테이스팅 노트 생성 시 색상이 없으면 예외가 발생한다.")
    @Test
    @MockMember
    void saveTastingNoteWithoutColor() throws Exception {
        //given
        TastingNoteRequest tastingNoteRequest = createTastingNoteRequestDetail(1L, null, LocalDate.parse("2025-01-01"),
                10, 10, 10, 10, 10, List.of("nose1", "nose2", "nose3"), 4F, "good");

        //when //then
        mockMvc.perform(post("/tasting-note/new-note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tastingNoteRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("색상 선택 필수입니다."));
    }

    @DisplayName("테이스팅 노트 생성 시 시음 날짜가 없으면 예외가 발생한다.")
    @Test
    @MockMember
    void saveTastingNoteWithoutTasteDate() throws Exception {
        //given
        TastingNoteRequest tastingNoteRequest = createTastingNoteRequestDetail(1L, "red", null,
                10, 10, 10, 10, 10, List.of("nose1", "nose2", "nose3"), 5F, "good");

        //when //then
        mockMvc.perform(post("/tasting-note/new-note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tastingNoteRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("시음 날짜는 필수입니다"));
    }

    @DisplayName("테이스팅 노트 생성 시 당도가 없으면 예외가 발생한다.")
    @Test
    @MockMember
    void saveTastingNoteWithoutSweetness() throws Exception {
        //given
        TastingNoteRequest tastingNoteRequest = createTastingNoteRequestDetail(1L, "red", LocalDate.parse("2025-01-01"),
                null, 10, 10, 10, 10, List.of("nose1", "nose2", "nose3"), 5F, "good");

        //when //then
        mockMvc.perform(post("/tasting-note/new-note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tastingNoteRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("당도 선택은 필수입니다."));
    }

    @DisplayName("테이스팅 노트 생성 시 당도 값이 0 미만이면 예외가 발생한다.")
    @Test
    @MockMember
    void saveTastingNoteWithSweetnessUnder0() throws Exception {
        //given
        TastingNoteRequest tastingNoteRequest = createTastingNoteRequestDetail(1L, "red", LocalDate.parse("2025-01-01"),
                -1, 10, 10, 10, 10, List.of("nose1", "nose2", "nose3"), 5F, "good");

        //when //then
        mockMvc.perform(post("/tasting-note/new-note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tastingNoteRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("당도는 0 이상 100 이하의 정수 값이어야 합니다."));
    }

    @DisplayName("테이스팅 노트 생성 시 당도 값이 100 초과이면 예외가 발생한다.")
    @Test
    @MockMember
    void saveTastingNoteWithSweetnessOver100() throws Exception {
        //given
        TastingNoteRequest tastingNoteRequest = createTastingNoteRequestDetail(1L, "red", LocalDate.parse("2025-01-01"),
                101, 10, 10, 10, 10, List.of("nose1", "nose2", "nose3"), 5F, "good");

        //when //then
        mockMvc.perform(post("/tasting-note/new-note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tastingNoteRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("당도는 0 이상 100 이하의 정수 값이어야 합니다."));
    }

    @DisplayName("테이스팅 노트 생성 시 당도, 산도, 탄닌, 바디, 알콜도 값이 모두 0이면 노트가 생성된다.")
    @Test
    @MockMember
    void saveTastingNoteWithAllZero() throws Exception {
        //given
        TastingNoteRequest tastingNoteRequest = createTastingNoteRequestDetail(1L, "red", LocalDate.parse("2025-01-01"),
                0, 0, 0, 0, 0, List.of("nose1", "nose2", "nose3"), 5F, "good");

        when(tastingNoteService.saveTastingNote(refEq(tastingNoteRequest), eq("user")))
                .thenReturn(1L);

        //when //then
        mockMvc.perform(post("/tasting-note/new-note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tastingNoteRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value("테이스팅 노트 작성 완료"));
    }

    @DisplayName("테이스팅 노트 생성 시 당도, 산도, 탄닌, 바디, 알콜도 값이 모두 100이면 노트가 생성된다.")
    @Test
    @MockMember
    void saveTastingNoteWithAllHundred() throws Exception {
        //given
        TastingNoteRequest tastingNoteRequest = createTastingNoteRequestDetail(1L, "red", LocalDate.parse("2025-01-01"),
                100, 100, 100, 100, 100, List.of("nose1", "nose2", "nose3"), 5F, "good");

        when(tastingNoteService.saveTastingNote(refEq(tastingNoteRequest), eq("user")))
                .thenReturn(1L);

        //when //then
        mockMvc.perform(post("/tasting-note/new-note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tastingNoteRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value("테이스팅 노트 작성 완료"));
    }

    @DisplayName("테이스팅 노트 생성 시 만족도가 없으면 예외가 발생한다.")
    @Test
    @MockMember
    void saveTastingNoteWithoutRating() throws Exception {
        //given
        TastingNoteRequest tastingNoteRequest = createTastingNoteRequestDetail(1L, "red", LocalDate.parse("2025-01-01"),
                10, 10, 10, 10, 10, List.of("nose1", "nose2", "nose3"), null, "good");

        //when //then
        mockMvc.perform(post("/tasting-note/new-note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tastingNoteRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("만족도 선택은 필수입니다."));
    }

    @DisplayName("테이스팅 노트 생성 시 만족도 값이 0 미만이면 예외가 발생한다.")
    @Test
    @MockMember
    void saveTastingNoteWithRatingUnder0() throws Exception {
        //given
        TastingNoteRequest tastingNoteRequest = createTastingNoteRequestDetail(1L, "red", LocalDate.parse("2025-01-01"),
                10, 10, 10, 10, 10, List.of("nose1", "nose2", "nose3"), -0.5F, "good");

        //when //then
        mockMvc.perform(post("/tasting-note/new-note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tastingNoteRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("만족도는 0 이상 5 이하의 실수 값이어야 합니다."));
    }

    @DisplayName("테이스팅 노트 생성 시 만족도 값이 5 초과이면 예외가 발생한다.")
    @Test
    @MockMember
    void saveTastingNoteWithRatingOver5() throws Exception {
        //given
        TastingNoteRequest tastingNoteRequest = createTastingNoteRequestDetail(1L, "red", LocalDate.parse("2025-01-01"),
                10, 10, 10, 10, 10, List.of("nose1", "nose2", "nose3"), 5.5F, "good");

        //when //then
        mockMvc.perform(post("/tasting-note/new-note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tastingNoteRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("만족도는 0 이상 5 이하의 실수 값이어야 합니다."));
    }

    @DisplayName("테이스팅 노트 생성 시 만족도 값이 5이면 노트가 생성된다.")
    @Test
    @MockMember
    void saveTastingNoteWithRatingEquals5() throws Exception {
        //given
        TastingNoteRequest tastingNoteRequest = createTastingNoteRequestDetail(1L, "red", LocalDate.parse("2025-01-01"),
                10, 10, 10, 10, 10, List.of("nose1", "nose2", "nose3"), 5F, "good");

        //when //then
        mockMvc.perform(post("/tasting-note/new-note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tastingNoteRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value("테이스팅 노트 작성 완료"));
    }

    @DisplayName("테이스팅 노트 생성 시 nose 리스트가 없어도 노트가 생성된다.")
    @Test
    @MockMember
    void saveTastingNoteWithoutNose() throws Exception {
        //given
        TastingNoteRequest tastingNoteRequest = createTastingNoteRequestDetail(1L, "red", LocalDate.parse("2025-01-01"),
                10, 10, 10, 10, 10, null, 5F, "good");

        when(tastingNoteService.saveTastingNote(refEq(tastingNoteRequest), eq("user")))
                .thenReturn(1L);

        //when //then
        mockMvc.perform(post("/tasting-note/new-note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tastingNoteRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value("테이스팅 노트 작성 완료"));
    }

    @DisplayName("테이스팅 노트 생성 시 리뷰가 없어도 노트가 생성된다.")
    @Test
    @MockMember
    void saveTastingNoteWithoutReview() throws Exception {
        //given
        TastingNoteRequest tastingNoteRequest = createTastingNoteRequestDetail(1L, "red", LocalDate.parse("2025-01-01"),
                10, 10, 10, 10, 10, List.of("nose1", "nose2", "nose3"), 5F, null);

        when(tastingNoteService.saveTastingNote(refEq(tastingNoteRequest), eq("user")))
                .thenReturn(1L);

        //when //then
        mockMvc.perform(post("/tasting-note/new-note")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tastingNoteRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value("테이스팅 노트 작성 완료"));
    }

    @DisplayName("없는 사용자가 전체 테이스팅 노트를 조회하면 예외가 발생한다.")
    @Test
    @MockMember
    void showAllTastingNoteByWrongUser() throws Exception {

        //given
        when(tastingNoteService.findAllTastingNote(eq(TastingNoteWineSort.of("전체")), eq("user"), any(Pageable.class)))
                .thenThrow(new GeneralException(ErrorStatus.TASTING_NOTE_FORBIDDEN));

        //when //then
        mockMvc.perform(get("/tasting-note/all")
                        .param("sort", "전체")
                )
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("NOTE4002"))
                .andExpect(jsonPath("$.message").value(ErrorStatus.TASTING_NOTE_FORBIDDEN.getMessage()));
    }

    @DisplayName("사용자의 모든 테이스팅 노트를 sort(전체) 기준으로 조회한다.")
    @Test
    @MockMember
    void showAllTastingNoteAll() throws Exception {
        // given
        String sort = "전체";
        when(tastingNoteService.findAllTastingNote(eq(TastingNoteWineSort.of(sort)), eq("user"), any(Pageable.class)))
                .thenReturn(AllTastingNoteResponse.builder()
                        .sortCount(new TastingNoteSortCountResponse(6, 1, 1, 1, 1, 2))
                        .pageResponse(new PageResponse<>(
                                List.of(
                                        createTastingNotePreviewResponse(1L, "2025-01-01", "wine", "레드", "2025-01-01"),
                                        createTastingNotePreviewResponse(2L, "2025-01-02", "wine2", "화이트", "2025-01-02"),
                                        createTastingNotePreviewResponse(3L, "2025-01-03", "wine3", "스파클링", "2025-01-03"),
                                        createTastingNotePreviewResponse(4L, "2025-01-04", "wine4", "로제", "2025-01-04"),
                                        createTastingNotePreviewResponse(5L, "2025-01-05", "wine5", "주정강화", "2025-01-05"),
                                        createTastingNotePreviewResponse(6L, "2025-01-06", "wine6", "기타", "2025-01-06")
                                ), 0, 1)).build()
                        );

        // when // then
        ResultActions resultActions = mockMvc.perform(get("/tasting-note/all")
                        .param("sort", sort)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"));
        assertJsonSortCount(resultActions, 6, 1, 1, 1, 1, 2);
        assertJsonNotePreviewList(resultActions, 0, 1L, "2025-01-01", "wine", "레드", "2025-01-01");
        assertJsonNotePreviewList(resultActions, 1, 2L, "2025-01-02", "wine2", "화이트", "2025-01-02");
        assertJsonNotePreviewList(resultActions, 2, 3L, "2025-01-03", "wine3", "스파클링", "2025-01-03");
        assertJsonNotePreviewList(resultActions, 3, 4L, "2025-01-04", "wine4", "로제", "2025-01-04");
        assertJsonNotePreviewList(resultActions, 4, 5L, "2025-01-05", "wine5", "주정강화", "2025-01-05");
        assertJsonNotePreviewList(resultActions, 5, 6L, "2025-01-06", "wine6", "기타", "2025-01-06");
    }

    @DisplayName("사용자의 모든 테이스팅 노트를 sort(레드) 기준으로 조회한다.")
    @Test
    @MockMember
    void showAllTastingNoteRed() throws Exception {
        // given
        String sort = "레드";
        when(tastingNoteService.findAllTastingNote(eq(TastingNoteWineSort.of(sort)), eq("user"), any(Pageable.class)))
                .thenReturn(AllTastingNoteResponse.builder()
                        .sortCount(new TastingNoteSortCountResponse(6, 1, 1, 1, 1, 2))
                        .pageResponse(new PageResponse<>(
                                List.of(
                                        createTastingNotePreviewResponse(1L, "2025-01-01", "wine", "레드", "2025-01-01")
                                ), 0, 1)).build()
                );

        // when // then
        ResultActions resultActions = mockMvc.perform(get("/tasting-note/all")
                        .param("sort", sort)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"));
        assertJsonSortCount(resultActions, 6, 1, 1, 1, 1, 2);
        assertJsonNotePreviewList(resultActions, 0, 1L, "2025-01-01","wine", "레드", "2025-01-01");
    }

    @DisplayName("사용자의 모든 테이스팅 노트를 sort(화이트) 기준으로 조회한다.")
    @Test
    @MockMember
    void showAllTastingNoteWhite() throws Exception {
        // given
        String sort = "화이트";
        when(tastingNoteService.findAllTastingNote(eq(TastingNoteWineSort.of(sort)), eq("user"), any(Pageable.class)))
                .thenReturn(AllTastingNoteResponse.builder()
                        .sortCount(new TastingNoteSortCountResponse(6, 1, 1, 1, 1, 2))
                        .pageResponse(new PageResponse<>(
                                List.of(
                                        createTastingNotePreviewResponse(2L, "2025-01-01", "wine2", "화이트", "2025-01-01")
                                ), 0, 1)).build()
                );

        // when // then
        ResultActions resultActions = mockMvc.perform(get("/tasting-note/all")
                        .param("sort", sort)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"));
        assertJsonSortCount(resultActions, 6, 1, 1, 1, 1, 2);
        assertJsonNotePreviewList(resultActions, 0, 2L, "2025-01-01", "wine2", "화이트", "2025-01-01");
    }

    @DisplayName("사용자의 모든 테이스팅 노트를 sort(스파클링) 기준으로 조회한다.")
    @Test
    @MockMember
    void showAllTastingNoteSparkling() throws Exception {
        // given
        String sort = "스파클링";
        when(tastingNoteService.findAllTastingNote(eq(TastingNoteWineSort.of(sort)), eq("user"), any(Pageable.class)))
                .thenReturn(AllTastingNoteResponse.builder()
                        .sortCount(new TastingNoteSortCountResponse(6, 1, 1, 1, 1, 2))
                        .pageResponse(new PageResponse<>(
                                List.of(
                                        createTastingNotePreviewResponse(3L, "2025-01-01", "wine3", "스파클링", "2025-01-01")
                                ), 0, 1)).build());

        // when // then
        ResultActions resultActions = mockMvc.perform(get("/tasting-note/all")
                        .param("sort", sort)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"));
        assertJsonSortCount(resultActions, 6, 1, 1, 1, 1, 2);
        assertJsonNotePreviewList(resultActions, 0, 3L, "2025-01-01","wine3", "스파클링", "2025-01-01");
    }

    @DisplayName("사용자의 모든 테이스팅 노트를 sort(로제) 기준으로 조회한다.")
    @Test
    @MockMember
    void showAllTastingNoteRose() throws Exception {
        // given
        String sort = "로제";
        when(tastingNoteService.findAllTastingNote(eq(TastingNoteWineSort.of(sort)), eq("user"), any(Pageable.class)))
                .thenReturn(AllTastingNoteResponse.builder()
                        .sortCount(new TastingNoteSortCountResponse(6, 1, 1, 1, 1, 2))
                        .pageResponse(new PageResponse<>(
                                List.of(
                                        createTastingNotePreviewResponse(4L, "2025-01-01","wine4", "로제", "2025-01-01")
                                ), 0, 1)).build());

        // when // then
        ResultActions resultActions = mockMvc.perform(get("/tasting-note/all")
                        .param("sort", sort)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"));
        assertJsonSortCount(resultActions, 6, 1, 1, 1, 1, 2);
        assertJsonNotePreviewList(resultActions, 0, 4L, "2025-01-01","wine4", "로제", "2025-01-01");
    }

    @DisplayName("사용자의 모든 테이스팅 노트를 sort(기타) 기준으로 조회한다.")
    @Test
    @MockMember
    void showAllTastingNoteEtc() throws Exception {
        // given
        String sort = "기타";
        when(tastingNoteService.findAllTastingNote(eq(TastingNoteWineSort.of(sort)), eq("user"), any(Pageable.class)))
                .thenReturn(AllTastingNoteResponse.builder()
                        .sortCount(new TastingNoteSortCountResponse(6, 1, 1, 1, 1, 2))
                        .pageResponse(new PageResponse<>(
                                List.of(
                                        createTastingNotePreviewResponse(5L, "2025-01-01","wine5", "주정강화", "2025-01-01"),
                                        createTastingNotePreviewResponse(6L, "2025-01-03","wine6", "기타", "2025-01-03")
                                ), 0, 1)).build());

        // when // then
        ResultActions resultActions = mockMvc.perform(get("/tasting-note/all")
                        .param("sort", sort)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"));
        assertJsonSortCount(resultActions, 6, 1, 1, 1, 1, 2);
        assertJsonNotePreviewList(resultActions, 0, 5L, "2025-01-01","wine5", "주정강화", "2025-01-01");
        assertJsonNotePreviewList(resultActions, 1, 6L, "2025-01-03","wine6", "기타", "2025-01-03");
    }

    @DisplayName("사용자의 모든 테이스팅 노트를 sort(전체) 기준으로 조회할 때 테이스팅 노트가 없으면 빈 리스트를 반환한다.")
    @Test
    @MockMember
    void showAllTastingNoteEmpty() throws Exception {
        // given
        String sort = "전체";
        when(tastingNoteService.findAllTastingNote(eq(TastingNoteWineSort.of(sort)), eq("user"), any(Pageable.class)))
                .thenReturn(AllTastingNoteResponse.builder()
                        .sortCount(new TastingNoteSortCountResponse(0, 0, 0, 0, 0, 0))
                        .pageResponse(new PageResponse<>(List.of(), 0, 0)).build());

        // when // then
        ResultActions resultActions = mockMvc.perform(get("/tasting-note/all")
                        .param("sort", "전체")
                )
                .andExpect(jsonPath("$.result.pageResponse.content").isEmpty())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result.pageResponse.content").isEmpty());
        assertJsonSortCount(resultActions, 0, 0, 0, 0, 0, 0);
    }

    @DisplayName("사용자의 특정 테이스팅 노트를 조회한다.")
    @Test
    @MockMember
    void showTastingNote() throws Exception {
        // given
        Long noteId = 1L;

        when(tastingNoteService.showTastingNoteByIdAndUsername(eq(noteId), eq("user")))
                .thenReturn(createTastingNoteResponse());

        // when // then
        mockMvc.perform(get("/tasting-note/{noteId}", noteId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result.noteId").value(1L))
                .andExpect(jsonPath("$.result.wineId").value(1L))
                .andExpect(jsonPath("$.result.wineName").value("wine"))
                .andExpect(jsonPath("$.result.sort").value("레드"))
                .andExpect(jsonPath("$.result.country").value("프랑스"))
                .andExpect(jsonPath("$.result.region").value("보르도"))
                .andExpect(jsonPath("$.result.imageUrl").value("https://DEFAULT_IMAGE_URL"))
                .andExpect(jsonPath("$.result.color").value("red"))
                .andExpect(jsonPath("$.result.tasteDate").value("2025-01-01"))
                .andExpect(jsonPath("$.result.sweetness").value(10))
                .andExpect(jsonPath("$.result.acidity").value(10))
                .andExpect(jsonPath("$.result.tannin").value(10))
                .andExpect(jsonPath("$.result.body").value(10))
                .andExpect(jsonPath("$.result.alcohol").value(10))
                .andExpect(jsonPath("$.result.noseList[0]").value("nose1"))
                .andExpect(jsonPath("$.result.noseList[1]").value("nose2"))
                .andExpect(jsonPath("$.result.noseList[2]").value("nose3"))
                .andExpect(jsonPath("$.result.rating").value(4.5F))
                .andExpect(jsonPath("$.result.review").value("good"));
    }

    @DisplayName("사용자의 특정 테이스팅 노트를 조회할 때 존재하지 않는 노트 아이디면 예외가 발생한다.")
    @Test
    @MockMember
    void showTastingNoteWithoutNoteId() throws Exception {
        // given
        Long noteId = -1L;
        when(tastingNoteService.showTastingNoteByIdAndUsername(eq(noteId), eq("user")))
                .thenThrow(new GeneralException(ErrorStatus.TASTING_NOTE_NOT_FOUND));


        // when // then
        mockMvc.perform(get("/tasting-note/{noteId}", noteId))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("NOTE4001"))
                .andExpect(jsonPath("$.message").value(ErrorStatus.TASTING_NOTE_NOT_FOUND.getMessage()));
    }

    @DisplayName("사용자의 특정 테이스팅 노트를 조회할 때 다른 사용자의 노트 아이디면 예외가 발생한다.")
    @Test
    @MockMember
    void showTastingNoteWithDifferentUsername() throws Exception {
        // given
        Long noteId = 1L;
        when(tastingNoteService.showTastingNoteByIdAndUsername(eq(noteId), eq("user")))
                .thenThrow(new GeneralException(ErrorStatus.TASTING_NOTE_FORBIDDEN));

        // when // then
        mockMvc.perform(get("/tasting-note/{noteId}", noteId))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("NOTE4002"))
                .andExpect(jsonPath("$.message").value(ErrorStatus.TASTING_NOTE_FORBIDDEN.getMessage()));
    }

    @DisplayName("사용자의 특정 테이스팅 노트를 수정한다.")
    @Test
    @MockMember
    void updateTastingNote() throws Exception {
        // given
        Long noteId = 1L;
        TastingNoteUpdateRequest tastingNoteUpdateRequest = createTastingNoteUpdateRequest("red", LocalDate.parse("2025-01-01"),
                10, 10, 10, 10, 10, List.of("nose1", "nose2", "nose3"), 5F, "good");

        // when // then
        mockMvc.perform(patch("/tasting-note/{noteId}", noteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tastingNoteUpdateRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value("노트 수정 완료"));

        verify(tastingNoteService).updateTastingNote(eq(noteId), refEq(tastingNoteUpdateRequest), eq("user"));
    }

    @DisplayName("사용자의 특정 테이스팅 노트를 수정할 때 존재하지 않는 노트 아이디면 예외가 발생한다.")
    @Test
    @MockMember
    void updateTastingNoteWithoutNoteId() throws Exception {
        // given
        Long noteId = -1L;
        TastingNoteUpdateRequest tastingNoteUpdateRequest = createTastingNoteUpdateRequest("red", LocalDate.parse("2025-01-01"),
                10, 10, 10, 10, 10, List.of("nose1", "nose2", "nose3"), 5F, "good");

        doThrow(new GeneralException(ErrorStatus.TASTING_NOTE_NOT_FOUND))
                .when(tastingNoteService).updateTastingNote(eq(noteId), refEq(tastingNoteUpdateRequest), eq("user"));

        // when // then
        mockMvc.perform(patch("/tasting-note/{noteId}", noteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tastingNoteUpdateRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("NOTE4001"))
                .andExpect(jsonPath("$.message").value(ErrorStatus.TASTING_NOTE_NOT_FOUND.getMessage()));

        verify(tastingNoteService).updateTastingNote(eq(noteId), refEq(tastingNoteUpdateRequest), eq("user"));
    }

    @DisplayName("사용자의 특정 테이스팅 노트를 수정할 때 다른 사용자의 노트 아이디면 예외가 발생한다.")
    @Test
    @MockMember
    void updateTastingNoteWithDifferentUsername() throws Exception {
        // given
        Long noteId = 1L;
        TastingNoteUpdateRequest tastingNoteUpdateRequest = createTastingNoteUpdateRequest("red", LocalDate.parse("2025-01-01"),
                10, 10, 10, 10, 10, List.of("nose1", "nose2", "nose3"), 5F, "good");

        doThrow(new GeneralException(ErrorStatus.TASTING_NOTE_FORBIDDEN))
                .when(tastingNoteService).updateTastingNote(eq(noteId), refEq(tastingNoteUpdateRequest), eq("user"));

        // when // then
        mockMvc.perform(patch("/tasting-note/{noteId}", noteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tastingNoteUpdateRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("NOTE4002"))
                .andExpect(jsonPath("$.message").value(ErrorStatus.TASTING_NOTE_FORBIDDEN.getMessage()));

        verify(tastingNoteService).updateTastingNote(eq(noteId), refEq(tastingNoteUpdateRequest), eq("user"));
    }

    @DisplayName("없는 사용자가 특정 테이스팅 노트를 수정하려고 하면 예외가 발생한다.")
    @Test
    @MockMember
    void updateTastingNoteWithNoUser() throws Exception {
        // given
        Long noteId = 1L;
        TastingNoteUpdateRequest tastingNoteUpdateRequest = createTastingNoteUpdateRequest("red", LocalDate.parse("2025-01-01"),
                10, 10, 10, 10, 10, List.of("nose1", "nose2", "nose3"), 5F, "good");

        doThrow(new GeneralException(ErrorStatus.MEMBER_NOT_FOUND))
                .when(tastingNoteService).updateTastingNote(eq(noteId), refEq(tastingNoteUpdateRequest), eq("user"));

        // when // then
        mockMvc.perform(patch("/tasting-note/{noteId}", noteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tastingNoteUpdateRequest))
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("MEMBER4001"))
                .andExpect(jsonPath("$.message").value(ErrorStatus.MEMBER_NOT_FOUND.getMessage()));

        verify(tastingNoteService).updateTastingNote(eq(noteId), refEq(tastingNoteUpdateRequest), eq("user"));
    }

    @DisplayName("사용자의 특정 테이스팅 노트를 삭제한다.")
    @Test
    @MockMember
    void deleteTastingNote() throws Exception {
        // given
        Long noteId = 1L;

        // when // then
        mockMvc.perform(delete("/tasting-note/{noteId}", noteId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.result").value("노트 삭제 완료"));

        verify(tastingNoteService).deleteTastingNote(eq(noteId), eq("user"));
    }

    @DisplayName("사용자의 특정 테이스팅 노트를 삭제할 때 존재하지 않는 노트 아이디면 예외가 발생한다.")
    @Test
    @MockMember
    void deleteTastingNoteWithoutNoteId() throws Exception {
        // given
        Long noteId = -1L;
        doThrow(new GeneralException(ErrorStatus.TASTING_NOTE_NOT_FOUND))
                .when(tastingNoteService).deleteTastingNote(eq(noteId), eq("user"));

        // when // then
        mockMvc.perform(delete("/tasting-note/{noteId}", noteId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("NOTE4001"))
                .andExpect(jsonPath("$.message").value(ErrorStatus.TASTING_NOTE_NOT_FOUND.getMessage()));

        verify(tastingNoteService).deleteTastingNote(eq(noteId), eq("user"));
    }

    @DisplayName("사용자의 특정 테이스팅 노트를 삭제할 때 다른 사용자의 노트 아이디면 예외가 발생한다.")
    @Test
    @MockMember
    void deleteTastingNoteWithDifferentUsername() throws Exception {
        // given
        Long noteId = 1L;
        doThrow(new GeneralException(ErrorStatus.TASTING_NOTE_FORBIDDEN))
                .when(tastingNoteService).deleteTastingNote(eq(noteId), eq("user"));

        // when // then
        mockMvc.perform(delete("/tasting-note/{noteId}", noteId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("NOTE4002"))
                .andExpect(jsonPath("$.message").value(ErrorStatus.TASTING_NOTE_FORBIDDEN.getMessage()));

        verify(tastingNoteService).deleteTastingNote(eq(noteId), eq("user"));
    }

    @DisplayName("없는 사용자가 특정 테이스팅 노트를 삭제하려고 하면 예외가 발생한다.")
    @Test
    @MockMember
    void deleteTastingNoteWithNoUser() throws Exception {
        // given
        Long noteId = 1L;
        doThrow(new GeneralException(ErrorStatus.MEMBER_NOT_FOUND))
                .when(tastingNoteService).deleteTastingNote(eq(noteId), eq("user"));

        // when // then
        mockMvc.perform(delete("/tasting-note/{noteId}", noteId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("MEMBER4001"))
                .andExpect(jsonPath("$.message").value(ErrorStatus.MEMBER_NOT_FOUND.getMessage()));

        verify(tastingNoteService).deleteTastingNote(eq(noteId), eq("user"));
    }

    @DisplayName("와인 이름으로 사용자의 테이스팅 노트를 검색한다.")
    @Test
    @MockMember
    void searchTastingNoteByWineName() throws Exception {
        // given
        String wineName = "와인";
        when(tastingNoteService.searchTastingNoteByWineName(eq(wineName), eq("user"), any(Pageable.class)))
                .thenReturn(new PageResponse(
                        List.of(createTastingNotePreviewResponse(6L, "2025-01-06", "와인6", "기타", "2025-01-06"),
                                createTastingNotePreviewResponse(5L, "2025-01-05", "와인5", "주정강화", "2025-01-05"),
                                createTastingNotePreviewResponse(4L, "2025-01-04", "와인4", "로제", "2025-01-04"),
                                createTastingNotePreviewResponse(3L, "2025-01-03", "와인3", "스파클링", "2025-01-03"),
                                createTastingNotePreviewResponse(2L, "2025-01-02", "와인2", "화이트", "2025-01-02"),
                                createTastingNotePreviewResponse(1L, "2025-01-01", "와인1", "레드", "2025-01-01")
                        ), 0, 1));

        // when // then
        ResultActions resultActions = mockMvc.perform(get("/tasting-note?searchName={searchName}", wineName))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"));
        assertJsonSearchNotePreviewList(resultActions, 0, 6L, "2025-01-06", "와인6", "기타", "2025-01-06");
        assertJsonSearchNotePreviewList(resultActions, 1, 5L, "2025-01-05", "와인5", "주정강화", "2025-01-05");
        assertJsonSearchNotePreviewList(resultActions, 2, 4L, "2025-01-04", "와인4", "로제", "2025-01-04");
        assertJsonSearchNotePreviewList(resultActions, 3, 3L, "2025-01-03", "와인3", "스파클링", "2025-01-03");
        assertJsonSearchNotePreviewList(resultActions, 4, 2L, "2025-01-02", "와인2", "화이트", "2025-01-02");
        assertJsonSearchNotePreviewList(resultActions, 5, 1L, "2025-01-01", "와인1", "레드", "2025-01-01");
    }

    @DisplayName("와인 이름으로 사용자의 테이스팅 노트를 검색할 때 테이스팅 노트가 없으면 빈 리스트를 반환한다.")
    @Test
    @MockMember
    void searchTastingNoteByWineNameEmpty() throws Exception {
        // given
        String wineName = "와인";
        when(tastingNoteService.searchTastingNoteByWineName(eq(wineName), eq("user"), any(Pageable.class)))
                .thenReturn(new PageResponse<>(List.of(), 0, 0));

        // when // then
        mockMvc.perform(get("/tasting-note?searchName={searchName}", wineName))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.content").isEmpty())
                .andExpect(jsonPath("$.code").value("COMMON200"))
                .andExpect(jsonPath("$.message").value("OK"));
    }


    TastingNoteRequest createTastingNoteRequest(Long wineId) {
        return new TastingNoteRequest(wineId, "red", LocalDate.parse("2025-01-01"), 10, 10, 10, 10, 10, List.of("nose1", "nose2", "nose3"), 5F, "good");
    }

    TastingNoteRequest createTastingNoteRequestDetail(Long wineId, String color, LocalDate tasteDate,
                                                Integer sweetness, Integer acidity, Integer tannin, Integer body, Integer alcohol,
                                                List<String> noseList, Float rating, String review) {
        return new TastingNoteRequest(wineId, color, tasteDate, sweetness, acidity, tannin, body, alcohol, noseList, rating, review);
    }

    TastingNotePreviewResponse createTastingNotePreviewResponse(Long noteId, String tasteDate, String wineName, String sort, String createdAt) {
        return TastingNotePreviewResponse.builder()
                .noteId(noteId)
                .tasteDate(LocalDate.parse(tasteDate))
                .wineName(wineName)
                .imageUrl("https://DEFAULT_IMAGE_URL")
                .sort(sort)
                .createdAt(LocalDate.parse(createdAt))
                .build();
    }

    private TastingNoteResponse createTastingNoteResponse(){
        return TastingNoteResponse.builder()
                .noteId(1L)
                .wineId(1L)
                .wineName("wine")
                .sort("레드")
                .country("프랑스")
                .region("보르도")
                .variety("피노누아")
                .imageUrl("https://DEFAULT_IMAGE_URL")
                .color("red")
                .tasteDate(LocalDate.parse("2025-01-01"))
                .sweetness(10)
                .acidity(10)
                .tannin(10)
                .body(10)
                .alcohol(10)
                .noseList(List.of(TastingNoteNose.builder().noseElement("nose1").build(),
                        TastingNoteNose.builder().noseElement("nose2").build(),
                        TastingNoteNose.builder().noseElement("nose3").build()))
                .rating(4.5F)
                .review("good")
                .createdAt(LocalDate.parse("2025-01-01"))
                .build();
    }

    private TastingNoteUpdateRequest createTastingNoteUpdateRequest(String color, LocalDate tasteDate,
                                                                    Integer sweetness, Integer acidity, Integer tannin, Integer body, Integer alcohol,
                                                                    List<String> noseList, Float rating, String review) {
        return new TastingNoteUpdateRequest(color, tasteDate, sweetness, acidity, tannin, body, alcohol, noseList, rating, review);
    }

    // Helper methods for repeated assertions
    private void assertJsonSortCount(ResultActions resultActions, int totalCount, int redCount, int whiteCount, int sparklingCount, int roseCount, int etcCount) throws Exception {
        resultActions.andExpect(jsonPath("$.result.sortCount.totalCount").value(totalCount))
                .andExpect(jsonPath("$.result.sortCount.redCount").value(redCount))
                .andExpect(jsonPath("$.result.sortCount.whiteCount").value(whiteCount))
                .andExpect(jsonPath("$.result.sortCount.sparklingCount").value(sparklingCount))
                .andExpect(jsonPath("$.result.sortCount.roseCount").value(roseCount))
                .andExpect(jsonPath("$.result.sortCount.etcCount").value(etcCount));
    }

    private void assertJsonNotePreviewList(ResultActions resultActions, int index, long noteId, String tasteDate, String wineName, String sort, String createdAt) throws Exception {
        resultActions.andExpect(jsonPath("$.result.pageResponse.content[" + index + "].noteId").value(noteId))
                .andExpect(jsonPath("$.result.pageResponse.content[" + index + "].tasteDate").value(tasteDate))
                .andExpect(jsonPath("$.result.pageResponse.content[" + index + "].wineName").value(wineName))
                .andExpect(jsonPath("$.result.pageResponse.content[" + index + "].sort").value(sort))
                .andExpect(jsonPath("$.result.pageResponse.content[" + index + "].imageUrl").value("https://DEFAULT_IMAGE_URL"))
                .andExpect(jsonPath("$.result.pageResponse.content[" + index + "].createdAt").value(createdAt));
    }

    private void assertJsonSearchNotePreviewList(ResultActions resultActions, int index, long noteId, String tasteDate, String wineName, String sort, String createdAt) throws Exception {
        resultActions.andExpect(jsonPath("$.result.content[" + index + "].noteId").value(noteId))
                .andExpect(jsonPath("$.result.content[" + index + "].tasteDate").value(tasteDate))
                .andExpect(jsonPath("$.result.content[" + index + "].wineName").value(wineName))
                .andExpect(jsonPath("$.result.content[" + index + "].sort").value(sort))
                .andExpect(jsonPath("$.result.content[" + index + "].imageUrl").value("https://DEFAULT_IMAGE_URL"))
                .andExpect(jsonPath("$.result.content[" + index + "].createdAt").value(createdAt));
    }

}
