package com.minimals.IV.stash;
import java.security.SecureRandom;

import javax.crypto.spec.IvParameterSpec;

public class StaticIV {
    public static void main(String[] args) {
        // byte[] bytes = "Hello".getBytes();
        // IvParameterSpec iv1 = new IvParameterSpec("Hello".getBytes());
        
        // byte[] bytes = new byte[16];
        // Supplier s = new Supplier("Hello");
        

        // new SecureRandom().nextBytes(bytes);
        // System.out.println(new Supplier().toString());
        // IvParameterSpec ivSpec = new IvParameterSpec(new Supplier().toBytes());
        String val="";
        for(int i = 0; i<9; i++){
            val+=(char)(65+i);
        }
        IvParameterSpec ivSpec = new IvParameterSpec(val.getBytes());
        String val2 = "ABCDEFGHI";
        IvParameterSpec ivSpec2 = new IvParameterSpec(val2.getBytes());
        System.out.println("Hello "+val);
        System.out.println("Hello "+val2);
    }
}
