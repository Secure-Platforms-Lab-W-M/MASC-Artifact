package org.apache.http.conn.ssl;

import java.security.cert.X509Certificate;
import java.util.Arrays;
import org.apache.http.util.Args;

@Deprecated
public final class PrivateKeyDetails {
   private final X509Certificate[] certChain;
   private final String type;

   public PrivateKeyDetails(String var1, X509Certificate[] var2) {
      this.type = (String)Args.notNull(var1, "Private key type");
      this.certChain = var2;
   }

   public X509Certificate[] getCertChain() {
      return this.certChain;
   }

   public String getType() {
      return this.type;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.type);
      var1.append(':');
      var1.append(Arrays.toString(this.certChain));
      return var1.toString();
   }
}
