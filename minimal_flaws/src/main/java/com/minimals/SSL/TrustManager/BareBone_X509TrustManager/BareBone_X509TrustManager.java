package com.minimals.SSL.TrustManager.BareBone_X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class BareBone_X509TrustManager{

    static X509TrustManager getTrustmanager(){
        return new X509TrustManager(){
        
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                // TODO Auto-generated method stub
                return null;
            }
        
            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                // TODO Auto-generated method stub
                
            }
        
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                // TODO Auto-generated method stub
                
            }
        };
    }
    public static void main(String[] args) {
        getTrustmanager();
    }
}