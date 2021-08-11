package org.apache.http.impl.cookie;

import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.protocol.HttpContext;

public class DefaultCookieSpecProvider implements CookieSpecProvider {
   private final DefaultCookieSpecProvider.CompatibilityLevel compatibilityLevel;
   private volatile CookieSpec cookieSpec;
   private final String[] datepatterns;
   private final boolean oneHeader;
   private final PublicSuffixMatcher publicSuffixMatcher;

   public DefaultCookieSpecProvider() {
      this(DefaultCookieSpecProvider.CompatibilityLevel.DEFAULT, (PublicSuffixMatcher)null, (String[])null, false);
   }

   public DefaultCookieSpecProvider(PublicSuffixMatcher var1) {
      this(DefaultCookieSpecProvider.CompatibilityLevel.DEFAULT, var1, (String[])null, false);
   }

   public DefaultCookieSpecProvider(DefaultCookieSpecProvider.CompatibilityLevel var1, PublicSuffixMatcher var2) {
      this(var1, var2, (String[])null, false);
   }

   public DefaultCookieSpecProvider(DefaultCookieSpecProvider.CompatibilityLevel var1, PublicSuffixMatcher var2, String[] var3, boolean var4) {
      if (var1 == null) {
         var1 = DefaultCookieSpecProvider.CompatibilityLevel.DEFAULT;
      }

      this.compatibilityLevel = var1;
      this.publicSuffixMatcher = var2;
      this.datepatterns = var3;
      this.oneHeader = var4;
   }

   public CookieSpec create(HttpContext var1) {
      if (this.cookieSpec == null) {
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         label563: {
            label571: {
               BasicPathHandler var64;
               RFC2965Spec var3;
               RFC2109Spec var4;
               CommonCookieAttributeHandler var5;
               label560: {
                  try {
                     if (this.cookieSpec != null) {
                        break label571;
                     }

                     var3 = new RFC2965Spec(this.oneHeader, new CommonCookieAttributeHandler[]{new RFC2965VersionAttributeHandler(), new BasicPathHandler(), PublicSuffixDomainFilter.decorate(new RFC2965DomainAttributeHandler(), this.publicSuffixMatcher), new RFC2965PortAttributeHandler(), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicCommentHandler(), new RFC2965CommentUrlAttributeHandler(), new RFC2965DiscardAttributeHandler()});
                     var4 = new RFC2109Spec(this.oneHeader, new CommonCookieAttributeHandler[]{new RFC2109VersionHandler(), new BasicPathHandler(), PublicSuffixDomainFilter.decorate(new RFC2109DomainHandler(), this.publicSuffixMatcher), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicCommentHandler()});
                     var5 = PublicSuffixDomainFilter.decorate(new BasicDomainHandler(), this.publicSuffixMatcher);
                     if (this.compatibilityLevel == DefaultCookieSpecProvider.CompatibilityLevel.IE_MEDIUM_SECURITY) {
                        var64 = new BasicPathHandler() {
                           public void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException {
                           }
                        };
                        break label560;
                     }
                  } catch (Throwable var63) {
                     var10000 = var63;
                     var10001 = false;
                     break label563;
                  }

                  try {
                     var64 = new BasicPathHandler();
                  } catch (Throwable var61) {
                     var10000 = var61;
                     var10001 = false;
                     break label563;
                  }
               }

               String[] var2;
               BasicSecureHandler var6;
               BasicCommentHandler var7;
               label550: {
                  try {
                     var6 = new BasicSecureHandler();
                     var7 = new BasicCommentHandler();
                     if (this.datepatterns != null) {
                        var2 = (String[])this.datepatterns.clone();
                        break label550;
                     }
                  } catch (Throwable var62) {
                     var10000 = var62;
                     var10001 = false;
                     break label563;
                  }

                  try {
                     var2 = new String[]{"EEE, dd-MMM-yy HH:mm:ss z"};
                  } catch (Throwable var60) {
                     var10000 = var60;
                     var10001 = false;
                     break label563;
                  }
               }

               try {
                  this.cookieSpec = new DefaultCookieSpec(var3, var4, new NetscapeDraftSpec(new CommonCookieAttributeHandler[]{var5, var64, var6, var7, new BasicExpiresHandler(var2)}));
               } catch (Throwable var59) {
                  var10000 = var59;
                  var10001 = false;
                  break label563;
               }
            }

            label536:
            try {
               return this.cookieSpec;
            } catch (Throwable var58) {
               var10000 = var58;
               var10001 = false;
               break label536;
            }
         }

         while(true) {
            Throwable var65 = var10000;

            try {
               throw var65;
            } catch (Throwable var57) {
               var10000 = var57;
               var10001 = false;
               continue;
            }
         }
      } else {
         return this.cookieSpec;
      }
   }

   public static enum CompatibilityLevel {
      DEFAULT,
      IE_MEDIUM_SECURITY;

      static {
         DefaultCookieSpecProvider.CompatibilityLevel var0 = new DefaultCookieSpecProvider.CompatibilityLevel("IE_MEDIUM_SECURITY", 1);
         IE_MEDIUM_SECURITY = var0;
      }
   }
}
