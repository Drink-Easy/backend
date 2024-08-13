package com.drinkeg.drinkeg.converter;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.Party;
import com.drinkeg.drinkeg.dto.PartyRequestDTO;
import com.drinkeg.drinkeg.dto.PartyResponseDTO;
import jakarta.persistence.Converter;
import org.springframework.stereotype.Component;


@Component
public class PartyConverter {

    // RequestDTO를 엔티티로 변환
    public Party fromRequest(PartyRequestDTO partyRequest, Member member) {
        return Party.builder()
                .member(member)
                .name(partyRequest.getName())
                .introduce(partyRequest.getIntroduce())
                .limitMemberNum(partyRequest.getLimitMemberNum())
                .partyDate(partyRequest.getPartyDate())
                .admissionFee(partyRequest.getAdmissionFee())
                .place(partyRequest.getPlace())
                .hostId(member.getId())
                //.partyWine(partyRequest.getPartyWine())
                .build();
    }

    // 엔티티를 ResponseDTO로 변환
    public PartyResponseDTO toResponse(Party party) {
        return PartyResponseDTO.builder()
                .id(party.getId())
                .hostId(party.getHostId())
                .name(party.getName())
                .introduce(party.getIntroduce())
                .limitMemberNum(party.getLimitMemberNum())
                .partyDate(party.getPartyDate())
                .admissionFee(party.getAdmissionFee())
                .place(party.getPlace())
                //.partyWine(party.getPartyWine())
                .build();
    }

    // 기존 엔티티를 업데이트된 정보로 변환
    public Party updatePartyFromRequest(Party existingParty, PartyRequestDTO partyRequest) {
        return Party.builder()
                .id(existingParty.getId()) // 기존 ID
                .member(existingParty.getMember()) // 기존 Member
                .name(partyRequest.getName())
                .introduce(partyRequest.getIntroduce())
                .limitMemberNum(partyRequest.getLimitMemberNum())
                .partyDate(partyRequest.getPartyDate())
                .admissionFee(partyRequest.getAdmissionFee())
                .place(partyRequest.getPlace())
                .hostId(existingParty.getHostId()) // 기존 host
                //.partyWine(partyRequest.getPartyWine())
                .build();
    }
}
