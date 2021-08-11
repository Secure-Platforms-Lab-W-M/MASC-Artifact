package org.apache.http.impl.cookie;

import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.protocol.HttpContext;

public class NetscapeDraftSpecProvider implements CookieSpecProvider {
   private volatile CookieSpec cookieSpec;
   private final String[] datepatterns;

   public NetscapeDraftSpecProvider() {
      this((String[])null);
   }

   public NetscapeDraftSpecProvider(String[] var1) {
      this.datepatterns = var1;
   }

   public CookieSpec create(HttpContext var1) {
      if (this.cookieSpec == null) {
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         label144: {
            try {
               if (this.cookieSpec == null) {
                  this.cookieSpec = new NetscapeDraftSpec(this.datepatterns);
               }
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label144;
            }

            label141:
            try {
               return this.cookieSpec;
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label141;
            }
         }

         while(true) {
            Throwable var14 = var10000;

            try {
               throw var14;
            } catch (Throwable var11) {
               var10000 = var11;
               var10001 = false;
               continue;
            }
         }
      } else {
         return this.cookieSpec;
      }
   }
}
