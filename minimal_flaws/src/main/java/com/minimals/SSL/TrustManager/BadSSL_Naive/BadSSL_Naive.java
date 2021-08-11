package com.minimals.SSL.TrustManager.BadSSL_Naive;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLConnection;

import javax.net.ssl.*;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class BadSSL_Naive {

    public static final String userURL = "https://self-signed.badssl.com";

    private static X509TrustManager getX509TrustManager(){
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

    public static void main(final String[] args) {
        try {
            
            TrustManager[] trustAll = new TrustManager[]{
                getX509TrustManager()
            };

            SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, trustAll, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
            // HttpsURLConnection.setDefaultHostnameVerifier(new DefaultHostnameVerifier());
            final URLConnection conn = new URL(userURL).openConnection();

            if (conn != null) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String input;

                    while ((input = br.readLine()) != null) {
                        System.out.println(input);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
