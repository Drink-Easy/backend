package com.drinkeg.drinkeg.tastingNote.repository;

import com.drinkeg.drinkeg.tastingNote.domain.TastingNoteNose;

import java.util.List;
import java.util.Optional;

public interface TastingNoteNoseRepositoryCustom {

    List<TastingNoteNose> getTastingNoteNoseListByUsername(String username);
}
