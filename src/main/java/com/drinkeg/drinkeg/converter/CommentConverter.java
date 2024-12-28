package com.drinkeg.drinkeg.converter;

import com.drinkeg.drinkeg.domain.comment.domain.Comment;
import com.drinkeg.drinkeg.domain.comment.dto.CommentRequestDTO;
import com.drinkeg.drinkeg.domain.comment.dto.CommentResponseDTO;
import com.drinkeg.drinkeg.domain.member.domain.Member;

import com.drinkeg.drinkeg.domain.party.domain.Party;
import org.springframework.stereotype.Component;

@Component
public class CommentConverter {

    public Comment toEntity(CommentRequestDTO commentRequest, Party party, Member member) {
        return Comment.builder()
                .party(party)
                .member(member)
                .content(commentRequest.getContent())
                .isDeleted(false) // 기본값 설정
                .build();
    }

    public CommentResponseDTO toResponse(Comment comment) {
        return CommentResponseDTO.builder()
                .id(comment.getId())
                .partyId(comment.getParty().getId())
                .memberName(comment.getMember().getUsername())
                .content(comment.getContent())
                .isDeleted(comment.isDeleted())
                .build();
    }

    public Comment setDeleted(Comment comment) {
        comment = Comment.builder()
                .id(comment.getId())
                .party(comment.getParty())
                .member(comment.getMember())
                .content(comment.getContent())
                .isDeleted(true)
                .build();
        return comment;
    }
}

