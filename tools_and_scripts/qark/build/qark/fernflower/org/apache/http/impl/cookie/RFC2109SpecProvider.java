package org.apache.http.impl.cookie;

import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.protocol.HttpContext;

public class RFC2109SpecProvider implements CookieSpecProvider {
   private volatile CookieSpec cookieSpec;
   private final boolean oneHeader;
   private final PublicSuffixMatcher publicSuffixMatcher;

   public RFC2109SpecProvider() {
      this((PublicSuffixMatcher)null, false);
   }

   public RFC2109SpecProvider(PublicSuffixMatcher var1) {
      this(var1, false);
   }

   public RFC2109SpecProvider(PublicSuffixMatcher var1, boolean var2) {
      this.oneHeader = var2;
      this.publicSuffixMatcher = var1;
   }

   public CookieSpec create(HttpContext var1) {
      if (this.cookieSpec == null) {
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         label144: {
            try {
               if (this.cookieSpec == null) {
                  this.cookieSpec = new RFC2109Spec(this.oneHeader, new CommonCookieAttributeHandler[]{new RFC2109VersionHandler(), new BasicPathHandler(), PublicSuffixDomainFilter.decorate(new RFC2109DomainHandler(), this.publicSuffixMatcher), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicCommentHandler()});
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
