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

        memberRepository.save(data);
    }

    public MemberResponseDTO addMemberDetail(MemberRequestDTO memberRequestDTO, String username) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus.SESSION_UNAUTHORIZED));

        if (memberRequestDTO.getIsNewbie() != null) {
            member.updateIsNewbie(memberRequestDTO.getIsNewbie());
        }
        if (memberRequestDTO.getMonthPrice() != null) {
            member.updateMonthPrice(memberRequestDTO.getMonthPrice());
        }
        if (memberRequestDTO.getWineSort() != null) {
            member.updateWineSort(memberRequestDTO.getWineSort());
        }
        if (memberRequestDTO.getWineNation() != null) {
            member.updateWineNation(memberRequestDTO.getWineNation());
        }
        if (memberRequestDTO.getWineVariety() != null) {
            member.updateWineVariety(memberRequestDTO.getWineVariety());
        }
        if (memberRequestDTO.getRegion() != null) {
            member.updateRegion(memberRequestDTO.getRegion());
        }

        memberRepository.save(member);

        MemberResponseDTO memberResponseDTO = MemberResponseDTO.builder()
                .id(member.getId())
                .name(member.getName())
                .username(member.getUsername())
                .role(member.getRole())
                .isNewbie(member.getIsNewbie())
                .monthPrice(member.getMonthPrice())
                .wineSort(member.getWineSort())
                .wineNation(member.getWineNation())
                .wineVariety(member.getWineVariety())
                .region(member.getRegion())
                .build();

        return memberResponseDTO;
    }
}