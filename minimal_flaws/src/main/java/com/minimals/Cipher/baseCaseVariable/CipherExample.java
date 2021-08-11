package com.minimals.Cipher.baseCaseVariable;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class CipherExample  {
    public static void main(String[] args) 
    throws NoSuchAlgorithmException, NoSuchPaddingException {
        String algorithmName = "AES";
        Cipher c = Cipher.getInstance(algorithmName);
        System.out.println(c.getAlgorithm());
    }
}
