package com.drinkeg.drinkeg.dto.AppleLoginDTO;


import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.apipayLoad.handler.TempHandler;
import lombok.Getter;

import java.util.List;

// 애플 서버에서 주는 공개키 (public key) DTO
public class ApplePublicKeyResponseDTO {

    private List<ApplePublicKey> keys;

    @Getter
    public static class ApplePublicKey {
        private String kty;
        private String kid;
        private String use;
        private String alg;
        private String n;
        private String e;
    }

    // 여러개 받은 공개 키 중에 Identity Token과 kid와 alg가 일치하는 토큰 찾아주는 메서드
    public ApplePublicKey getMatchedKeyBy(String kid, String alg) {
        return keys.stream()
                .filter(key -> key.getKid().equals(kid) && key.getAlg().equals(alg))
                .findAny()
                .orElseThrow(() -> new TempHandler(ErrorStatus.MATCH_PUBLIC_KEY_NOR_FOUND));
    }


}
