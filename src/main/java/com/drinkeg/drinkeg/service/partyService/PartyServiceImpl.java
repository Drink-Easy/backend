package com.drinkeg.drinkeg.service.partyService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.PartyConverter;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.Party;
import com.drinkeg.drinkeg.dto.PartyRequestDTO;
import com.drinkeg.drinkeg.dto.PartyResponseDTO;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.PartyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.access.prepost.PreAuthorize;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartyServiceImpl implements PartyService {

    private final PartyRepository partyRepository;
    private final PartyConverter partyConverter;

    @Override
    public void createParty(PartyRequestDTO partyRequest, Member member) {

        // entity 저장
        Party party = partyConverter.fromRequest(partyRequest, member);
        Party savedParty = partyRepository.save(party);

        //partyConverter.toResponse(savedParty);
    }

    @Override
    public PartyResponseDTO getParty(Long id) {
        // entity 조회
        Party party = partyRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PARTY_NOT_FOUND));

        // 엔티티를 DTO로 변환
        return partyConverter.toResponse(party);

    }

    @Override
    public List<PartyResponseDTO> getAllParties() {
        List<Party> parties = partyRepository.findAll();
        return parties.stream().map(partyConverter::toResponse).collect(Collectors.toList());
    }

    @Override
    //@PreAuthorize("@partyServiceImpl.isHost(#id, #memberId)")
    public PartyResponseDTO updateParty(Long id, PartyRequestDTO partyRequest, Long memberId) {
        // 필수 값 검증
        validatePartyRequest(partyRequest);

        // 기존 모임 조회
        Party existingParty = partyRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PARTY_NOT_FOUND));

        // hostId로 소유자 확인
        if (existingParty.getHostId() == null || !existingParty.getHostId().equals(memberId)) {
            throw new GeneralException(ErrorStatus.NOT_YOUR_PARTY); // 사용자가 호스트가 아닐 경우 예외 발생
        }
        // 업데이트된 엔티티 생성
        Party updatedParty = partyConverter.updatePartyFromRequest(existingParty, partyRequest);
        Party savedParty = partyRepository.save(updatedParty);

        return partyConverter.toResponse(savedParty);
    }

    @Override
    //@PreAuthorize("@partyServiceImpl.isPartyOwner(#id, #memberId)")
    public void deleteParty(Long id, Long memberId) {
        Party party = partyRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PARTY_NOT_FOUND));

        // hostId로 소유자 확인
        if (party.getHostId() == null || !party.getHostId().equals(memberId)) {
            throw new GeneralException(ErrorStatus.NOT_YOUR_PARTY); // 사용자가 호스트가 아닐 경우 예외 발생
        }
        partyRepository.delete(party);
    }

    // 모임 정보를 정상적으로 받아왔는지 확인하는 메소드
    public void validatePartyRequest(PartyRequestDTO partyRequest) {
        if (partyRequest.getName() == null || partyRequest.getName().isEmpty()) {
            throw new GeneralException(ErrorStatus.INVALID_PARTY_REQUEST); // 기본 메시지 사용
        }
        if (partyRequest.getIntroduce() == null || partyRequest.getIntroduce().isEmpty()) {
            throw new GeneralException(ErrorStatus.INVALID_PARTY_REQUEST); // 기본 메시지 사용
        }
        if (partyRequest.getLimitMemberNum() == null) {
            throw new GeneralException(ErrorStatus.INVALID_PARTY_REQUEST); // 기본 메시지 사용
        }
        if (partyRequest.getPartyDate() == null) {
            throw new GeneralException(ErrorStatus.INVALID_PARTY_REQUEST); // 기본 메시지 사용
        }
        if (partyRequest.getAdmissionFee() == null) {
            throw new GeneralException(ErrorStatus.INVALID_PARTY_REQUEST); // 기본 메시지 사용
        }
        if (partyRequest.getPlace() == null || partyRequest.getPlace().isEmpty()) {
            throw new GeneralException(ErrorStatus.INVALID_PARTY_REQUEST); // 기본 메시지 사용
        }
    }

    // 현재 사용자가 호스트인지 확인하는 메소드
    /*public boolean isHost(Long partyId, Long memberId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PARTY_NOT_FOUND));

        // hostId로 소유자 확인
        if (party.getHostId() == null || !party.getHostId().equals(memberId)) {
            throw new GeneralException(ErrorStatus.NOT_YOUR_PARTY); // 사용자가 호스트가 아닐 경우 예외 발생
        }

        return true; // 사용자가 호스트일 경우 true 반환
    }*/
}
