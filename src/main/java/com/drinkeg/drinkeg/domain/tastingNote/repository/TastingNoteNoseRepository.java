package com.drinkeg.drinkeg.domain.tastingNote.repository;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNoteNose;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TastingNoteNoseRepository extends JpaRepository<TastingNoteNose, Long>, TastingNoteNoseRepositoryCustom {

    @Modifying
    @Query("DELETE TastingNoteNose tnn WHERE tnn.tastingNote.id = :tastingNoteId AND tnn.id = :id")
    void deleteByIdAndTastingNoteId(@Param Long tastingNoteId, @Param Long id);
}
