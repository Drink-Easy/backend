package com.drinkeg.drinkeg.wineLecture.repository;

import com.drinkeg.drinkeg.wineClass.domain.WineClass;
import com.drinkeg.drinkeg.wineLecture.domain.WineLecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WineLectureRepository extends JpaRepository<WineLecture, Long>, WineLectureRepositoryCustom {
    List<WineLecture> findByWineClass(WineClass wineClass);
}
