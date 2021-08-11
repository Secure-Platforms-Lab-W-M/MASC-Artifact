package com.minimals.Cipher.aesGCMNoPaddingReplaceDES;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class CipherExample  {
    public static void main(String[] args) 
    throws NoSuchAlgorithmException, NoSuchPaddingException {
        Cipher c = Cipher.getInstance("AES/GCM/NoPadding".replace("AES/GCM/NoPadding", "DES"));

        System.out.println(c.toString());
    }
}
