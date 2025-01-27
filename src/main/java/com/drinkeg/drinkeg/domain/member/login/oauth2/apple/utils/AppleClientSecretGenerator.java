package com.drinkeg.drinkeg.domain.member.login.oauth2.apple.utils;


import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class AppleClientSecretGenerator {

    private final ApplePrivateKeyGenerator applePrivateKeyGenerator;


    @Value("${spring.servlet.social-login.provider.apple.key-id}")
    private String keyId;
    @Value("${spring.servlet.social-login.provider.apple.team-id}")
    private String teamId;
    @Value("${spring.servlet.social-login.provider.apple.client-id}")
    private String clientId;

    public String generateClientSecret() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        System.out.println("--------------apple generateClientSecret---------------");

        Date expirationDate = Date.from(LocalDateTime.now().plusDays(5)
                .atZone(ZoneId.systemDefault()).toInstant());

        System.out.println("--------------apple generateClientSecret---------------2");


        return Jwts.builder()
                    .setHeaderParam("alg", "ES256")
                    .setHeaderParam("kid", keyId)
                    .setIssuer(teamId)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(expirationDate)
                    .setAudience("https://appleid.apple.com")
                    .setSubject(clientId)
                    .signWith(applePrivateKeyGenerator.getPrivateKey(), SignatureAlgorithm.ES256)
                    .compact();}


    }
}