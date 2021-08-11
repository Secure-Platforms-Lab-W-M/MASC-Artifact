package org.apache.http.impl.cookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.util.Args;

public abstract class CookieSpecBase extends AbstractCookieSpec {
   public CookieSpecBase() {
   }

   protected CookieSpecBase(HashMap var1) {
      super(var1);
   }

   protected CookieSpecBase(CommonCookieAttributeHandler... var1) {
      super(var1);
   }

   protected static String getDefaultDomain(CookieOrigin var0) {
      return var0.getHost();
   }

   protected static String getDefaultPath(CookieOrigin var0) {
      String var3 = var0.getPath();
      int var2 = var3.lastIndexOf(47);
      String var4 = var3;
      if (var2 >= 0) {
         int var1 = var2;
         if (var2 == 0) {
            var1 = 1;
         }

         var4 = var3.substring(0, var1);
      }

      return var4;
   }

   public boolean match(Cookie var1, CookieOrigin var2) {
      Args.notNull(var1, "Cookie");
      Args.notNull(var2, "Cookie origin");
      Iterator var3 = this.getAttribHandlers().iterator();

      do {
         if (!var3.hasNext()) {
            return true;
         }
      } while(((CookieAttributeHandler)var3.next()).match(var1, var2));

      return false;
   }

   protected List parse(HeaderElement[] var1, CookieOrigin var2) throws MalformedCookieException {
      ArrayList var6 = new ArrayList(var1.length);
      int var5 = var1.length;

      for(int var3 = 0; var3 < var5; ++var3) {
         HeaderElement var8 = var1[var3];
         String var7 = var8.getName();
         String var9 = var8.getValue();
         if (var7 != null && !var7.isEmpty()) {
            BasicClientCookie var12 = new BasicClientCookie(var7, var9);
            var12.setPath(getDefaultPath(var2));
            var12.setDomain(getDefaultDomain(var2));
            NameValuePair[] var13 = var8.getParameters();

            for(int var4 = var13.length - 1; var4 >= 0; --var4) {
               NameValuePair var14 = var13[var4];
               String var10 = var14.getName().toLowerCase(Locale.ROOT);
               var12.setAttribute(var10, var14.getValue());
               CookieAttributeHandler var11 = this.findAttribHandler(var10);
               if (var11 != null) {
                  var11.parse(var12, var14.getValue());
               }
            }

            var6.add(var12);
         }
      }

      return var6;
   }

   public void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      Args.notNull(var2, "Cookie origin");
      Iterator var3 = this.getAttribHandlers().iterator();

      while(var3.hasNext()) {
         ((CookieAttributeHandler)var3.next()).validate(var1, var2);
      }

   }
}
