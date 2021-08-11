package com.minimals.SSL.HostNameVerifier.b_BadHostNameVerifier;
import javax.net.ssl.SSLSession;

public class BadHostName{
    public static void main(String[] args) {
        new ABadHostNameVerifier(){
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
    }
}
