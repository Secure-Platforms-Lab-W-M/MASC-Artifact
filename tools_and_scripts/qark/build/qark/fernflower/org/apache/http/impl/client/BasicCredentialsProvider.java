package org.apache.http.impl.client;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.util.Args;

public class BasicCredentialsProvider implements CredentialsProvider {
   private final ConcurrentHashMap credMap = new ConcurrentHashMap();

   private static Credentials matchCredentials(Map var0, AuthScope var1) {
      Credentials var7 = (Credentials)var0.get(var1);
      Credentials var6 = var7;
      if (var7 == null) {
         int var2 = -1;
         AuthScope var5 = null;

         int var3;
         for(Iterator var8 = var0.keySet().iterator(); var8.hasNext(); var2 = var3) {
            AuthScope var9 = (AuthScope)var8.next();
            int var4 = var1.match(var9);
            var3 = var2;
            if (var4 > var2) {
               var3 = var4;
               var5 = var9;
            }
         }

         var6 = var7;
         if (var5 != null) {
            var6 = (Credentials)var0.get(var5);
         }
      }

      return var6;
   }

   public void clear() {
      this.credMap.clear();
   }

   public Credentials getCredentials(AuthScope var1) {
      Args.notNull(var1, "Authentication scope");
      return matchCredentials(this.credMap, var1);
   }

   public void setCredentials(AuthScope var1, Credentials var2) {
      Args.notNull(var1, "Authentication scope");
      this.credMap.put(var1, var2);
   }

   public String toString() {
      return this.credMap.toString();
   }
}
