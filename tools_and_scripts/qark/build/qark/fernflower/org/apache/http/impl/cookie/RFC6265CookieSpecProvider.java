package org.apache.http.impl.cookie;

import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.protocol.HttpContext;

public class RFC6265CookieSpecProvider implements CookieSpecProvider {
   private final RFC6265CookieSpecProvider.CompatibilityLevel compatibilityLevel;
   private volatile CookieSpec cookieSpec;
   private final PublicSuffixMatcher publicSuffixMatcher;

   public RFC6265CookieSpecProvider() {
      this(RFC6265CookieSpecProvider.CompatibilityLevel.RELAXED, (PublicSuffixMatcher)null);
   }

   public RFC6265CookieSpecProvider(PublicSuffixMatcher var1) {
      this(RFC6265CookieSpecProvider.CompatibilityLevel.RELAXED, var1);
   }

   public RFC6265CookieSpecProvider(RFC6265CookieSpecProvider.CompatibilityLevel var1, PublicSuffixMatcher var2) {
      if (var1 == null) {
         var1 = RFC6265CookieSpecProvider.CompatibilityLevel.RELAXED;
      }

      this.compatibilityLevel = var1;
      this.publicSuffixMatcher = var2;
   }

   public CookieSpec create(HttpContext var1) {
      if (this.cookieSpec == null) {
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         label381: {
            label380: {
               int var2;
               try {
                  if (this.cookieSpec != null) {
                     break label380;
                  }

                  var2 = null.$SwitchMap$org$apache$http$impl$cookie$RFC6265CookieSpecProvider$CompatibilityLevel[this.compatibilityLevel.ordinal()];
               } catch (Throwable var44) {
                  var10000 = var44;
                  var10001 = false;
                  break label381;
               }

               if (var2 != 1) {
                  if (var2 != 2) {
                     try {
                        this.cookieSpec = new RFC6265LaxSpec(new CommonCookieAttributeHandler[]{new BasicPathHandler(), PublicSuffixDomainFilter.decorate(new BasicDomainHandler(), this.publicSuffixMatcher), new LaxMaxAgeHandler(), new BasicSecureHandler(), new LaxExpiresHandler()});
                     } catch (Throwable var43) {
                        var10000 = var43;
                        var10001 = false;
                        break label381;
                     }
                  } else {
                     try {
                        this.cookieSpec = new RFC6265LaxSpec(new CommonCookieAttributeHandler[]{new BasicPathHandler() {
                           public void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException {
                           }
                        }, PublicSuffixDomainFilter.decorate(new BasicDomainHandler(), this.publicSuffixMatcher), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicExpiresHandler(RFC6265StrictSpec.DATE_PATTERNS)});
                     } catch (Throwable var42) {
                        var10000 = var42;
                        var10001 = false;
                        break label381;
                     }
                  }
               } else {
                  try {
                     this.cookieSpec = new RFC6265StrictSpec(new CommonCookieAttributeHandler[]{new BasicPathHandler(), PublicSuffixDomainFilter.decorate(new BasicDomainHandler(), this.publicSuffixMatcher), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicExpiresHandler(RFC6265StrictSpec.DATE_PATTERNS)});
                  } catch (Throwable var41) {
                     var10000 = var41;
                     var10001 = false;
                     break label381;
                  }
               }
            }

            label367:
            try {
               return this.cookieSpec;
            } catch (Throwable var40) {
               var10000 = var40;
               var10001 = false;
               break label367;
            }
         }

         while(true) {
            Throwable var45 = var10000;

            try {
               throw var45;
            } catch (Throwable var39) {
               var10000 = var39;
               var10001 = false;
               continue;
            }
         }
      } else {
         return this.cookieSpec;
      }
   }

   public static enum CompatibilityLevel {
      IE_MEDIUM_SECURITY,
      RELAXED,
      STRICT;

      static {
         RFC6265CookieSpecProvider.CompatibilityLevel var0 = new RFC6265CookieSpecProvider.CompatibilityLevel("IE_MEDIUM_SECURITY", 2);
         IE_MEDIUM_SECURITY = var0;
      }
   }
}
