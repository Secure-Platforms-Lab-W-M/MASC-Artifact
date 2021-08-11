package com.minimals.RandomNumber.StaticSeedSecureRandom.basecase_getinstance;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SecureRand {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        byte[] seedBytes = "Seed".getBytes(StandardCharsets.UTF_8);
        SecureRandom rand1 = SecureRandom.getInstance("NativePRNG");
        SecureRandom rand2 = SecureRandom.getInstance("NativePRNG");
        rand1.setSeed(seedBytes);
        rand2.setSeed(seedBytes);
        //"The returned SecureRandom object has not been seeded. To seed the returned object, call the setSeed method. If setSeed is not called, the first call to nextBytes will force the SecureRandom object to seed itself. This self-seeding will not occur if setSeed was previously called"
        
        System.out.println(rand1.nextInt()==rand2.nextInt());

    }
}
