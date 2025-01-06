package com.drinkeg.drinkeg.domain.wineNote.repository;

import com.drinkeg.drinkeg.domain.wine.domain.WineNoteStatics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WineNoteRepository extends JpaRepository<WineNoteStatics, Long>, WineNoteRepositoryCustom {

}