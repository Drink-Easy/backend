package com.drinkeg.drinkeg.domain.tastingNote.repository;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNoteSortCountResponse;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TastingNoteRepository extends JpaRepository<TastingNote, Long>, TastingNoteRepositoryCustom {

    @Modifying
    @Query("UPDATE TastingNote t SET t.member = null WHERE t.member.username = :username")
    void updateTastingNoteMemberNull(@Param("username") String username);


    @Query("SELECT DISTINCT t " +
            "FROM TastingNote t " +
            "LEFT JOIN FETCH t.member m " +
            "LEFT JOIN FETCH t.wine w " +
            "LEFT JOIN FETCH t.noseList n " +
            "WHERE t.id = :tastingNoteId ")
    Optional<TastingNote> findTastingNoteWithWineAndNoseAndMemberById(
            @Param("tastingNoteId") Long tastingNoteId);


}
