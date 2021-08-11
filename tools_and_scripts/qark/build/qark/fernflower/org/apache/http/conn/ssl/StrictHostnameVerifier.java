package org.apache.http.conn.ssl;

import javax.net.ssl.SSLException;

@Deprecated
public class StrictHostnameVerifier extends AbstractVerifier {
   public static final StrictHostnameVerifier INSTANCE = new StrictHostnameVerifier();

   public final String toString() {
      return "STRICT";
   }

   public final void verify(String var1, String[] var2, String[] var3) throws SSLException {
      this.verify(var1, var2, var3, true);
   }
}
