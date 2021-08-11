package com.minimals.messagedigest.stringReplace;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestComplex {
    public static void main(String[] args) {
        MessageDigest cryptoDigest;
        try {
            cryptoDigest = MessageDigest.getInstance("MD$5".replace("$", ""));
            System.out.println(cryptoDigest.getAlgorithm());

        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error");
        }
    }
}

