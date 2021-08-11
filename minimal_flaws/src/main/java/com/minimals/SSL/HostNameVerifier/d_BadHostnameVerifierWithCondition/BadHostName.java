package com.minimals.SSL.HostNameVerifier.d_BadHostNameVerifierWithCondition;
import javax.net.ssl.SSLSession;

public class BadHostName{
    public static void main(String[] args) {
        new ABadHostNameVerifier(){
            @Override
            public boolean verify(String hostname, SSLSession session) {
                if(true || session.getCipherSuite().length()>=0){
                    return true;
                }
                return false;
            }
        };
    }
}
