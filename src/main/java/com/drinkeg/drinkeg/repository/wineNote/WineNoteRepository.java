package com.drinkeg.drinkeg.repository.wineNote;

import com.drinkeg.drinkeg.domain.WineNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WineNoteRepository extends JpaRepository<WineNote, Long>, WineNoteRepositoryCustom {

}