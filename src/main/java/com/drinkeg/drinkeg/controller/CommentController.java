package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.CommentDTO.CommentRequestDTO;
import com.drinkeg.drinkeg.dto.CommentDTO.CommentResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.service.commentService.CommentService;
import com.drinkeg.drinkeg.service.memberService.MemberService;
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
    public ApiResponse<List<CommentResponseDTO>> getCommentsByPartyId(
            @PathVariable("partyId") Long partyId) {

        List<CommentResponseDTO> comments = commentService.getCommentsByPartyId(partyId);

        return ApiResponse.onSuccess(comments);
    }


    // 모임 댓글 생성
    @PostMapping
    public ApiResponse<String> createComment(
            @AuthenticationPrincipal PrincipalDetail principalDetail,
            @RequestBody CommentRequestDTO commentRequest) {

        commentService.createComment(principalDetail, commentRequest);

        return ApiResponse.onSuccess("댓글 생성 완료");
    }




    // 댓글 삭제 (대댓글 O)
    @PatchMapping("/{commentId}")
    public ApiResponse<String> updateCommentStatus(
            @AuthenticationPrincipal PrincipalDetail principalDetail,
            @PathVariable("commentId") Long commentId) {

        commentService.updateCommentStatus(principalDetail, commentId);

        return ApiResponse.onSuccess("댓글 삭제 완료");
    }


    // 댓글 삭제 (대댓글 X)
    @DeleteMapping("/{commentId}")
    public ApiResponse<String> deleteComment(
            @AuthenticationPrincipal PrincipalDetail principalDetail,
            @PathVariable("commentId") Long commentId) {

        commentService.deleteComment(principalDetail, commentId);

        return ApiResponse.onSuccess("댓글 삭제 완료");
    }



}
