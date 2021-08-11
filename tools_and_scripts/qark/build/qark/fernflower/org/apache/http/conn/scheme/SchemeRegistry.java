package org.apache.http.conn.scheme;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpHost;
import org.apache.http.util.Args;

@Deprecated
public final class SchemeRegistry {
   private final ConcurrentHashMap registeredSchemes = new ConcurrentHashMap();

   public final Scheme get(String var1) {
      Args.notNull(var1, "Scheme name");
      return (Scheme)this.registeredSchemes.get(var1);
   }

   public final Scheme getScheme(String var1) {
      Scheme var2 = this.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Scheme '");
         var3.append(var1);
         var3.append("' not registered.");
         throw new IllegalStateException(var3.toString());
      }
   }

   public final Scheme getScheme(HttpHost var1) {
      Args.notNull(var1, "Host");
      return this.getScheme(var1.getSchemeName());
   }

   public final List getSchemeNames() {
      return new ArrayList(this.registeredSchemes.keySet());
   }

   public final Scheme register(Scheme var1) {
      Args.notNull(var1, "Scheme");
      return (Scheme)this.registeredSchemes.put(var1.getName(), var1);
   }

   public void setItems(Map var1) {
      if (var1 != null) {
         this.registeredSchemes.clear();
         this.registeredSchemes.putAll(var1);
      }
   }

   public final Scheme unregister(String var1) {
      Args.notNull(var1, "Scheme name");
      return (Scheme)this.registeredSchemes.remove(var1);
   }
}
