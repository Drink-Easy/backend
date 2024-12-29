package com.drinkeg.drinkeg.comment.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.comment.dto.CommentRequestDTO;
import com.drinkeg.drinkeg.comment.dto.CommentResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.comment.service.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    // 특정 모임의 댓글 및 대댓글 조회
    @GetMapping("/{partyId}")
    @Operation(summary = "모임의 댓글 및 대댓글 조회", description = "모임 id로 댓글 및 대댓글 조회 CommentResponseDTO")
    public ApiResponse<List<CommentResponseDTO>> getCommentsByPartyId(
            @PathVariable("partyId") Long partyId) {

        List<CommentResponseDTO> comments = commentService.getCommentsByPartyId(partyId);

        return ApiResponse.onSuccess(comments);
    }


    // 특정 모임의 댓글 및 대댓글 개수 조회
    @GetMapping("/count/{partyId}")
    @Operation(summary = "댓글 및 대댓글 개수 조회", description = "모임 id로 댓글 및 대댓글 개수(long) 조회")
    public ApiResponse<Long> countCommentsAndRecomments(@PathVariable("partyId") Long partyId) {

        long count = commentService.countCommentsAndRecommentsByPartyId(partyId);

        return ApiResponse.onSuccess(count); // 숫자를 반환하도록 수정
    }

    // 모임 댓글 생성
    @PostMapping
    @Operation(summary = "모임에 댓글 생성", description = "모임 id에 댓글 생성")
    public ApiResponse<String> createComment(
            @AuthenticationPrincipal PrincipalDetail principalDetail,
            @RequestBody CommentRequestDTO commentRequest) {

        commentService.createComment(principalDetail, commentRequest);

        return ApiResponse.onSuccess("댓글 생성 완료");
    }




    // 댓글 삭제 (대댓글 O)
    @PatchMapping("/{commentId}")
    @Operation(summary = "댓글 소프트 삭제", description = "댓글 id로 대댓글이 있는 댓글에 대해 소프트삭제(isDelete변경)")
    public ApiResponse<String> updateCommentStatus(
            @AuthenticationPrincipal PrincipalDetail principalDetail,
            @PathVariable("commentId") Long commentId) {

        commentService.updateCommentStatus(principalDetail, commentId);

        return ApiResponse.onSuccess("댓글 삭제 완료");
    }


    // 댓글 삭제 (대댓글 X)
    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 하드 삭제", description = "댓글 id로 하드삭제 - 대댓글이 없는 경우 사용")
    public ApiResponse<String> deleteComment(
            @AuthenticationPrincipal PrincipalDetail principalDetail,
            @PathVariable("commentId") Long commentId) {

        commentService.deleteComment(principalDetail, commentId);

        return ApiResponse.onSuccess("댓글 삭제 완료");
    }



}
