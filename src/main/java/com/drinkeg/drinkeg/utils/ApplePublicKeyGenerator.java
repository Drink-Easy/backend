package com.drinkeg.drinkeg.utils;


import com.drinkeg.drinkeg.dto.AppleLoginDTO.ApplePublicKeyResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ApplePublicKeyGenerator {

    // apple 서버에서 받은 public key들 중에서 Identity Token의 헤더 값과 일치하는 토큰을 찾아 반환해주는 메소드
    public PublicKey generatePublicKey(Map<String, String> tokenHeaders,
                                       ApplePublicKeyResponseDTO applePublicKeys)
            throws AuthenticationException, NoSuchAlgorithmException, InvalidKeySpecException {

        System.out.println("======apple public key generator==========");
        System.out.println(tokenHeaders);
        for (ApplePublicKeyResponseDTO.ApplePublicKey key : applePublicKeys.getKeys()) {
            System.out.println("kty: " + key.getKty());
            System.out.println("kid: " + key.getKid());
            System.out.println("use: " + key.getUse());
            System.out.println("alg: " + key.getAlg());
            System.out.println("n: " + key.getN());
            System.out.println("e: " + key.getE());
            System.out.println("----------------------------");
        }

        ApplePublicKeyResponseDTO.ApplePublicKey publicKey = applePublicKeys.getMatchedKeyBy(tokenHeaders.get("kid"),
                tokenHeaders.get("alg"));

        /*for (ApplePublicKeyResponseDTO.ApplePublicKey key : applePublicKeys.getKeys()) {
            System.out.println("kty: " + key.getKty());
            System.out.println("kid: " + key.getKid());
            System.out.println("use: " + key.getUse());
            System.out.println("alg: " + key.getAlg());
            System.out.println("n: " + key.getN());
            System.out.println("e: " + key.getE());
            System.out.println("----------------------------");
        }

         */

        return getPublicKey(publicKey);
    }


    // apple 서버에서 받은 public key 조각들을 하나의 공개키로 만들어주는 메서드
    private PublicKey getPublicKey(ApplePublicKeyResponseDTO.ApplePublicKey publicKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] nBytes = Base64.getUrlDecoder().decode(publicKey.getN());
        byte[] eBytes = Base64.getUrlDecoder().decode(publicKey.getE());

        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(new BigInteger(1, nBytes),
                new BigInteger(1, eBytes));


        KeyFactory keyFactory = KeyFactory.getInstance(publicKey.getKty());
        return keyFactory.generatePublic(publicKeySpec);
    }

}
