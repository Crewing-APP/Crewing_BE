package com.crewing.common.util;

import com.crewing.auth.dto.AppleDto;
import com.crewing.auth.dto.AppleKeyInfo;
import com.crewing.auth.dto.ApplePublicKeyResponse;
import com.crewing.auth.dto.AppleTokenResponseDto;
import com.crewing.common.error.auth.ApplePublicKeyException;
import com.crewing.common.error.auth.AppleTokenValidationException;
import com.crewing.external.AppleClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.io.*;
import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.crewing.auth.dto.AppleDto.*;
import static com.nimbusds.oauth2.sdk.GrantType.AUTHORIZATION_CODE;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppleAuthUtil {
    @Value("${apple.key.teamId}")
    private String teamId;
    @Value("${apple.key.bundleId}")
    private String clientId;
    @Value("${apple.key.keyId}")
    private String keyId;
    @Value("${apple.key.keyPath}")
    private String appleSignKeyFilePath;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AppleClient appleClient;
    // identityToken 공개키를 이용한 서명 검증
    public Claims verifyIdentityToken(String identityToken) {
        try {
            ApplePublicKeyResponse response = appleClient.findAppleAuthPublicKeys();

            String identityTokenHeader = identityToken.substring(0, identityToken.indexOf("."));

            //identityTokenHeader decode
            String decodedIdentityTokenHeader = new String(Base64.getDecoder().decode(identityTokenHeader), StandardCharsets.UTF_8);

            Map<String, String> identityTokenHeaderMap = objectMapper.readValue(decodedIdentityTokenHeader, Map.class);
            AppleKeyInfo appleKeyInfo = response.keys().stream()
                    .filter(key -> Objects.equals(key.getKid(), identityTokenHeaderMap.get("kid"))
                            && Objects.equals(key.getAlg(), identityTokenHeaderMap.get("alg")))
                    .findFirst()
                    .orElseThrow(ApplePublicKeyException::new);

            byte[] nBytes = Base64.getUrlDecoder().decode(appleKeyInfo.getN());
            byte[] eBytes = Base64.getUrlDecoder().decode(appleKeyInfo.getE());

            BigInteger n = new BigInteger(1, nBytes);
            BigInteger e = new BigInteger(1, eBytes);

            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance(appleKeyInfo.getKty());
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            return Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(identityToken)
                    .getBody();
        } catch (Exception e) {
            throw new AppleTokenValidationException();
        }
    }
    // 애플 accessToken, refreshToken 획득
    public AppleTokenResponseDto getAppleToken(String authorizationCode) {
        AppleTokenRequestDto appleTokenRequest = AppleTokenRequestDto.builder()
                .client_id(clientId)
                .client_secret(createClientSecret())
                .code(authorizationCode)
                .grant_type(String.valueOf(AUTHORIZATION_CODE))
                .build();
        log.info("clientId={}",clientId);
        return appleClient.findAppleToken(appleTokenRequest);
    }

    // 회원 탈퇴 애플 서버에 요청
    public void revoke(String refreshToken) {
        try {
            AppleRevokeRequest appleRevokeRequest = AppleRevokeRequest.builder()
                    .client_id(clientId)
                    .client_secret(this.createClientSecret())
                    .token(refreshToken)
                    .token_type_hint("refresh_token")
                    .build();
            appleClient.revoke(appleRevokeRequest);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Apple Revoke Error");
        }
    }
    // client_secret 생성
    public String createClientSecret() {
        Date expirationDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());
        try {
            return Jwts.builder()
                    .setHeaderParam("kid", keyId)
                    .setHeaderParam("alg", "ES256")
                    .setIssuer(teamId)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(expirationDate)
                    .setAudience("https://appleid.apple.com")
                    .setSubject(clientId)
                    .signWith(this.getPrivateKey(), SignatureAlgorithm.ES256)
                    .compact();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PrivateKey getPrivateKey() throws IOException {
        ClassPathResource resource = new ClassPathResource(appleSignKeyFilePath);
        String privateKey = new String(Files.readAllBytes(Paths.get(resource.getURI())));

        Reader pemReader = new StringReader(privateKey);
        PEMParser pemParser = new PEMParser(pemReader);
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
        log.info("apple getPrivateKey 성공");

        return converter.getPrivateKey(object);
    }


}
