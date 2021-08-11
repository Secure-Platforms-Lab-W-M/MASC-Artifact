package org.apache.http.impl.cookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

public class RFC2965Spec extends RFC2109Spec {
   public RFC2965Spec() {
      this((String[])null, false);
   }

   RFC2965Spec(boolean var1, CommonCookieAttributeHandler... var2) {
      super(var1, var2);
   }

   public RFC2965Spec(String[] var1, boolean var2) {
      RFC2965VersionAttributeHandler var3 = new RFC2965VersionAttributeHandler();
      BasicPathHandler var4 = new BasicPathHandler() {
         public void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException {
            if (!this.match(var1, var2)) {
               StringBuilder var3 = new StringBuilder();
               var3.append("Illegal 'path' attribute \"");
               var3.append(var1.getPath());
               var3.append("\". Path of origin: \"");
               var3.append(var2.getPath());
               var3.append("\"");
               throw new CookieRestrictionViolationException(var3.toString());
            }
         }
      };
      RFC2965DomainAttributeHandler var5 = new RFC2965DomainAttributeHandler();
      RFC2965PortAttributeHandler var6 = new RFC2965PortAttributeHandler();
      BasicMaxAgeHandler var7 = new BasicMaxAgeHandler();
      BasicSecureHandler var8 = new BasicSecureHandler();
      BasicCommentHandler var9 = new BasicCommentHandler();
      if (var1 != null) {
         var1 = (String[])var1.clone();
      } else {
         var1 = DATE_PATTERNS;
      }

      super(var2, var3, var4, var5, var6, var7, var8, var9, new BasicExpiresHandler(var1), new RFC2965CommentUrlAttributeHandler(), new RFC2965DiscardAttributeHandler());
   }

   private static CookieOrigin adjustEffectiveHost(CookieOrigin var0) {
      String var4 = var0.getHost();
      boolean var3 = true;
      int var1 = 0;

      boolean var2;
      while(true) {
         var2 = var3;
         if (var1 >= var4.length()) {
            break;
         }

         char var6 = var4.charAt(var1);
         if (var6 == '.' || var6 == ':') {
            var2 = false;
            break;
         }

         ++var1;
      }

      if (var2) {
         StringBuilder var5 = new StringBuilder();
         var5.append(var4);
         var5.append(".local");
         return new CookieOrigin(var5.toString(), var0.getPort(), var0.getPath(), var0.isSecure());
      } else {
         return var0;
      }
   }

   private List createCookies(HeaderElement[] var1, CookieOrigin var2) throws MalformedCookieException {
      ArrayList var6 = new ArrayList(var1.length);
      int var5 = var1.length;

      for(int var3 = 0; var3 < var5; ++var3) {
         HeaderElement var8 = var1[var3];
         String var7 = var8.getName();
         String var9 = var8.getValue();
         if (var7 == null || var7.isEmpty()) {
            throw new MalformedCookieException("Cookie name may not be empty");
         }

         BasicClientCookie2 var11 = new BasicClientCookie2(var7, var9);
         var11.setPath(getDefaultPath(var2));
         var11.setDomain(getDefaultDomain(var2));
         var11.setPorts(new int[]{var2.getPort()});
         NameValuePair[] var12 = var8.getParameters();
         HashMap var14 = new HashMap(var12.length);

         for(int var4 = var12.length - 1; var4 >= 0; --var4) {
            NameValuePair var10 = var12[var4];
            var14.put(var10.getName().toLowerCase(Locale.ROOT), var10);
         }

         Iterator var13 = var14.entrySet().iterator();

         while(var13.hasNext()) {
            NameValuePair var15 = (NameValuePair)((Entry)var13.next()).getValue();
            String var16 = var15.getName().toLowerCase(Locale.ROOT);
            var11.setAttribute(var16, var15.getValue());
            CookieAttributeHandler var17 = this.findAttribHandler(var16);
            if (var17 != null) {
               var17.parse(var11, var15.getValue());
            }
         }

         var6.add(var11);
      }

      return var6;
   }

   protected void formatCookieAsVer(CharArrayBuffer var1, Cookie var2, int var3) {
      super.formatCookieAsVer(var1, var2, var3);
      if (var2 instanceof ClientCookie) {
         String var5 = ((ClientCookie)var2).getAttribute("port");
         if (var5 != null) {
            var1.append("; $Port");
            var1.append("=\"");
            if (!var5.trim().isEmpty()) {
               int[] var6 = var2.getPorts();
               if (var6 != null) {
                  int var4 = var6.length;

                  for(var3 = 0; var3 < var4; ++var3) {
                     if (var3 > 0) {
                        var1.append(",");
                     }

                     var1.append(Integer.toString(var6[var3]));
                  }
               }
            }

            var1.append("\"");
         }
      }

   }

   public int getVersion() {
      return 1;
   }

   public Header getVersionHeader() {
      CharArrayBuffer var1 = new CharArrayBuffer(40);
      var1.append("Cookie2");
      var1.append(": ");
      var1.append("$Version=");
      var1.append(Integer.toString(this.getVersion()));
      return new BufferedHeader(var1);
   }

   public boolean match(Cookie var1, CookieOrigin var2) {
      Args.notNull(var1, "Cookie");
      Args.notNull(var2, "Cookie origin");
      return super.match(var1, adjustEffectiveHost(var2));
   }

   public List parse(Header var1, CookieOrigin var2) throws MalformedCookieException {
      Args.notNull(var1, "Header");
      Args.notNull(var2, "Cookie origin");
      if (var1.getName().equalsIgnoreCase("Set-Cookie2")) {
         return this.createCookies(var1.getElements(), adjustEffectiveHost(var2));
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Unrecognized cookie header '");
         var3.append(var1.toString());
         var3.append("'");
         throw new MalformedCookieException(var3.toString());
      }
   }

   protected List parse(HeaderElement[] var1, CookieOrigin var2) throws MalformedCookieException {
      return this.createCookies(var1, adjustEffectiveHost(var2));
   }

   public String toString() {
      return "rfc2965";
   }

   public void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      Args.notNull(var2, "Cookie origin");
      super.validate(var1, adjustEffectiveHost(var2));
   }
}
