package com.drinkeg.drinkeg.domain.wineNote.repository;

import com.drinkeg.drinkeg.domain.wineNote.domain.WineNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WineNoteRepository extends JpaRepository<WineNote, Long>, WineNoteRepositoryCustom {

}