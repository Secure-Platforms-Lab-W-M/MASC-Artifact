package com.minimals.Cipher.secureBlowFish;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class BlowFish {

    public static void main(String[] args) {
        int keygen_size = 256;
        try {

            KeyGenerator keyGenerator = KeyGenerator.getInstance("Blowfish");
            keyGenerator.init(keygen_size);

            Key key = keyGenerator.generateKey();

            Cipher cipher = Cipher.getInstance("Blowfish/CFB8/NoPadding");

            SecureRandom random = new SecureRandom();
            byte[] iv = new byte[8];
            random.nextBytes(iv);

            IvParameterSpec spec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);
            System.out.println(cipher.getBlockSize());
            String plaintext = "Hello World";
            byte[] encrypted = cipher.doFinal(plaintext.getBytes());

            String encrypted_str = new String(encrypted, StandardCharsets.UTF_8);

            System.out.println(encrypted_str);

            Cipher decrypter = Cipher.getInstance("Blowfish/CFB8/NoPadding");
            decrypter.init(Cipher.DECRYPT_MODE, key, spec);

            byte [] retrieved = decrypter.doFinal(encrypted);

            String retrieved_text = new String(retrieved, StandardCharsets.UTF_8);

            System.out.println(retrieved_text);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
