package com.drinkeg.drinkeg.service.partyJoinMemberService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.PartyJoinMemberConverter;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.Party;
import com.drinkeg.drinkeg.domain.PartyJoinMember;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.PartyJoinMemberRepository;
import com.drinkeg.drinkeg.service.memberService.MemberService;
import com.drinkeg.drinkeg.service.partyService.PartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartyJoinMemberServiceImpl implements PartyJoinMemberService{

    private final PartyJoinMemberRepository partyJoinMemberRepository;
    private final PartyJoinMemberConverter partyJoinMemberConverter;
    private final MemberService memberService;
    private final PartyService partyService;

    @Override
    public PartyJoinMember save(PartyJoinMember partyJoinMember) {
        return partyJoinMemberRepository.save(partyJoinMember);
    }

    @Override
    public long countByParty(Party party) {
        return partyJoinMemberRepository.countByParty(party);
    }

    // 멤버가 특정 모임에 참가하는 로직
    @Override
    public void participateInParty(PrincipalDetail principalDetail, Long partyId) {
        // 멤버와 파티를 조회
        Member member = memberService.loadMemberByPrincipleDetail(principalDetail);
        Party party = partyService.findPartyById(partyId);

        // 이미 해당 모임에 참가했는지 확인
        if (partyJoinMemberRepository.existsByMemberAndParty(member, party)) {
            throw new GeneralException(ErrorStatus.EXIST_IN_PARTY);
        }

        // 모임의 최대 참가자 수를 넘지 않았는지 확인
        if (party.getParticipateMemberNum() >= party.getLimitMemberNum()) {
            throw new GeneralException(ErrorStatus.PARTY_FULL);
        }

        // 새로운 참가 기록을 생성 (Converter 사용)
        PartyJoinMember partyJoinMember = partyJoinMemberConverter.toEntity(member, party, false);
        partyJoinMemberRepository.save(partyJoinMember);

        // 참가자 수를 partyJoinMember 테이블을 기반으로 갱신
        long updatedParticipantCount = partyJoinMemberRepository.countByParty(party);
        party.setParticipateMemberNum((int) updatedParticipantCount);  // 업데이트된 참가자 수를 설정
        partyService.saveParty(party);
    }

    @Override
    public void cancelPartyJoin(PrincipalDetail principalDetail, Long partyId) {
        // 멤버와 파티를 조회
        Member foundMember = memberService.loadMemberByPrincipleDetail(principalDetail);
        Long memberId = foundMember.getId();
        Party party = partyService.findPartyById(partyId);

        // 사용자가 모임에 참가했는지 확인 (참가하지 않았으면 예외 발생)
        PartyJoinMember partyJoinMember = partyJoinMemberRepository.findByMemberAndParty(foundMember, party)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_FOUND_PARTY_JOIN));

        // 사용자가 모임의 호스트인지 확인 (호스트는 참가 취소할 수 없음)
        if (party.getHostId().equals(memberId)) {
            throw new GeneralException(ErrorStatus.HOST_CANNOT_LEAVE);
        }

        // 모임 참가 기록 삭제
        partyJoinMemberRepository.delete(partyJoinMember);

        // 참가자 수를 partyJoinMember 테이블을 기반으로 갱신
        long updatedParticipantCount = partyJoinMemberRepository.countByParty(party);
        party.setParticipateMemberNum((int) updatedParticipantCount);  // 업데이트된 참가자 수를 설정
        partyService.saveParty(party);
    }
}
