package com.drinkeg.drinkeg.party.service;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.PartyConverter;
import com.drinkeg.drinkeg.converter.PartyJoinMemberConverter;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.party.domain.Party;
import com.drinkeg.drinkeg.domain.PartyJoinMember;
import com.drinkeg.drinkeg.party.dto.PartyRequestDTO;
import com.drinkeg.drinkeg.party.dto.PartyResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.PartyBookmarkRepository;
import com.drinkeg.drinkeg.party.repository.PartyRepository;
import com.drinkeg.drinkeg.service.memberService.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartyServiceImpl implements PartyService {

    private final PartyRepository partyRepository;
    private final PartyConverter partyConverter;
    private final PartyJoinMemberConverter partyJoinMemberConverter;
    private final MemberService memberService;
    //단순한 dao (북마크여부)판단을 위한 레포
    private final PartyBookmarkRepository partyBookmarkRepository;

    @Override
    public Party findPartyById(Long partyId) {
        return partyRepository.findById(partyId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PARTY_NOT_FOUND));
    }

    @Override
    public Party saveParty(Party party) {
        return partyRepository.save(party);
    }

    @Override
    public void createParty(PartyRequestDTO partyRequest, PrincipalDetail principalDetail) {

        // entity 저장
        Member member = memberService.loadMemberByPrincipalDetail(principalDetail);
        Party party = partyConverter.fromRequest(partyRequest, member);
        Party savedParty = partyRepository.save(party);


        // 파티 생성 후, 해당 멤버를 PartyJoinMember 테이블에 호스트로 등록
        PartyJoinMember partyJoinMember = partyJoinMemberConverter.toEntity(member, savedParty, true);

        party.getParticipations().add(partyJoinMember);
        // Party와 PartyJoinMember를 함께 저장 (CascadeType.ALL 덕분에 PartyJoinMember도 함께 저장됨)
        partyRepository.save(party);


    }


    @Override
    public Page<PartyResponseDTO> getSortedParties(String sortType, PrincipalDetail principalDetail, Pageable pageable) {
        Member foundMember = memberService.loadMemberByPrincipalDetail(principalDetail);
        String memberRegion = memberService.loadMemberByPrincipalDetail(principalDetail).getRegion();
        Page<Party> parties = switch (sortType) {
            case "recent" ->
                // 최신순 정렬
                    partyRepository.findAllByOrderByCreatedAtDesc(pageable);
            case "deadline" ->
                // 마감 임박순 정렬
                    partyRepository.findAllByOrderByPartyDateAsc(pageable);
            case "popular" ->
                // 인원이 많이 모인 순 정렬
                    partyRepository.findAllByOrderByParticipateMemberNumDesc(pageable);
            case "price" ->
                // 가격순 정렬 (낮은 가격 순)
                    partyRepository.findAllByOrderByAdmissionFeeAsc(pageable);
            case "distance" ->
                // 가까운 모임 (사용자 region과 party place가 일치하는 모임만)
                    partyRepository.findByPlace(memberRegion, pageable);
            default -> throw new GeneralException(ErrorStatus.INVALID_SORT_TYPE); // 유효하지 않은 정렬 기준일 경우 예외 처리
        };

        // Party 엔티티를 PartyResponseDTO로 변환
        return parties.map(party -> {
            PartyResponseDTO partyResponseDTO = partyConverter.toResponse(party);
            boolean isBookmarked = partyBookmarkRepository.existsByMemberAndParty(foundMember, party);
            partyResponseDTO.setBookmarked(isBookmarked);
            return partyResponseDTO;
        });
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
    public List<PartyResponseDTO> getAllParties(PrincipalDetail principalDetail) {
        // 현재 로그인한 사용자 조회
        Member member = memberService.loadMemberByPrincipalDetail(principalDetail);

        // 모든 파티 조회
        List<Party> parties = partyRepository.findAll();

        // 각 파티에 대해 북마크 여부 확인 후 DTO로 변환
        return parties.stream().map(party -> {
            boolean isBookmarked = partyBookmarkRepository.existsByMemberAndParty(member, party);
            PartyResponseDTO partyResponseDTO = partyConverter.toResponse(party);
            partyResponseDTO.setBookmarked(isBookmarked); // 북마크 여부 설정
            return partyResponseDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public PartyResponseDTO updateParty(Long id, PartyRequestDTO partyRequest, PrincipalDetail principalDetail) {
        // 필수 값 검증
        validatePartyRequest(partyRequest);

        // 기존 모임 조회
        Party existingParty = partyRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PARTY_NOT_FOUND));

        // hostId로 소유자 확인
        Member foundMember = memberService.loadMemberByPrincipalDetail(principalDetail);
        Long memberId = foundMember.getId();
        if (existingParty.getHostId() == null || !existingParty.getHostId().equals(memberId)) {
            throw new GeneralException(ErrorStatus.NOT_YOUR_PARTY); // 사용자가 호스트가 아닐 경우 예외 발생
        }

        // 새로운 제한 인원이 기존 참가 인원보다 작은지 확인
        if (partyRequest.getLimitMemberNum() < existingParty.getParticipateMemberNum()) {
            throw new GeneralException(ErrorStatus.INVALID_PARTY_REQUEST_LIMITMEMBERNUM);
        }

        // 업데이트된 엔티티 생성
        Party updatedParty = partyConverter.updatePartyFromRequest(existingParty, partyRequest);
        Party savedParty = partyRepository.save(updatedParty);

        return partyConverter.toResponse(savedParty);
    }

    @Override
    public void deleteParty(Long id, PrincipalDetail principalDetail) {
        Party party = partyRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PARTY_NOT_FOUND));

        Member foundMember = memberService.loadMemberByPrincipalDetail(principalDetail);
        Long memberId = foundMember.getId();
        if (party.getHostId() == null || !party.getHostId().equals(memberId)) {
            throw new GeneralException(ErrorStatus.NOT_YOUR_PARTY); // 사용자가 호스트가 아닐 경우 예외 발생
        }
        partyRepository.delete(party);
    }

    @Override
    public List<PartyResponseDTO> searchPartiesByName(String searchName, PrincipalDetail principalDetail) {
        Member member = memberService.loadMemberByPrincipalDetail(principalDetail);

        // 모임 제목이 정확히 일치하는 모임을 검색
        List<Party> exactMatchParties = partyRepository.findAllByName(searchName);

        // 모임 제목을 공백으로 나누어 키워드로 검색
        String[] keywords = searchName.split(" ");
        Set<Party> searchParties = new LinkedHashSet<>(exactMatchParties);

        for (String keyword : keywords) {
            // 키워드가 포함된 모임을 검색
            List<Party> keywordContainingParties = partyRepository.findAllByNameContainingIgnoreCase(keyword);

            // 모임 제목에 키워드가 포함된 모임을 결과에 추가
            if (!keywordContainingParties.isEmpty()) {
                searchParties.addAll(keywordContainingParties);
            }
        }

        // 모임을 PartyResponseDTO로 변환하여 북마크 여부 설정 후 반환
        return searchParties.stream()
                .map(party -> {
                    PartyResponseDTO partyResponseDTO = partyConverter.toResponse(party);
                    boolean isBookmarked = partyBookmarkRepository.existsByMemberAndParty(member, party);
                    partyResponseDTO.setBookmarked(isBookmarked);
                    return partyResponseDTO;
                })
                .collect(Collectors.toList());
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

    // bookmarkCount 증가 메서드
    @Override
    public void increaseBookmarkCount(Long partyId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PARTY_NOT_FOUND));

        int updatedCount = party.getBookmarkCount() + 1;
        party.updateBookmarkCount(updatedCount); // 커스텀 메서드 호출
        partyRepository.save(party);
    }

    // bookmarkCount 감소 메서드
    @Override
    public void decreaseBookmarkCount(Long partyId) {
        Party party = partyRepository.findById(partyId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PARTY_NOT_FOUND));

        if (party.getBookmarkCount() > 0) {
            int updatedCount = party.getBookmarkCount() - 1;
            party.updateBookmarkCount(updatedCount); // 커스텀 메서드 호출
            partyRepository.save(party);
        }
    }
}
