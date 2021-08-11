package org.apache.http.impl.auth;

import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeFactory;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

public class KerberosSchemeFactory implements AuthSchemeFactory, AuthSchemeProvider {
   private final boolean stripPort;
   private final boolean useCanonicalHostname;

   public KerberosSchemeFactory() {
      this(true, true);
   }

   public KerberosSchemeFactory(boolean var1) {
      this.stripPort = var1;
      this.useCanonicalHostname = true;
   }

   public KerberosSchemeFactory(boolean var1, boolean var2) {
      this.stripPort = var1;
      this.useCanonicalHostname = var2;
   }

   public AuthScheme create(HttpContext var1) {
      return new KerberosScheme(this.stripPort, this.useCanonicalHostname);
   }

   public boolean isStripPort() {
      return this.stripPort;
   }

   public boolean isUseCanonicalHostname() {
      return this.useCanonicalHostname;
   }

   public AuthScheme newInstance(HttpParams var1) {
      return new KerberosScheme(this.stripPort, this.useCanonicalHostname);
   }
}
