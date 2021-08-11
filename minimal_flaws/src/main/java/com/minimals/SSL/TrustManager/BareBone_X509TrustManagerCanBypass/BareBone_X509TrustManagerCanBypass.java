package com.minimals.SSL.TrustManager.BareBone_X509TrustManagerCanBypass;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class BareBone_X509TrustManagerCanBypass {
    static X509TrustManager getTrustManager(){
		return new BareBone_X509TrustManagerCanBypassExt(){

			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				// TODO Auto-generated method stub

			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				// TODO Auto-generated method stub

			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				// TODO Auto-generated method stub
				return null;
			}

        };
	}
    public static void main(String[] args) {
        getTrustManager();
        System.out.println("Hello World");
    }
}
