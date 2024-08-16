package com.drinkeg.drinkeg.service.loginService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.loginDTO.jwtDTO.JoinDTO;
import com.drinkeg.drinkeg.dto.loginDTO.jwtDTO.MemberRequestDTO;
import com.drinkeg.drinkeg.dto.loginDTO.jwtDTO.MemberResponseDTO;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void join(JoinDTO joinDTO) {

        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();

        Boolean isExist = memberRepository.existsByUsername(username);

        if (isExist) {
            throw new GeneralException(ErrorStatus.MEMBER_ALREADY_EXIST);
        }

        Member data = new Member();

        data.setUsername(username);
        data.setPassword(bCryptPasswordEncoder.encode(password));
        data.setRole("ROLE_USER");
        data.setIsFirst(true);

        memberRepository.save(data);
    }

    public MemberResponseDTO addMemberDetail(MemberRequestDTO memberRequestDTO, String username) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus.SESSION_UNAUTHORIZED));

        if (memberRequestDTO.getName() != null) {
            member.updateName(memberRequestDTO.getName());
        }
        if (memberRequestDTO.getIsNewbie() != null) {
            member.updateIsNewbie(memberRequestDTO.getIsNewbie());
        }
        if (memberRequestDTO.getMonthPrice() != null) {
            member.updateMonthPriceMax(memberRequestDTO.getMonthPrice());
        }
        if (memberRequestDTO.getWineSort() != null) {
            member.updateWineSort(memberRequestDTO.getWineSort());
        }
        if (memberRequestDTO.getWineArea() != null) {
            member.updateWineNation(memberRequestDTO.getWineArea());
        }
        if (memberRequestDTO.getWineVariety() != null) {
            member.updateWineVariety(memberRequestDTO.getWineVariety());
        }
        if (memberRequestDTO.getRegion() != null) {
            member.updateRegion(memberRequestDTO.getRegion());
        }

        // 회원 가입을 한 유저로 변경
        member.updateIsFirst();

        memberRepository.save(member);

        MemberResponseDTO memberResponseDTO = MemberResponseDTO.builder()
                .id(member.getId())
                .name(member.getName())
                .username(member.getUsername())
                .role(member.getRole())
                .isNewbie(member.getIsNewbie())
                .isFirst(member.getIsFirst())
                .monthPriceMax(member.getMonthPriceMax())
                .wineSort(member.getWineSort())
                .wineArea(member.getWineArea())
                .wineVariety(member.getWineVariety())
                .region(member.getRegion())
                .build();

        return memberResponseDTO;
    }
}