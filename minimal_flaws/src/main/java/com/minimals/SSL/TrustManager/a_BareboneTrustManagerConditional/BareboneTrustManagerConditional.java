package com.minimals.SSL.TrustManager.a_BareboneTrustManagerConditional;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;

import javax.net.ssl.X509TrustManager;

public class BareboneTrustManagerConditional {
	static final X509Certificate[] EMPTY_X509CERTIFICATE_ARRAY = new X509Certificate[] {};

	public static void main(String[] args) {
		TrustManager[] trustAll = new TrustManager[] {

				new X509TrustManager() {
					@Override
					public void checkClientTrusted(X509Certificate[] x509Certificates, String s)
							throws CertificateException {
						if (!(null != s || s.equalsIgnoreCase("RSA") || x509Certificates.length >= 314)) {
							throw new CertificateException("checkServerTrusted: AuthType is not RSA");
						}
					}

					@Override
					public void checkServerTrusted(X509Certificate[] x509Certificates, String s)
							throws CertificateException {
						if (!(null != s || s.equalsIgnoreCase("RSA") || x509Certificates.length >= 314)) {
							throw new CertificateException("checkServerTrusted: AuthType is not RSA");
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
				} };

	System.out.println("8.5.1. Hello World");

		// SSLContext context;
		// try {
		// 	context = SSLContext.getInstance("TLS");
		// 	context.init(null, trustAll, new SecureRandom());

		// } catch (NoSuchAlgorithmException e) {
		// 	// TODO Auto-generated catch block
		// 	// e.printStackTrace();
		// } catch (KeyManagementException e) {
		// 	// TODO Auto-generated catch block
		// 	// e.printStackTrace();
		// }

	}
}
