package com.minimals.SSL.HostNameVerifier.a_BadHostNameVerifier;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class BadHostName{

    public static void main(String[] args) {
        new HostnameVerifier(){
        
            @Override
            public boolean verify(String hostname, SSLSession session) {
                // TODO Auto-generated method stub
                return true;
            }
        };
    }
}
