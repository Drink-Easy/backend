package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.Recomment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecommentRepository extends JpaRepository<Recomment, Long> {

    boolean existsByCommentId(Long commentId);

    Optional<Recomment> findByIdAndCommentId(Long recommentId, Long commentId);

}
