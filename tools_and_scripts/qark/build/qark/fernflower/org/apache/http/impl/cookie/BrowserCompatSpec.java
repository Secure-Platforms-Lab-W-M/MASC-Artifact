package org.apache.http.impl.cookie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.message.BasicHeaderElement;
import org.apache.http.message.BasicHeaderValueFormatter;
import org.apache.http.message.BufferedHeader;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@Deprecated
public class BrowserCompatSpec extends CookieSpecBase {
   private static final String[] DEFAULT_DATE_PATTERNS = new String[]{"EEE, dd MMM yyyy HH:mm:ss zzz", "EEE, dd-MMM-yy HH:mm:ss zzz", "EEE MMM d HH:mm:ss yyyy", "EEE, dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MMM-yyyy HH-mm-ss z", "EEE, dd MMM yy HH:mm:ss z", "EEE dd-MMM-yyyy HH:mm:ss z", "EEE dd MMM yyyy HH:mm:ss z", "EEE dd-MMM-yyyy HH-mm-ss z", "EEE dd-MMM-yy HH:mm:ss z", "EEE dd MMM yy HH:mm:ss z", "EEE,dd-MMM-yy HH:mm:ss z", "EEE,dd-MMM-yyyy HH:mm:ss z", "EEE, dd-MM-yyyy HH:mm:ss z"};

   public BrowserCompatSpec() {
      this((String[])null, BrowserCompatSpecFactory.SecurityLevel.SECURITYLEVEL_DEFAULT);
   }

   public BrowserCompatSpec(String[] var1) {
      this(var1, BrowserCompatSpecFactory.SecurityLevel.SECURITYLEVEL_DEFAULT);
   }

   public BrowserCompatSpec(String[] var1, BrowserCompatSpecFactory.SecurityLevel var2) {
      BrowserCompatVersionAttributeHandler var3 = new BrowserCompatVersionAttributeHandler();
      BasicDomainHandler var4 = new BasicDomainHandler();
      BasicPathHandler var8;
      if (var2 == BrowserCompatSpecFactory.SecurityLevel.SECURITYLEVEL_IE_MEDIUM) {
         var8 = new BasicPathHandler() {
            public void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException {
            }
         };
      } else {
         var8 = new BasicPathHandler();
      }

      BasicMaxAgeHandler var5 = new BasicMaxAgeHandler();
      BasicSecureHandler var6 = new BasicSecureHandler();
      BasicCommentHandler var7 = new BasicCommentHandler();
      if (var1 != null) {
         var1 = (String[])var1.clone();
      } else {
         var1 = DEFAULT_DATE_PATTERNS;
      }

      super(var3, var4, var8, var5, var6, var7, new BasicExpiresHandler(var1));
   }

   private static boolean isQuoteEnclosed(String var0) {
      return var0 != null && var0.startsWith("\"") && var0.endsWith("\"");
   }

   public List formatCookies(List var1) {
      Args.notEmpty(var1, "List of cookies");
      CharArrayBuffer var3 = new CharArrayBuffer(var1.size() * 20);
      var3.append("Cookie");
      var3.append(": ");

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         Cookie var4 = (Cookie)var1.get(var2);
         if (var2 > 0) {
            var3.append("; ");
         }

         String var5 = var4.getName();
         String var6 = var4.getValue();
         if (var4.getVersion() > 0 && !isQuoteEnclosed(var6)) {
            BasicHeaderValueFormatter.INSTANCE.formatHeaderElement(var3, new BasicHeaderElement(var5, var6), false);
         } else {
            var3.append(var5);
            var3.append("=");
            if (var6 != null) {
               var3.append(var6);
            }
         }
      }

      ArrayList var7 = new ArrayList(1);
      var7.add(new BufferedHeader(var3));
      return var7;
   }

   public int getVersion() {
      return 0;
   }

   public Header getVersionHeader() {
      return null;
   }

   public List parse(Header var1, CookieOrigin var2) throws MalformedCookieException {
      Args.notNull(var1, "Header");
      Args.notNull(var2, "Cookie origin");
      if (var1.getName().equalsIgnoreCase("Set-Cookie")) {
         HeaderElement[] var7 = var1.getElements();
         boolean var5 = false;
         boolean var3 = false;
         int var6 = var7.length;

         int var4;
         for(var4 = 0; var4 < var6; ++var4) {
            HeaderElement var8 = var7[var4];
            if (var8.getParameterByName("version") != null) {
               var5 = true;
            }

            if (var8.getParameterByName("expires") != null) {
               var3 = true;
            }
         }

         if (!var3 && var5) {
            return this.parse(var7, var2);
         } else {
            NetscapeDraftHeaderParser var9 = NetscapeDraftHeaderParser.DEFAULT;
            CharArrayBuffer var10;
            ParserCursor var16;
            if (var1 instanceof FormattedHeader) {
               CharArrayBuffer var15 = ((FormattedHeader)var1).getBuffer();
               ParserCursor var18 = new ParserCursor(((FormattedHeader)var1).getValuePos(), var15.length());
               var10 = var15;
               var16 = var18;
            } else {
               String var17 = var1.getValue();
               if (var17 == null) {
                  throw new MalformedCookieException("Header value is null");
               }

               var10 = new CharArrayBuffer(var17.length());
               var10.append(var17);
               var16 = new ParserCursor(0, var10.length());
            }

            HeaderElement var20 = var9.parseHeader(var10, var16);
            String var12 = var20.getName();
            String var19 = var20.getValue();
            if (var12 != null && !var12.isEmpty()) {
               BasicClientCookie var13 = new BasicClientCookie(var12, var19);
               var13.setPath(getDefaultPath(var2));
               var13.setDomain(getDefaultDomain(var2));
               NameValuePair[] var14 = var20.getParameters();

               for(var4 = var14.length - 1; var4 >= 0; --var4) {
                  NameValuePair var21 = var14[var4];
                  var19 = var21.getName().toLowerCase(Locale.ROOT);
                  var13.setAttribute(var19, var21.getValue());
                  CookieAttributeHandler var22 = this.findAttribHandler(var19);
                  if (var22 != null) {
                     var22.parse(var13, var21.getValue());
                  }
               }

               if (var3) {
                  var13.setVersion(0);
               }

               return Collections.singletonList(var13);
            } else {
               throw new MalformedCookieException("Cookie name may not be empty");
            }
         }
      } else {
         StringBuilder var11 = new StringBuilder();
         var11.append("Unrecognized cookie header '");
         var11.append(var1.toString());
         var11.append("'");
         throw new MalformedCookieException(var11.toString());
      }
   }

   public String toString() {
      return "compatibility";
   }
}
