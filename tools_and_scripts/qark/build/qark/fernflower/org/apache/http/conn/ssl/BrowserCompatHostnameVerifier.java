package org.apache.http.conn.ssl;

import javax.net.ssl.SSLException;

@Deprecated
public class BrowserCompatHostnameVerifier extends AbstractVerifier {
   public static final BrowserCompatHostnameVerifier INSTANCE = new BrowserCompatHostnameVerifier();

   public final String toString() {
      return "BROWSER_COMPATIBLE";
   }

   public final void verify(String var1, String[] var2, String[] var3) throws SSLException {
      this.verify(var1, var2, var3, false);
   }
}
