package com.drinkeg.drinkeg.repository.wineLectureComplete;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Transactional
public class WineLectureCompleteRepositoryImpl implements WineLectureCompleteRepositoryCustom {
    private final EntityManager entityManager;



}
