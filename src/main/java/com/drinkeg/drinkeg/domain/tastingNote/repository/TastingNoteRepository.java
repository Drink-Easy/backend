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



    @Query("SELECT new com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNoteSortCountResponse( " +
            "COUNT(t), " +
            "SUM(CASE WHEN t.wine.sort = '레드' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN t.wine.sort = '화이트' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN t.wine.sort = '스파클링' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN t.wine.sort = '로제' THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN t.wine.sort NOT IN ('레드', '화이트', '스파클링', '로제') THEN 1 ELSE 0 END) " +
            ") " +
            "FROM TastingNote t " +
            "WHERE t.member.username = :username")
    TastingNoteSortCountResponse findTastingNoteSortCountsByUsername(@Param("username") String username);

}
