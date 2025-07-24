package com.example.bankcards.util;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PanEncryptor {
	
    private static final String ALGO = "AES";
    
    @Value("${app.card.encryption.secret}")
    private String secret;

    public String encrypt(String pan) {
    	try {
    		Cipher cipher = Cipher.getInstance(ALGO);
	        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secret.getBytes(), ALGO));
	        byte[] encrypted = cipher.doFinal(pan.getBytes());
	        return Base64.getEncoder().encodeToString(encrypted);
    	} catch (Exception ex) {
    		throw new RuntimeException(ex);
    	}
    }

    public String decrypt(String encryptedPan) {
    	try {
    		Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secret.getBytes(), ALGO));
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encryptedPan));
            return new String(original);
    	} catch (Exception ex) {
    		throw new RuntimeException(ex);
    	}
    }
}
