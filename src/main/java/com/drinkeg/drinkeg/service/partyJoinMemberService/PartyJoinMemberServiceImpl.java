package com.drinkeg.drinkeg.service.partyJoinMemberService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.PartyJoinMemberConverter;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.Party;
import com.drinkeg.drinkeg.domain.PartyJoinMember;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.MemberRepository;
import com.drinkeg.drinkeg.repository.PartyJoinMemberRepository;
import com.drinkeg.drinkeg.repository.PartyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PartyJoinMemberServiceImpl implements PartyJoinMemberService{

    private final PartyJoinMemberRepository partyJoinMemberRepository;
    private final MemberRepository memberRepository;
    private final PartyRepository partyRepository;
    private final PartyJoinMemberConverter partyJoinMemberConverter; // Converter 주입

    // 멤버가 특정 모임에 참가하는 로직
    public void participateInParty(Long memberId, Long partyId) {
        // 멤버와 파티를 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PARTY_NOT_FOUND));

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

        // 모임의 참가자 수 증가
        party.setParticipateMemberNum(party.getParticipateMemberNum() + 1);
        partyRepository.save(party);  // 업데이트된 참가자 수를 저장
    }
}
