package com.drinkeg.drinkeg.wineNote.dao;

import com.drinkeg.drinkeg.wineNote.domain.WineNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WineNoteRepository extends JpaRepository<WineNote, Long>, WineNoteRepositoryCustom {

}