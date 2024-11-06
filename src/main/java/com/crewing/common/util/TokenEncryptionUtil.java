package com.crewing.common.util;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class TokenEncryptionUtil {
    // AES 암호화를 위한 기본 설정
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 16;
    private static final int GCM_IV_LENGTH = 12;

    // 암호화 메서드
    public static String encrypt(String token) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        byte[] iv = new byte[GCM_IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        SecretKey key = generateKey();
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        byte[] encrypted = cipher.doFinal(token.getBytes());

        byte[] encryptedToken = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, encryptedToken, 0, iv.length);
        System.arraycopy(encrypted, 0, encryptedToken, iv.length, encrypted.length);

        return Base64.getEncoder().encodeToString(encryptedToken);
    }

    // 복호화 메서드
    public static String decrypt(String encryptedToken) throws Exception {
        byte[] decodedToken = Base64.getDecoder().decode(encryptedToken);
        byte[] iv = new byte[GCM_IV_LENGTH];
        System.arraycopy(decodedToken, 0, iv, 0, iv.length);

        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKey key = generateKey();
        cipher.init(Cipher.DECRYPT_MODE, key, spec);

        byte[] original = cipher.doFinal(decodedToken, iv.length, decodedToken.length - iv.length);
        return new String(original);
    }

    // AES 키 생성 메서드
    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); // 256비트 키 사용
        return keyGen.generateKey();
    }

}
