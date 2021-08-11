package com.minimals.messagedigest.baseCaseVariable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestBase {

    public static void main(String[] args) {
        MessageDigest digest;
        String algorithmName = "MD5";
        try {
            digest = MessageDigest.getInstance(algorithmName);
            System.out.println(digest.getAlgorithm());

        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
