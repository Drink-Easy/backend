package com.drinkeg.drinkeg.service.partyBookmarkService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.PartyBookmarkConverter;
import com.drinkeg.drinkeg.converter.PartyConverter;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.party.domain.Party;
import com.drinkeg.drinkeg.domain.PartyBookmark;
import com.drinkeg.drinkeg.party.dto.PartyResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.event.partyBookmarkEvent.PartyBookmarkCreateEvent;
import com.drinkeg.drinkeg.event.partyBookmarkEvent.PartyBookmarkDeleteEvent;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.PartyBookmarkRepository;
import com.drinkeg.drinkeg.service.memberService.MemberService;
import com.drinkeg.drinkeg.party.service.PartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartyBookmarkServiceImpl implements PartyBookmarkService{
    private final PartyBookmarkRepository partyBookmarkRepository;
    private final PartyBookmarkConverter partyBookmarkConverter;
    private final MemberService memberService;
    private final PartyService partyService;
    private final ApplicationEventPublisher eventPublisher;


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

        // 북마크 생성 이벤트 발행
        eventPublisher.publishEvent(new PartyBookmarkCreateEvent(partyId, member.getId()));
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

        // 북마크 삭제 이벤트 발행
        eventPublisher.publishEvent(new PartyBookmarkDeleteEvent(partyId, member.getId()));
    }

    // 특정 멤버가 북마크한 파티들을 조회
    public List<PartyResponseDTO> getMemberBookmarks(PrincipalDetail principalDetail) {
        Member member = memberService.loadMemberByPrincipalDetail(principalDetail);

        return partyBookmarkRepository.findByMember(member).stream()
                .map(partyBookmark -> PartyConverter.toResponse(partyBookmark.getParty()))
                .collect(Collectors.toList());
    }
}
