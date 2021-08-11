package com.minimals.RandomNumber.StaticSeedSecureRandom.basecase_seedconstructor;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SecureRand {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        byte[] seedBytes = "Seed".getBytes(StandardCharsets.UTF_8);
        
        //"The SecureRandom instance is seeded with the specified seed bytes." --> unsafe 
        SecureRandom rand1 = new SecureRandom(seedBytes);        
        SecureRandom rand2 = new SecureRandom(seedBytes);        

        System.out.println(rand1.nextInt()==rand2.nextInt());


    }
}
