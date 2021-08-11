package com.minimals.messagedigest.interprocedureCase;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestComplex {
    private String digestName = "SHA-256";
    public MessageDigestComplex A(){
        digestName = "SHA-256";
        return this;
    }
    public MessageDigestComplex B(){
        digestName = "MD5";
        return this;
    }
    public String getName(){
        return digestName;
    }
    public static void main(String[] args) {
        MessageDigest cryptoDigest;
        try {
            cryptoDigest = MessageDigest.getInstance(new MessageDigestComplex().A().B().getName());
            System.out.println(cryptoDigest.getAlgorithm());

        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error");
        }
    }
}
