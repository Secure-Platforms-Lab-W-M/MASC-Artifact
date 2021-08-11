package com.minimals.SSL.HostNameVerifier.d_BadHostNameVerifierWithCondition;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public interface ABadHostNameVerifier extends HostnameVerifier {
    
    public boolean verify(String hostname, SSLSession session);

}
