package com.drinkeg.drinkeg.service.partyBookmarkService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.PartyBookmarkConverter;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.Party;
import com.drinkeg.drinkeg.domain.PartyBookmark;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.PartyBookmarkRepository;
import com.drinkeg.drinkeg.service.memberService.MemberService;
import com.drinkeg.drinkeg.service.partyService.PartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartyBookmarkServiceImpl implements PartyBookmarkService{
    private final PartyBookmarkRepository partyBookmarkRepository;
    private final PartyBookmarkConverter partyBookmarkConverter;
    private final MemberService memberService;
    private final PartyService partyService;

    // 북마크 생성
    public void createBookmark(PrincipalDetail principalDetail, Long partyId) {
        // 멤버 및 파티 조회
        Member member = memberService.loadMemberByPrincipalDetail(principalDetail);
        Party party = partyService.findPartyById(partyId);

        // 이미 북마크한 경우 예외 처리
        if (partyBookmarkRepository.existsByMemberAndParty(member, party)) {
            throw new GeneralException(ErrorStatus.PARTY_BOOKMARK_ALREADY_EXISTS);
        }

        // Converter를 통해 PartyBookmark 생성
        PartyBookmark partyBookmark = partyBookmarkConverter.toEntity(member, party);
        partyBookmarkRepository.save(partyBookmark);

        // bookmarkCount 증가
        partyService.increaseBookmarkCount(party.getId());
    }

    // 북마크 취소
    public void cancelBookmark(PrincipalDetail principalDetail, Long partyId) {
        // 멤버 및 파티 조회
        Member member = memberService.loadMemberByPrincipalDetail(principalDetail);
        Party party = partyService.findPartyById(partyId);

        // 북마크 존재 여부 확인 및 삭제
        PartyBookmark partyBookmark = partyBookmarkRepository.findByMemberAndParty(member, party)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PARTY_BOOKMARK_NOT_FOUND));
        partyBookmarkRepository.delete(partyBookmark);

        // bookmarkCount 감소
        partyService.decreaseBookmarkCount(party.getId());
    }
}
