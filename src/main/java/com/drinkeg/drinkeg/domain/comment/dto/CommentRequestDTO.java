package com.drinkeg.drinkeg.domain.comment.dto;

import com.drinkeg.drinkeg.domain.comment.domain.Comment;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.party.domain.Party;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequestDTO {
    private Long partyId;
    private String content;


    public static Comment toEntity(CommentRequestDTO commentRequest, Party party, Member member) {
        return Comment.create(member, party, commentRequest.getContent());
    }
}