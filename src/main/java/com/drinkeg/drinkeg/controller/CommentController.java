package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.dto.CommentRequestDTO;
import com.drinkeg.drinkeg.dto.CommentResponseDTO;
import com.drinkeg.drinkeg.dto.RecommentRequestDTO;
import com.drinkeg.drinkeg.dto.RecommentResponseDTO;
import com.drinkeg.drinkeg.service.commentService.CommentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    //@GetMapping

    // 모임 댓글 생성
    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponseDTO>> createComment(
            @RequestBody CommentRequestDTO commentRequest) {
        try {
            CommentResponseDTO createdComment = commentService.createComment(commentRequest);
            ApiResponse<CommentResponseDTO> response = ApiResponse.onSuccess(createdComment);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (EntityNotFoundException ex) {
            ErrorStatus errorStatus = ErrorStatus.MEMBER_NOT_FOUND;
            if (ex.getMessage().contains("회원이 없습니다.")) {
                errorStatus = ErrorStatus.MEMBER_NOT_FOUND; // MEMBER_NOT_FOUND 에러 처리
            } else if (ex.getMessage().contains("모임이 없습니다.")) {
                errorStatus = ErrorStatus.PARTY_NOT_FOUND; // PARTY_NOT_FOUND 에러 처리
            }
            ApiResponse<CommentResponseDTO> response = ApiResponse.onFailure(
                    errorStatus.getCode(),
                    errorStatus.getMessage(),
                    null
            );
            return ResponseEntity.status(errorStatus.getHttpStatus()).body(response);
        }
    }



    // 대댓글 생성
    @PostMapping("/{commentId}/recomments")
    public ResponseEntity<ApiResponse<RecommentResponseDTO>> createRecomment(
            @PathVariable("commentId") Long commentId,
            @RequestBody RecommentRequestDTO recommentRequest) {
        try {
            RecommentResponseDTO createdRecomment = commentService.createRecomment(commentId, recommentRequest);
            ApiResponse<RecommentResponseDTO> response = ApiResponse.onSuccess(createdRecomment);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (EntityNotFoundException ex) {
            ErrorStatus errorStatus = ErrorStatus.COMMENT_NOT_FOUND;
            if (ex.getMessage().contains("댓글이 없습니다.")) {
                errorStatus = ErrorStatus.COMMENT_NOT_FOUND;  // COMMENT_NOT_FOUND 대댓글이 달릴 댓글이 없을 때
            } else if (ex.getMessage().contains("회원이 없습니다.")) {
                errorStatus = ErrorStatus.MEMBER_NOT_FOUND; // MEMBER_NOT_FOUND 에러 처리
            }
            ApiResponse<RecommentResponseDTO> response = ApiResponse.onFailure(
                    errorStatus.getCode(),
                    errorStatus.getMessage(),
                    null
            );
            return ResponseEntity.status(errorStatus.getHttpStatus()).body(response);
        }
    }


    // 댓글 삭제 (대댓글 O)
    @PatchMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> updateCommentStatus(@PathVariable("commentId") Long commentId) {
        try {
            commentService.updateCommentStatus(commentId);
            ApiResponse<Void> response = ApiResponse.onSuccess(null);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException ex) {
            ErrorStatus errorStatus = ErrorStatus.COMMENT_NOT_FOUND;
            ApiResponse<Void> response = ApiResponse.onFailure(
                    errorStatus.getCode(),
                    errorStatus.getMessage(),
                    null
            );
            return ResponseEntity.status(errorStatus.getHttpStatus()).body(response);
        } catch (IllegalStateException ex) {
            ErrorStatus errorStatus = ErrorStatus.COMMENT_HAS_NO_RECOMMENTS;
            ApiResponse<Void> response = ApiResponse.onFailure(
                    errorStatus.getCode(),
                    errorStatus.getMessage(),
                    null
            );
            return ResponseEntity.status(errorStatus.getHttpStatus()).body(response);
        }
    }

    // 댓글 삭제 (대댓글 X)
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable("commentId") Long commentId) {
        try {
            commentService.deleteComment(commentId);
            ApiResponse<Void> response = ApiResponse.onSuccess(null);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException ex) {
            ErrorStatus errorStatus = ErrorStatus.COMMENT_NOT_FOUND;
            ApiResponse<Void> response = ApiResponse.onFailure(
                    errorStatus.getCode(),
                    errorStatus.getMessage(),
                    null
            );
            return ResponseEntity.status(errorStatus.getHttpStatus()).body(response);
        } catch (IllegalStateException ex) {
            ErrorStatus errorStatus = ErrorStatus.COMMENT_HAS_RECOMMENTS;
            ApiResponse<Void> response = ApiResponse.onFailure(
                    errorStatus.getCode(),
                    errorStatus.getMessage(),
                    null
            );
            return ResponseEntity.status(errorStatus.getHttpStatus()).body(response);
        }
    }

    // 대댓글 삭제
    @DeleteMapping("/{commentId}/recomments/{recommentId}")
    public ResponseEntity<ApiResponse<Void>> deleteRecomment(@PathVariable("commentId") Long commentId, @PathVariable("recommentId") Long recommentId) {
        try {
            commentService.deleteRecomment(commentId, recommentId);
            ApiResponse<Void> response = ApiResponse.onSuccess(null);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException ex) {
            ErrorStatus errorStatus = ErrorStatus.RECOMMENT_NOT_FOUND;
            ApiResponse<Void> response = ApiResponse.onFailure(errorStatus.getCode(), errorStatus.getMessage(), null);
            return ResponseEntity.status(errorStatus.getHttpStatus()).body(response);
        }
    }

}
