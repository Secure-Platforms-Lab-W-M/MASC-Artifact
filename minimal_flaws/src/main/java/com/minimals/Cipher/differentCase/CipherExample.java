package com.minimals.Cipher.differentCase;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class CipherExample  {
    public static void main(String[] args) 
    throws NoSuchAlgorithmException, NoSuchPaddingException {
        Cipher c = Cipher.getInstance("des");        
        System.out.println(c.toString());
    }
}
