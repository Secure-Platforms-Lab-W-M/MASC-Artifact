package org.apache.http.cookie;

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
public final class CookieSpecRegistry implements Lookup {
   private final ConcurrentHashMap registeredSpecs = new ConcurrentHashMap();

   public CookieSpec getCookieSpec(String var1) throws IllegalStateException {
      return this.getCookieSpec(var1, (HttpParams)null);
   }

   public CookieSpec getCookieSpec(String var1, HttpParams var2) throws IllegalStateException {
      Args.notNull(var1, "Name");
      CookieSpecFactory var3 = (CookieSpecFactory)this.registeredSpecs.get(var1.toLowerCase(Locale.ENGLISH));
      if (var3 != null) {
         return var3.newInstance(var2);
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("Unsupported cookie spec: ");
         var4.append(var1);
         throw new IllegalStateException(var4.toString());
      }
   }

   public List getSpecNames() {
      return new ArrayList(this.registeredSpecs.keySet());
   }

   public CookieSpecProvider lookup(final String var1) {
      return new CookieSpecProvider() {
         public CookieSpec create(HttpContext var1x) {
            HttpRequest var2 = (HttpRequest)var1x.getAttribute("http.request");
            return CookieSpecRegistry.this.getCookieSpec(var1, var2.getParams());
         }
      };
   }

   public void register(String var1, CookieSpecFactory var2) {
      Args.notNull(var1, "Name");
      Args.notNull(var2, "Cookie spec factory");
      this.registeredSpecs.put(var1.toLowerCase(Locale.ENGLISH), var2);
   }

   public void setItems(Map var1) {
      if (var1 != null) {
         this.registeredSpecs.clear();
         this.registeredSpecs.putAll(var1);
      }
   }

   public void unregister(String var1) {
      Args.notNull(var1, "Id");
      this.registeredSpecs.remove(var1.toLowerCase(Locale.ENGLISH));
   }
}
