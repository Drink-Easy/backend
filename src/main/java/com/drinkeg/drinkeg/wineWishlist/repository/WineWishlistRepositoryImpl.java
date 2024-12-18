package com.drinkeg.drinkeg.wineWishlist.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.drinkeg.drinkeg.wineWishlist.domain.QWineWishlist.wineWishlist;


@Repository
@RequiredArgsConstructor
@Transactional
public class WineWishlistRepositoryImpl implements WineWishlistRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public Boolean existsByMemberIdAndWineId(Long memberId, Long wineId) {
        // wineWishlist에 해당하는 데이터가 존재하면 true, 존재하지 않으면 false
        Optional<Boolean> isLiked = Optional.ofNullable(
                queryFactory
                        .select(wineWishlist.id.isNotNull()) // wineWishlist가 존재하는지 여부를 체크
                        .from(wineWishlist)
                        .where(wineWishlist.wine.id.eq(wineId).and(wineWishlist.member.id.eq(memberId)))
                        .fetchOne()
        );

        return isLiked.orElse(false);
    }
}
