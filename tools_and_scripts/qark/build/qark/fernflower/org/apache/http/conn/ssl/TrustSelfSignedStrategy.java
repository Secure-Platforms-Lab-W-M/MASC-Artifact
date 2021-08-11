package org.apache.http.conn.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class TrustSelfSignedStrategy implements TrustStrategy {
   public static final TrustSelfSignedStrategy INSTANCE = new TrustSelfSignedStrategy();

   public boolean isTrusted(X509Certificate[] var1, String var2) throws CertificateException {
      return var1.length == 1;
   }
}
