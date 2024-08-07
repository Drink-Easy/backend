package com.drinkeg.drinkeg.service.partyService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.PartyConverter;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.Party;
import com.drinkeg.drinkeg.dto.PartyRequestDTO;
import com.drinkeg.drinkeg.dto.PartyResponseDTO;
import com.drinkeg.drinkeg.repository.MemberRepository;
import com.drinkeg.drinkeg.repository.PartyRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartyServiceImpl implements PartyService {

    private final PartyRepository partyRepository;
    private final MemberRepository memberRepository;
    private final PartyConverter partyConverter;

    @Override
    public PartyResponseDTO createParty(PartyRequestDTO partyRequest) {
        // 호스트 멤버 id 검증
        Member member = memberRepository.findById(partyRequest.getHostId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorStatus.MEMBER_NOT_FOUND.getMessage()));

        // entity 저장
        Party party = partyConverter.fromRequest(partyRequest, member);
        Party savedParty = partyRepository.save(party);

        return partyConverter.toResponse(savedParty);
    }

    @Override
    public PartyResponseDTO getParty(Long id) {
        // entity 조회
        Party party = partyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorStatus.PARTY_NOT_FOUND.getMessage()));

        return partyConverter.toResponse(party);
    }

    @Override
    public List<PartyResponseDTO> getAllParties() {
        List<Party> parties = partyRepository.findAll();
        return parties.stream().map(partyConverter::toResponse).collect(Collectors.toList());
    }

    @Override
    public PartyResponseDTO updateParty(Long id, PartyRequestDTO partyRequest) {
        // 기존 모임 조회
        Party existingParty = partyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorStatus.PARTY_NOT_FOUND.getMessage()));

        // 업데이트된 엔티티 생성
        Party updatedParty = partyConverter.updatePartyFromRequest(existingParty, partyRequest);
        Party savedParty = partyRepository.save(updatedParty);

        return partyConverter.toResponse(savedParty);
    }

    @Override
    public void deleteParty(Long id) {
        Party party = partyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorStatus.PARTY_NOT_FOUND.getMessage()));
        partyRepository.delete(party);
    }

}
