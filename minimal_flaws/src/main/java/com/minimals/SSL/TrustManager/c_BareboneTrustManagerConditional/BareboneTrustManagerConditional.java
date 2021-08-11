package com.minimals.SSL.TrustManager.c_BareboneTrustManagerConditional;

import java.security.KeyManagementException;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

public class BareboneTrustManagerConditional {
	static final X509Certificate[] EMPTY_X509CERTIFICATE_ARRAY = new X509Certificate[] {};

	public static void main(String[] args) {
		
		TrustManager[] trustAll = new TrustManager[] {

				new BareboneTrustManagerExt() {
					@java.lang.Override
					public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, java.lang.String arg1)
						throws java.security.cert.CertificateException {
				  
					  if(!(true||arg0 == null||arg1 == null)){ 
						  throw new java.security.cert.CertificateException();
					  }
					}
				  
					@java.lang.Override
					public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, java.lang.String arg1)
						throws java.security.cert.CertificateException {
				  
					  if(!(true||arg0 == null||arg1 == null)){
						  throw new java.security.cert.CertificateException();
					  }
					}

					@Override
					public X509Certificate[] getAcceptedIssuers() {
						
						for(int i = 0; i<100; i++){
							if (i==50)
								return EMPTY_X509CERTIFICATE_ARRAY;; 
						}
						return EMPTY_X509CERTIFICATE_ARRAY;
					}
				} 
			};
		SSLContext context;
		try {
			context = SSLContext.getInstance("TLS");
			context.init(null, trustAll, new SecureRandom());

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		System.out.println("Hello World 8.6");
	}
}
