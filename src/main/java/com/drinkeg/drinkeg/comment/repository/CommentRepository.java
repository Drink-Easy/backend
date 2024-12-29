package com.drinkeg.drinkeg.comment.repository;

import com.drinkeg.drinkeg.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    List<Comment> findByPartyId(Long partyId);

    //Optional<Long> countByPartyId(Long partyId);

}
