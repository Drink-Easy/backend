package com.drinkeg.drinkeg.member.login.oauth2.apple;


import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    public PrivateKey getPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        InputStream privateKey = new ClassPathResource("AUTHKEY_DRINKEG.p8").getInputStream();


        String result = new BufferedReader(new InputStreamReader(privateKey)) .lines().collect(Collectors.joining("\n"));

        String key = result.replace("-----BEGIN PRIVATE KEY-----\n", "")
                .replace("-----END PRIVATE KEY-----", "");

        byte[] encoded = Base64.decodeBase64(key);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return keyFactory.generatePrivate(keySpec);

    }

}
