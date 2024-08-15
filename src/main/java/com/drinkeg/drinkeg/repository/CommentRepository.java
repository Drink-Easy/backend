package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPartyId(Long partyId);
}
