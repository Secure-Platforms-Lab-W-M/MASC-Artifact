package com.minimals.messagedigest.stringCaseTransformation;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class MessageDigestComplex {
    public static void main(String[] args) {
        MessageDigest cryptoDigest;
        try {
            cryptoDigest = MessageDigest.getInstance("md5".toUpperCase(Locale.ENGLISH));
            System.out.println(cryptoDigest.getAlgorithm());

        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error");
        }
    }
}
