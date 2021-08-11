package org.apache.http.conn.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class NoopHostnameVerifier implements HostnameVerifier {
   public static final NoopHostnameVerifier INSTANCE = new NoopHostnameVerifier();

   public final String toString() {
      return "NO_OP";
   }

   public boolean verify(String var1, SSLSession var2) {
      return true;
   }
}
