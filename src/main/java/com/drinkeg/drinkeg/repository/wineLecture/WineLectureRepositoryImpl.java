package com.drinkeg.drinkeg.repository.wineLecture;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Transactional
public class WineLectureRepositoryImpl {
    private final JPAQueryFactory queryFactory;


}
