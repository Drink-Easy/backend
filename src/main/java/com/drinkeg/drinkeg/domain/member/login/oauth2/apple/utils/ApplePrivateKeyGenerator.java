package com.drinkeg.drinkeg.domain.member.login.oauth2.apple.utils;


import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ApplePrivateKeyGenerator  {

    @Value("${spring.servlet.social-login.provider.apple.private-key-path}")
    private String privateKeyPath;

    public PrivateKey getPrivateKey() {

        try (Reader pemReader = new FileReader(privateKeyPath)) {


            PEMParser pemParser = new PEMParser(pemReader);
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
            PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();

            PrivateKey privateKey = converter.getPrivateKey(object);


            return privateKey;

        } catch (IOException e) {
            e.printStackTrace();
            throw new GeneralException(ErrorStatus.FAILED_TO_LOAD_PRIVATE_KEY);
        }
    }

}
