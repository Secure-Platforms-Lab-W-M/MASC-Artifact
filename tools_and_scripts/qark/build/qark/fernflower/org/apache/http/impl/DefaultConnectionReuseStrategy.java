package org.apache.http.impl;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.ProtocolVersion;
import org.apache.http.TokenIterator;
import org.apache.http.message.BasicTokenIterator;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

public class DefaultConnectionReuseStrategy implements ConnectionReuseStrategy {
   public static final DefaultConnectionReuseStrategy INSTANCE = new DefaultConnectionReuseStrategy();

   private boolean canResponseHaveBody(HttpRequest var1, HttpResponse var2) {
      boolean var5 = false;
      if (var1 != null && var1.getRequestLine().getMethod().equalsIgnoreCase("HEAD")) {
         return false;
      } else {
         int var3 = var2.getStatusLine().getStatusCode();
         boolean var4 = var5;
         if (var3 >= 200) {
            var4 = var5;
            if (var3 != 204) {
               var4 = var5;
               if (var3 != 304) {
                  var4 = var5;
                  if (var3 != 205) {
                     var4 = true;
                  }
               }
            }
         }

         return var4;
      }
   }

   protected TokenIterator createTokenIterator(HeaderIterator var1) {
      return new BasicTokenIterator(var1);
   }

   public boolean keepAlive(HttpResponse var1, HttpContext var2) {
      Args.notNull(var1, "HTTP response");
      Args.notNull(var2, "HTTP context");
      int var3;
      Header var5;
      if (var1.getStatusLine().getStatusCode() == 204) {
         var5 = var1.getFirstHeader("Content-Length");
         if (var5 != null) {
            label134: {
               try {
                  var3 = Integer.parseInt(var5.getValue());
               } catch (NumberFormatException var13) {
                  break label134;
               }

               if (var3 > 0) {
                  return false;
               }
            }
         }

         if (var1.getFirstHeader("Transfer-Encoding") != null) {
            return false;
         }
      }

      HttpRequest var15 = (HttpRequest)var2.getAttribute("http.request");
      boolean var4;
      boolean var10001;
      if (var15 != null) {
         BasicTokenIterator var21;
         try {
            var21 = new BasicTokenIterator(var15.headerIterator("Connection"));
         } catch (ParseException var12) {
            var10001 = false;
            return false;
         }

         while(true) {
            try {
               if (!var21.hasNext()) {
                  break;
               }

               var4 = "Close".equalsIgnoreCase(var21.nextToken());
            } catch (ParseException var11) {
               var10001 = false;
               return false;
            }

            if (var4) {
               return false;
            }
         }
      }

      ProtocolVersion var6 = var1.getStatusLine().getProtocolVersion();
      var5 = var1.getFirstHeader("Transfer-Encoding");
      if (var5 != null) {
         if (!"chunked".equalsIgnoreCase(var5.getValue())) {
            return false;
         }
      } else if (this.canResponseHaveBody(var15, var1)) {
         Header[] var16 = var1.getHeaders("Content-Length");
         if (var16.length != 1) {
            return false;
         }

         Header var17 = var16[0];

         try {
            var3 = Integer.parseInt(var17.getValue());
         } catch (NumberFormatException var7) {
            return false;
         }

         if (var3 < 0) {
            return false;
         }
      }

      HeaderIterator var22 = var1.headerIterator("Connection");
      HeaderIterator var18 = var22;
      if (!var22.hasNext()) {
         var18 = var1.headerIterator("Proxy-Connection");
      }

      if (var18.hasNext()) {
         BasicTokenIterator var14;
         try {
            var14 = new BasicTokenIterator(var18);
         } catch (ParseException var9) {
            var10001 = false;
            return false;
         }

         boolean var19 = false;

         while(true) {
            String var20;
            try {
               if (!var14.hasNext()) {
                  break;
               }

               var20 = var14.nextToken();
               if ("Close".equalsIgnoreCase(var20)) {
                  return false;
               }
            } catch (ParseException var10) {
               var10001 = false;
               return false;
            }

            try {
               var4 = "Keep-Alive".equalsIgnoreCase(var20);
            } catch (ParseException var8) {
               var10001 = false;
               return false;
            }

            if (var4) {
               var19 = true;
            }
         }

         if (var19) {
            return true;
         }
      }

      return var6.lessEquals(HttpVersion.HTTP_1_0) ^ true;
   }
}
