package com.drinkeg.drinkeg.domain.tastingNote.repository;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNoteNose;

import java.util.List;

public interface TastingNoteNoseRepositoryCustom {

    List<TastingNoteNose> getTastingNoteNoseListByUsername(String username);
}
