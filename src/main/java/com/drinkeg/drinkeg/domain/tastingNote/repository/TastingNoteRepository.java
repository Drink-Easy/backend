package com.drinkeg.drinkeg.domain.tastingNote.repository;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TastingNoteRepository extends JpaRepository<TastingNote, Long>, TastingNoteRepositoryCustom {
    @Modifying
    @Query("UPDATE TastingNote t SET t.member = null WHERE t.member.username = :username")
    void updateTastingNoteMemberNull(@Param("username") String username);

    @Query("SELECT t FROM TastingNote t WHERE t.wine.id = :wineId ORDER BY t.updatedAt DESC LIMIT 3")
    List<TastingNote> findRecentThreeTastingNoteBy(Long wineId);
}
