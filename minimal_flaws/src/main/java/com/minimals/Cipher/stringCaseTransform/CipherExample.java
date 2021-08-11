package com.minimals.Cipher.stringCaseTransform;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class CipherExample  {
    public static void main(String[] args) 
    throws NoSuchAlgorithmException, NoSuchPaddingException {
        Cipher c = Cipher.getInstance("des".toUpperCase(Locale.ENGLISH));
        System.out.println(c.toString());
    }
}
