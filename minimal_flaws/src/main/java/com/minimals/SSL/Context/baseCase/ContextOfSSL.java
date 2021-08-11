package com.minimals.SSL.Context.baseCase;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;

public class ContextOfSSL {
    public static void main(String[] args) {
        try {
            SSLContext context = SSLContext.getInstance("SSL");
            System.out.println(context.getProtocol());
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            System.out.println("Error");
        }
        
    }
}
