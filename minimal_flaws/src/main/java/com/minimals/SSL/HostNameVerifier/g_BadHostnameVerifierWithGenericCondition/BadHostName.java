package com.minimals.SSL.HostNameVerifier.g_BadHostNameVerifierWithGenericCondition;
import javax.net.ssl.SSLSession;

public class BadHostName{
    public static void main(String[] args) {
        new ABadHostNameVerifier(){
            @Override
            public boolean verify(String hostname, SSLSession session) {
                if(true || session == null){
                    return true;
                }
                return false;
            }
        };
    }
}
