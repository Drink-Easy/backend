package com.drinkeg.drinkeg.service.wineNoteService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.domain.TastingNote;
import com.drinkeg.drinkeg.domain.WineNote;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.TastingNoteRepository;
import com.drinkeg.drinkeg.repository.WineNoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WineNoteServiceImpl implements WineNoteService {

    private final WineNoteRepository wineNoteRepository;
    private final TastingNoteRepository tastingNoteRepository;

    @Override
    public void updateWineNote(WineNote wineNote) {
        List<TastingNote> allTastingNotes = tastingNoteRepository.findAllByWine(wineNote.getWine());

        // 맛 점수 합
        int totalSugarContent = allTastingNotes.stream().mapToInt(TastingNote::getSugarContent).sum();
        int totalAcidity = allTastingNotes.stream().mapToInt(TastingNote::getAcidity).sum();
        int totalTannin = allTastingNotes.stream().mapToInt(TastingNote::getTannin).sum();
        int totalBody = allTastingNotes.stream().mapToInt(TastingNote::getBody).sum();
        int totalAlcohol = allTastingNotes.stream().mapToInt(TastingNote::getAlcohol).sum();

        // 맛 점수 평균
        int avgSugarContent = totalSugarContent / allTastingNotes.size();
        int avgAcidity = totalAcidity / allTastingNotes.size();
        int avgTannin = totalTannin / allTastingNotes.size();
        int avgBody = totalBody / allTastingNotes.size();
        int avgAlcohol = totalAlcohol / allTastingNotes.size();

        // 맛 평균 없데이트
        wineNote.updateSugarContent(avgSugarContent);
        wineNote.updateAcidity(avgAcidity);
        wineNote.updateTannin(avgTannin);
        wineNote.updateBody(avgBody);
        wineNote.updateAlcohol(avgAlcohol);

        // Top3 향으로 업데이트
        wineNote.updateScentAroma(getTop3Scent(allTastingNotes, "aroma"));
        wineNote.updateScentTaste(getTop3Scent(allTastingNotes, "taste"));
        wineNote.updateScentFinish(getTop3Scent(allTastingNotes, "finish"));

        wineNoteRepository.save(wineNote);

    }

    // Top3 향 추출
    private List<String> getTop3Scent(List<TastingNote> allTastingNotes, String scentName) {
        List<String> scentList = allTastingNotes.stream()
                .map(tastingNote -> switch (scentName) {
                    case "aroma" -> tastingNote.getScentAroma();
                    case "taste" -> tastingNote.getScentTaste();
                    case "finish" -> tastingNote.getScentFinish();
                    default -> throw new GeneralException(ErrorStatus.NOT_INVALID_SCENT);
                })
                .flatMap(List::stream)
                .toList();

        Map<String, Long> countMap = scentList.stream()
                // Function.identity()를 사용하여 각 향의 이름을 key 로 하는 Map 을 생성
                // Collectors.counting()을 사용하여 각 향의 개수를 value 로 하는 Map 을 생성
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // entrySet()은 Map 의 key 와 value 를 Set 으로 반환
        return countMap.entrySet().stream()
                // value 를 기준으로 내림차순 정렬
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

}
