package org.apache.http.client.protocol;

import java.io.IOException;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

public class ResponseProcessCookies implements HttpResponseInterceptor {
   private final Log log = LogFactory.getLog(this.getClass());

   private static String formatCooke(Cookie var0) {
      StringBuilder var3 = new StringBuilder();
      var3.append(var0.getName());
      var3.append("=\"");
      String var2 = var0.getValue();
      if (var2 != null) {
         String var1 = var2;
         if (var2.length() > 100) {
            StringBuilder var4 = new StringBuilder();
            var4.append(var2.substring(0, 100));
            var4.append("...");
            var1 = var4.toString();
         }

         var3.append(var1);
      }

      var3.append("\"");
      var3.append(", version:");
      var3.append(Integer.toString(var0.getVersion()));
      var3.append(", domain:");
      var3.append(var0.getDomain());
      var3.append(", path:");
      var3.append(var0.getPath());
      var3.append(", expiry:");
      var3.append(var0.getExpiryDate());
      return var3.toString();
   }

   private void processCookies(HeaderIterator var1, CookieSpec var2, CookieOrigin var3, CookieStore var4) {
      label50:
      while(var1.hasNext()) {
         Header var5 = var1.nextHeader();

         MalformedCookieException var10000;
         label45: {
            Iterator var6;
            boolean var10001;
            try {
               var6 = var2.parse(var5, var3).iterator();
            } catch (MalformedCookieException var14) {
               var10000 = var14;
               var10001 = false;
               break label45;
            }

            while(true) {
               Cookie var7;
               try {
                  if (!var6.hasNext()) {
                     continue label50;
                  }

                  var7 = (Cookie)var6.next();
               } catch (MalformedCookieException var13) {
                  var10000 = var13;
                  var10001 = false;
                  break;
               }

               try {
                  var2.validate(var7, var3);
                  var4.addCookie(var7);
                  if (this.log.isDebugEnabled()) {
                     Log var18 = this.log;
                     StringBuilder var19 = new StringBuilder();
                     var19.append("Cookie accepted [");
                     var19.append(formatCooke(var7));
                     var19.append("]");
                     var18.debug(var19.toString());
                  }
               } catch (MalformedCookieException var12) {
                  MalformedCookieException var8 = var12;

                  try {
                     if (this.log.isWarnEnabled()) {
                        Log var9 = this.log;
                        StringBuilder var10 = new StringBuilder();
                        var10.append("Cookie rejected [");
                        var10.append(formatCooke(var7));
                        var10.append("] ");
                        var10.append(var8.getMessage());
                        var9.warn(var10.toString());
                     }
                  } catch (MalformedCookieException var11) {
                     var10000 = var11;
                     var10001 = false;
                     break;
                  }
               }
            }
         }

         MalformedCookieException var15 = var10000;
         if (this.log.isWarnEnabled()) {
            Log var16 = this.log;
            StringBuilder var17 = new StringBuilder();
            var17.append("Invalid cookie header: \"");
            var17.append(var5);
            var17.append("\". ");
            var17.append(var15.getMessage());
            var16.warn(var17.toString());
         }
      }

   }

   public void process(HttpResponse var1, HttpContext var2) throws HttpException, IOException {
      Args.notNull(var1, "HTTP request");
      Args.notNull(var2, "HTTP context");
      HttpClientContext var4 = HttpClientContext.adapt(var2);
      CookieSpec var5 = var4.getCookieSpec();
      if (var5 == null) {
         this.log.debug("Cookie spec not specified in HTTP context");
      } else {
         CookieStore var3 = var4.getCookieStore();
         if (var3 == null) {
            this.log.debug("Cookie store not specified in HTTP context");
         } else {
            CookieOrigin var6 = var4.getCookieOrigin();
            if (var6 == null) {
               this.log.debug("Cookie origin not specified in HTTP context");
            } else {
               this.processCookies(var1.headerIterator("Set-Cookie"), var5, var6, var3);
               if (var5.getVersion() > 0) {
                  this.processCookies(var1.headerIterator("Set-Cookie2"), var5, var6, var3);
               }

            }
         }
      }
   }
}
