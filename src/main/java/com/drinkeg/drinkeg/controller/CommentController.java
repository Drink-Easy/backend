package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.CommentDTO.CommentRequestDTO;
import com.drinkeg.drinkeg.dto.CommentDTO.CommentResponseDTO;
import com.drinkeg.drinkeg.dto.RecommentDTO.RecommentRequestDTO;
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
    private final MemberService memberService;

    // 특정 모임의 댓글 및 대댓글 조회
    @GetMapping("/{partyId}")
    public ApiResponse<List<CommentResponseDTO>> getCommentsByPartyId(
            @PathVariable("partyId") Long partyId) {
        List<CommentResponseDTO> comments = commentService.getCommentsByPartyId(partyId);
        return ApiResponse.onSuccess(comments);
    }


    // 모임 댓글 생성
    @PostMapping
    public ApiResponse<String> createComment(//필요한게
            @AuthenticationPrincipal PrincipalDetail principalDetail,
            @RequestBody CommentRequestDTO commentRequest) {

        // 현재 로그인 한 사용자 정보 가져오기
        Member foundMember = memberService.loadMemberByPrincipleDetail(principalDetail);
        Long memberId = foundMember.getId();

        commentService.createComment(commentRequest, memberId);
        return ApiResponse.onSuccess("댓글 생성 완료");
    }



    // 대댓글 생성
    @PostMapping("/{commentId}/recomments")
    public ApiResponse<String> createRecomment(
            @AuthenticationPrincipal PrincipalDetail principalDetail,
            @PathVariable("commentId") Long commentId,
            @RequestBody RecommentRequestDTO recommentRequest) {
        // 현재 로그인한 사용자 정보 조회
        Member foundMember = memberService.loadMemberByPrincipleDetail(principalDetail);

        commentService.createRecomment(commentId, recommentRequest, foundMember);
        return ApiResponse.onSuccess("대댓글 생성 완료");
    }



    // 댓글 삭제 (대댓글 O)
    @PatchMapping("/{commentId}")
    public ApiResponse<String> updateCommentStatus(
            @AuthenticationPrincipal PrincipalDetail principalDetail,
            @PathVariable("commentId") Long commentId) {
        // 현재 로그인한 사용자 정보 가져오기
        Member foundMember = memberService.loadMemberByPrincipleDetail(principalDetail);

        commentService.updateCommentStatus(commentId, foundMember);
        return ApiResponse.onSuccess("댓글 삭제 완료");
    }


    // 댓글 삭제 (대댓글 X)
    @DeleteMapping("/{commentId}")
    public ApiResponse<String> deleteComment(
            @AuthenticationPrincipal PrincipalDetail principalDetail,
            @PathVariable("commentId") Long commentId) {
        // 현재 로그인한 사용자 정보 불러오기
        Member foundMember = memberService.loadMemberByPrincipleDetail(principalDetail);

        commentService.deleteComment(commentId, foundMember);
        return ApiResponse.onSuccess("댓글 삭제 완료");
    }


    // 대댓글 삭제
    @DeleteMapping("/{commentId}/recomments/{recommentId}")
    public ApiResponse<String> deleteRecomment(
            @AuthenticationPrincipal PrincipalDetail principalDetail,
            @PathVariable("commentId") Long commentId,
            @PathVariable("recommentId") Long recommentId) {
        // 현재 로그인한 사용자 정보
        Member foundMember = memberService.loadMemberByPrincipleDetail(principalDetail);

        commentService.deleteRecomment(commentId, recommentId, foundMember);
        return ApiResponse.onSuccess("댓글 삭제 완료");
    }

}
