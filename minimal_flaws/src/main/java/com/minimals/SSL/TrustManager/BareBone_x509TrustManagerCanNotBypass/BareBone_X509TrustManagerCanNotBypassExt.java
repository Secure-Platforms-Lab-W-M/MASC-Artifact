package com.minimals.SSL.TrustManager.BareBone_x509TrustManagerCanNotBypass;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public abstract class BareBone_X509TrustManagerCanNotBypassExt implements X509TrustManager {
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
    }
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
    }
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
