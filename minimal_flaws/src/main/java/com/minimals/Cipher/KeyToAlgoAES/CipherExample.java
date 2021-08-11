package com.minimals.Cipher.KeyToAlgoAES;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class CipherExample {
        public static void main(String[] args) {
        try{
            String algo = "AES";
            KeyGenerator keygen = KeyGenerator.getInstance(algo);
            SecretKey key = keygen.generateKey();
            System.out.println(key.getAlgorithm());
            Cipher c = Cipher.getInstance(keygen.getAlgorithm());
            // Cipher c = Cipher.getInstance(key.getAlgorithm().toUpperCase());
            System.out.println(c.getAlgorithm() + " " + key.getAlgorithm());
            c.init(Cipher.ENCRYPT_MODE, key);
            c.doFinal("something".getBytes());

        }catch(Exception e ){
            e.printStackTrace();
        }

    }


}
