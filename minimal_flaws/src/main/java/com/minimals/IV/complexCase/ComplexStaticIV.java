package com.minimals.IV.complexCase;
import java.security.SecureRandom;

import javax.crypto.spec.IvParameterSpec;

public class ComplexStaticIV {
    public static void main(String[] args) {
        String val="";

        for(int i = 65; i < 75; i++){
            val += (char) i;
        }

        IvParameterSpec ivSpec = new IvParameterSpec(val.getBytes(),0,8);
        System.out.println(new String(ivSpec.getIV()));
        System.out.println(new String(val.getBytes()));
    }
}
