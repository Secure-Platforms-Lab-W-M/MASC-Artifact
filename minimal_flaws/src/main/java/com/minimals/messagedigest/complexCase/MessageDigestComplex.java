package com.minimals.messagedigest.complexCase;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestComplex {
    public static void main(String[] args) {
        MessageDigest cryptoDigest;
        try {
            cryptoDigest = MessageDigest.getInstance("SHA-256".replace("SHA-256", "md5"));
            System.out.println(cryptoDigest.getAlgorithm());

        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error");
        }
    }
}
