package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.domain.Comment;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.RecommentDTO.RecommentRequestDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.service.commentService.CommentService;
import com.drinkeg.drinkeg.service.recommentService.RecommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.drinkeg.drinkeg.service.memberService.MemberService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/recomments")
public class RecommentController {

    private final RecommentService recommentService;
    private final CommentService commentService;
    // 대댓글 생성
    @PostMapping("/{commentId}")
    public ApiResponse<String> createRecomment(
            @AuthenticationPrincipal PrincipalDetail principalDetail,
            @PathVariable("commentId") Long commentId,
            @RequestBody RecommentRequestDTO recommentRequest) {
        Comment comment = commentService.findByIdOrThrow(commentId);
        recommentService.createRecomment(comment, recommentRequest, principalDetail);

        return ApiResponse.onSuccess("대댓글 생성 완료");
    }


    // 대댓글 삭제
    @DeleteMapping("/{recommentId}")
    public ApiResponse<String> deleteRecomment(
            @AuthenticationPrincipal PrincipalDetail principalDetail,
            @PathVariable("recommentId") Long recommentId) {

        recommentService.deleteRecomment(principalDetail, recommentId);

        return ApiResponse.onSuccess("댓글 삭제 완료");
    }
}
