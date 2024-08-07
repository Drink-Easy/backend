package com.drinkeg.drinkeg.converter;

import com.drinkeg.drinkeg.domain.Comment;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.Recomment;
import com.drinkeg.drinkeg.dto.RecommentRequestDTO;
import com.drinkeg.drinkeg.dto.RecommentResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class RecommentConverter {

    public Recomment fromRequest(RecommentRequestDTO recommentRequest, Comment comment, Member member) {
        return Recomment.builder()
                .comment(comment)
                .member(member)
                .content(recommentRequest.getContent())
                .build();
    }

    public RecommentResponseDTO toResponse(Recomment recomment) {
        return RecommentResponseDTO.builder()
                .id(recomment.getId())
                .commentId(recomment.getComment().getId())
                .memberId(recomment.getMember().getId())
                .content(recomment.getContent())
                .build();
    }
}
