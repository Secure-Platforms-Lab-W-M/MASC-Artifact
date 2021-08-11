package org.apache.http.impl.cookie;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.message.BufferedHeader;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

public class NetscapeDraftSpec extends CookieSpecBase {
   protected static final String EXPIRES_PATTERN = "EEE, dd-MMM-yy HH:mm:ss z";

   public NetscapeDraftSpec() {
      this((String[])null);
   }

   public NetscapeDraftSpec(String[] var1) {
      BasicPathHandler var2 = new BasicPathHandler();
      NetscapeDomainHandler var3 = new NetscapeDomainHandler();
      BasicSecureHandler var4 = new BasicSecureHandler();
      BasicCommentHandler var5 = new BasicCommentHandler();
      if (var1 != null) {
         var1 = (String[])var1.clone();
      } else {
         var1 = new String[]{"EEE, dd-MMM-yy HH:mm:ss z"};
      }

      super(var2, var3, var4, var5, new BasicExpiresHandler(var1));
   }

   NetscapeDraftSpec(CommonCookieAttributeHandler... var1) {
      super(var1);
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

         var3.append(var4.getName());
         String var6 = var4.getValue();
         if (var6 != null) {
            var3.append("=");
            var3.append(var6);
         }
      }

      ArrayList var5 = new ArrayList(1);
      var5.add(new BufferedHeader(var3));
      return var5;
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
         NetscapeDraftHeaderParser var5 = NetscapeDraftHeaderParser.DEFAULT;
         CharArrayBuffer var6;
         ParserCursor var8;
         if (var1 instanceof FormattedHeader) {
            CharArrayBuffer var3 = ((FormattedHeader)var1).getBuffer();
            ParserCursor var4 = new ParserCursor(((FormattedHeader)var1).getValuePos(), var3.length());
            var6 = var3;
            var8 = var4;
         } else {
            String var9 = var1.getValue();
            if (var9 == null) {
               throw new MalformedCookieException("Header value is null");
            }

            var6 = new CharArrayBuffer(var9.length());
            var6.append(var9);
            var8 = new ParserCursor(0, var6.length());
         }

         return this.parse(new HeaderElement[]{var5.parseHeader(var6, var8)}, var2);
      } else {
         StringBuilder var7 = new StringBuilder();
         var7.append("Unrecognized cookie header '");
         var7.append(var1.toString());
         var7.append("'");
         throw new MalformedCookieException(var7.toString());
      }
   }

   public String toString() {
      return "netscape";
   }
}
