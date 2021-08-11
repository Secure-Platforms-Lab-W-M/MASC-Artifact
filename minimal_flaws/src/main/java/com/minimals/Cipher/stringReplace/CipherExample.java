package com.minimals.Cipher.stringReplace;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class CipherExample  {
    public static void main(String[] args) 
    throws NoSuchAlgorithmException, NoSuchPaddingException {
        Cipher c = Cipher.getInstance("DE$S".replace("$", ""));

        System.out.println(c.getAlgorithm());
    }
}
