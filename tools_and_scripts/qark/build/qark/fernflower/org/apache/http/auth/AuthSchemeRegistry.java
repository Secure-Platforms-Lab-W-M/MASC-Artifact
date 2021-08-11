package org.apache.http.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpRequest;
import org.apache.http.config.Lookup;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Deprecated
public final class AuthSchemeRegistry implements Lookup {
   private final ConcurrentHashMap registeredSchemes = new ConcurrentHashMap();

   public AuthScheme getAuthScheme(String var1, HttpParams var2) throws IllegalStateException {
      Args.notNull(var1, "Name");
      AuthSchemeFactory var3 = (AuthSchemeFactory)this.registeredSchemes.get(var1.toLowerCase(Locale.ENGLISH));
      if (var3 != null) {
         return var3.newInstance(var2);
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("Unsupported authentication scheme: ");
         var4.append(var1);
         throw new IllegalStateException(var4.toString());
      }
   }

   public List getSchemeNames() {
      return new ArrayList(this.registeredSchemes.keySet());
   }

   public AuthSchemeProvider lookup(final String var1) {
      return new AuthSchemeProvider() {
         public AuthScheme create(HttpContext var1x) {
            HttpRequest var2 = (HttpRequest)var1x.getAttribute("http.request");
            return AuthSchemeRegistry.this.getAuthScheme(var1, var2.getParams());
         }
      };
   }

   public void register(String var1, AuthSchemeFactory var2) {
      Args.notNull(var1, "Name");
      Args.notNull(var2, "Authentication scheme factory");
      this.registeredSchemes.put(var1.toLowerCase(Locale.ENGLISH), var2);
   }

   public void setItems(Map var1) {
      if (var1 != null) {
         this.registeredSchemes.clear();
         this.registeredSchemes.putAll(var1);
      }
   }

   public void unregister(String var1) {
      Args.notNull(var1, "Name");
      this.registeredSchemes.remove(var1.toLowerCase(Locale.ENGLISH));
   }
}
