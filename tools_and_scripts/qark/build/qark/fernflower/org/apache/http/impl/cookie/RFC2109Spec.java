package org.apache.http.impl.cookie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookiePathComparator;
import org.apache.http.cookie.CookieRestrictionViolationException;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

public class RFC2109Spec extends CookieSpecBase {
   static final String[] DATE_PATTERNS = new String[]{"EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy"};
   private final boolean oneHeader;

   public RFC2109Spec() {
      this((String[])null, false);
   }

   protected RFC2109Spec(boolean var1, CommonCookieAttributeHandler... var2) {
      super(var2);
      this.oneHeader = var1;
   }

   public RFC2109Spec(String[] var1, boolean var2) {
      RFC2109VersionHandler var3 = new RFC2109VersionHandler();
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
      RFC2109DomainHandler var5 = new RFC2109DomainHandler();
      BasicMaxAgeHandler var6 = new BasicMaxAgeHandler();
      BasicSecureHandler var7 = new BasicSecureHandler();
      BasicCommentHandler var8 = new BasicCommentHandler();
      if (var1 != null) {
         var1 = (String[])var1.clone();
      } else {
         var1 = DATE_PATTERNS;
      }

      super(var3, var4, var5, var6, var7, var8, new BasicExpiresHandler(var1));
      this.oneHeader = var2;
   }

   private List doFormatManyHeaders(List var1) {
      ArrayList var3 = new ArrayList(var1.size());
      Iterator var6 = var1.iterator();

      while(var6.hasNext()) {
         Cookie var4 = (Cookie)var6.next();
         int var2 = var4.getVersion();
         CharArrayBuffer var5 = new CharArrayBuffer(40);
         var5.append("Cookie: ");
         var5.append("$Version=");
         var5.append(Integer.toString(var2));
         var5.append("; ");
         this.formatCookieAsVer(var5, var4, var2);
         var3.add(new BufferedHeader(var5));
      }

      return var3;
   }

   private List doFormatOneHeader(List var1) {
      int var2 = Integer.MAX_VALUE;

      int var3;
      Cookie var5;
      for(Iterator var4 = var1.iterator(); var4.hasNext(); var2 = var3) {
         var5 = (Cookie)var4.next();
         var3 = var2;
         if (var5.getVersion() < var2) {
            var3 = var5.getVersion();
         }
      }

      CharArrayBuffer var8 = new CharArrayBuffer(var1.size() * 40);
      var8.append("Cookie");
      var8.append(": ");
      var8.append("$Version=");
      var8.append(Integer.toString(var2));
      Iterator var6 = var1.iterator();

      while(var6.hasNext()) {
         var5 = (Cookie)var6.next();
         var8.append("; ");
         this.formatCookieAsVer(var8, var5, var2);
      }

      ArrayList var7 = new ArrayList(1);
      var7.add(new BufferedHeader(var8));
      return var7;
   }

   protected void formatCookieAsVer(CharArrayBuffer var1, Cookie var2, int var3) {
      this.formatParamAsVer(var1, var2.getName(), var2.getValue(), var3);
      if (var2.getPath() != null && var2 instanceof ClientCookie && ((ClientCookie)var2).containsAttribute("path")) {
         var1.append("; ");
         this.formatParamAsVer(var1, "$Path", var2.getPath(), var3);
      }

      if (var2.getDomain() != null && var2 instanceof ClientCookie && ((ClientCookie)var2).containsAttribute("domain")) {
         var1.append("; ");
         this.formatParamAsVer(var1, "$Domain", var2.getDomain(), var3);
      }

   }

   public List formatCookies(List var1) {
      Args.notEmpty((Collection)var1, "List of cookies");
      if (((List)var1).size() > 1) {
         var1 = new ArrayList((Collection)var1);
         Collections.sort((List)var1, CookiePathComparator.INSTANCE);
      }

      return this.oneHeader ? this.doFormatOneHeader((List)var1) : this.doFormatManyHeaders((List)var1);
   }

   protected void formatParamAsVer(CharArrayBuffer var1, String var2, String var3, int var4) {
      var1.append(var2);
      var1.append("=");
      if (var3 != null) {
         if (var4 > 0) {
            var1.append('"');
            var1.append(var3);
            var1.append('"');
            return;
         }

         var1.append(var3);
      }

   }

   public int getVersion() {
      return 1;
   }

   public Header getVersionHeader() {
      return null;
   }

   public List parse(Header var1, CookieOrigin var2) throws MalformedCookieException {
      Args.notNull(var1, "Header");
      Args.notNull(var2, "Cookie origin");
      if (var1.getName().equalsIgnoreCase("Set-Cookie")) {
         return this.parse(var1.getElements(), var2);
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Unrecognized cookie header '");
         var3.append(var1.toString());
         var3.append("'");
         throw new MalformedCookieException(var3.toString());
      }
   }

   public String toString() {
      return "rfc2109";
   }

   public void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      String var3 = var1.getName();
      if (var3.indexOf(32) == -1) {
         if (!var3.startsWith("$")) {
            super.validate(var1, var2);
         } else {
            throw new CookieRestrictionViolationException("Cookie name may not start with $");
         }
      } else {
         throw new CookieRestrictionViolationException("Cookie name may not contain blanks");
      }
   }
}
