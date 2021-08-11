package com.minimals.SSL.HostNameVerifier.e_BadHostNameVerifierWithGenericCondition;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public interface ABadHostNameVerifier extends HostnameVerifier {
    
    public boolean verify(String hostname, SSLSession session);

}
