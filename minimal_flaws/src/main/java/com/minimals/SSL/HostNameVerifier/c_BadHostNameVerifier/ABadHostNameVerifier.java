package com.minimals.SSL.HostNameVerifier.c_BadHostNameVerifier;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public interface ABadHostNameVerifier extends HostnameVerifier {
    
    public boolean verify(String hostname, SSLSession session);

}
