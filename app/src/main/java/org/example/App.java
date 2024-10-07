package org.example;

import java.util.*;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.ECDHDecrypter;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.ECKey;
import java.text.ParseException;
import com.nimbusds.jose.JOSEException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) {

      try {
                // Read JWE from file
            String jweString = readFromFile("/jwe.txt");
      String jwkString = """
        {
            "kty": "EC",
            "d": "7N8jd8HvUp3vHC7a-xitehRnYuyZLy3kqkxG7KmpfMY",
            "use": "enc",
            "crv": "P-256",
            "kid": "A541J5yUqazgE8WBFkIyeh2OtK-udqUR_OC0kB7l3oU",
            "x": "cwYyuS94hcOtcPlrMMtGtflCfbZUwz5Mf1Gfa2m0AM8",
            "y": "KB7sJkFQyB8jZHO9vmWS5LNECL4id3OJO9HX9ChNonA",
            "alg": "ECDH-ES"
          }
      """;

      JWT jwt = JWTParser.parse(jweString);
      EncryptedJWT encryptedJWT = (EncryptedJWT) jwt;
      JWK decryptionKey = JWK.parse(jwkString);
      ECKey ecKey = (ECKey) decryptionKey;
      ECDHDecrypter decrypter = new ECDHDecrypter(ecKey);

      // DirectDecrypter decrypter = new DirectDecrypter((OctetSequenceKey)decryptionKey);
      encryptedJWT.decrypt(decrypter);
      String decryptedPayload = encryptedJWT.getPayload().toString();
      System.out.println(decryptedPayload);
        } catch (ParseException e) {
            System.err.println("Error parsing JWT or JWK: " + e.getMessage());
            e.printStackTrace();
        } catch (JOSEException e) {
            System.err.println("Error during decryption: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
  }

      private static String readFromFile(String fileName) throws Exception {
        try (InputStream inputStream = App.class.getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }
}