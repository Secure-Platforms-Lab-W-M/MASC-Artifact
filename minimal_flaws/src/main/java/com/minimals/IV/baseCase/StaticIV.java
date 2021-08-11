package com.minimals.IV.baseCase;
import java.security.SecureRandom;

import javax.crypto.spec.IvParameterSpec;

public class StaticIV {
    public static void main(String[] args) {
        byte[] bytes = "Hello".getBytes();
        IvParameterSpec ivSpec = new IvParameterSpec(bytes);
        System.out.println(new String(ivSpec.getIV()));
        System.out.println(new String(bytes));
    }
}
