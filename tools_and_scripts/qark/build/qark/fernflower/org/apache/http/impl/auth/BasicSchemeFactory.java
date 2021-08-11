package org.apache.http.impl.auth;

import java.nio.charset.Charset;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeFactory;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

public class BasicSchemeFactory implements AuthSchemeFactory, AuthSchemeProvider {
   private final Charset charset;

   public BasicSchemeFactory() {
      this((Charset)null);
   }

   public BasicSchemeFactory(Charset var1) {
      this.charset = var1;
   }

   public AuthScheme create(HttpContext var1) {
      return new BasicScheme(this.charset);
   }

   public AuthScheme newInstance(HttpParams var1) {
      return new BasicScheme();
   }
}
