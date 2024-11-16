package com.crewing.common.util;

import com.amazonaws.services.kms.AWSKMS;
import com.amazonaws.services.kms.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;


@Slf4j
@Service
@RequiredArgsConstructor
public class KmsUtil {
    private final AWSKMS kmsClient;
    @Value("${aws.kms.key-id}")
    private String KEY_ID;
    // 암호화 메서드
    public String encrypt(String token) {
        try {
            EncryptRequest request = new EncryptRequest();
            request.withKeyId(KEY_ID);
            request.withPlaintext(ByteBuffer.wrap(token.getBytes(StandardCharsets.UTF_8)));
            request.withEncryptionAlgorithm(EncryptionAlgorithmSpec.RSAES_OAEP_SHA_256);

            EncryptResult result = kmsClient.encrypt(request);
            ByteBuffer ciphertextBlob = result.getCiphertextBlob();

            return new String(Base64.encodeBase64(ciphertextBlob.array()));
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    // 복호화 메서드
    public String decrypt(String token) {
        try {

            DecryptRequest request = new DecryptRequest();
            request.withCiphertextBlob(ByteBuffer.wrap(Base64.decodeBase64(token)));
            request.withKeyId(KEY_ID);
            request.withEncryptionAlgorithm(EncryptionAlgorithmSpec.RSAES_OAEP_SHA_256);
            ByteBuffer plainText = kmsClient.decrypt(request).getPlaintext();
            byte[] bytes = new byte[plainText.remaining()];
            plainText.get(bytes);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }


}
