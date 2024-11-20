package com.drinkeg.drinkeg.wineNote.repository;

import com.drinkeg.drinkeg.wineNote.domain.WineNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WineNoteRepository extends JpaRepository<WineNote, Long>, WineNoteRepositoryCustom {

}