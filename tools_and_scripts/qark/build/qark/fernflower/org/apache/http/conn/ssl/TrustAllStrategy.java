package org.apache.http.conn.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class TrustAllStrategy implements TrustStrategy {
   public static final TrustAllStrategy INSTANCE = new TrustAllStrategy();

   public boolean isTrusted(X509Certificate[] var1, String var2) throws CertificateException {
      return true;
   }
}
