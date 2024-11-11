package com.drinkeg.drinkeg.service.wineNoteService;

import com.drinkeg.drinkeg.domain.TastingNote;
import com.drinkeg.drinkeg.domain.WineNote;
import com.drinkeg.drinkeg.repository.WineNoteRepository;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WineNoteServiceImpl implements WineNoteService {

    private final WineNoteRepository wineNoteRepository;

    @Override
    @Lock(LockModeType.PESSIMISTIC_READ) // 다른 트랜잭션에서 읽기만 가능, 수정 불가
    public void updateWineNote(WineNote wineNote, TastingNote t, boolean add) {

        // 새로 테이스팅 노트 작성 시
        if(add){
            wineNote.addTastingNoteScores(t);
        }
        // 테이스팅 노트 삭제 시
        else {
            wineNote.removeTastingNoteScores(t);
        }

        wineNoteRepository.save(wineNote);

    }
}
