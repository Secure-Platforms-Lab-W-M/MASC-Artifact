package org.apache.http.impl.cookie;

import java.util.Iterator;
import java.util.List;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie2;
import org.apache.http.message.ParserCursor;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

public class DefaultCookieSpec implements CookieSpec {
   private final NetscapeDraftSpec netscapeDraft;
   private final RFC2109Spec obsoleteStrict;
   private final RFC2965Spec strict;

   public DefaultCookieSpec() {
      this((String[])null, false);
   }

   DefaultCookieSpec(RFC2965Spec var1, RFC2109Spec var2, NetscapeDraftSpec var3) {
      this.strict = var1;
      this.obsoleteStrict = var2;
      this.netscapeDraft = var3;
   }

   public DefaultCookieSpec(String[] var1, boolean var2) {
      this.strict = new RFC2965Spec(var2, new CommonCookieAttributeHandler[]{new RFC2965VersionAttributeHandler(), new BasicPathHandler(), new RFC2965DomainAttributeHandler(), new RFC2965PortAttributeHandler(), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicCommentHandler(), new RFC2965CommentUrlAttributeHandler(), new RFC2965DiscardAttributeHandler()});
      this.obsoleteStrict = new RFC2109Spec(var2, new CommonCookieAttributeHandler[]{new RFC2109VersionHandler(), new BasicPathHandler(), new RFC2109DomainHandler(), new BasicMaxAgeHandler(), new BasicSecureHandler(), new BasicCommentHandler()});
      BasicDomainHandler var3 = new BasicDomainHandler();
      BasicPathHandler var4 = new BasicPathHandler();
      BasicSecureHandler var5 = new BasicSecureHandler();
      BasicCommentHandler var6 = new BasicCommentHandler();
      if (var1 != null) {
         var1 = (String[])var1.clone();
      } else {
         var1 = new String[]{"EEE, dd-MMM-yy HH:mm:ss z"};
      }

      this.netscapeDraft = new NetscapeDraftSpec(new CommonCookieAttributeHandler[]{var3, var4, var5, var6, new BasicExpiresHandler(var1)});
   }

   public List formatCookies(List var1) {
      Args.notNull(var1, "List of cookies");
      int var2 = Integer.MAX_VALUE;
      boolean var3 = true;

      int var4;
      for(Iterator var5 = var1.iterator(); var5.hasNext(); var2 = var4) {
         Cookie var6 = (Cookie)var5.next();
         if (!(var6 instanceof SetCookie2)) {
            var3 = false;
         }

         var4 = var2;
         if (var6.getVersion() < var2) {
            var4 = var6.getVersion();
         }
      }

      if (var2 > 0) {
         if (var3) {
            return this.strict.formatCookies(var1);
         } else {
            return this.obsoleteStrict.formatCookies(var1);
         }
      } else {
         return this.netscapeDraft.formatCookies(var1);
      }
   }

   public int getVersion() {
      return this.strict.getVersion();
   }

   public Header getVersionHeader() {
      return null;
   }

   public boolean match(Cookie var1, CookieOrigin var2) {
      Args.notNull(var1, "Cookie");
      Args.notNull(var2, "Cookie origin");
      if (var1.getVersion() > 0) {
         return var1 instanceof SetCookie2 ? this.strict.match(var1, var2) : this.obsoleteStrict.match(var1, var2);
      } else {
         return this.netscapeDraft.match(var1, var2);
      }
   }

   public List parse(Header var1, CookieOrigin var2) throws MalformedCookieException {
      Args.notNull(var1, "Header");
      Args.notNull(var2, "Cookie origin");
      HeaderElement[] var7 = var1.getElements();
      boolean var5 = false;
      boolean var4 = false;
      int var6 = var7.length;

      for(int var3 = 0; var3 < var6; ++var3) {
         HeaderElement var8 = var7[var3];
         if (var8.getParameterByName("version") != null) {
            var5 = true;
         }

         if (var8.getParameterByName("expires") != null) {
            var4 = true;
         }
      }

      if (!var4 && var5) {
         if ("Set-Cookie2".equals(var1.getName())) {
            return this.strict.parse(var7, var2);
         } else {
            return this.obsoleteStrict.parse(var7, var2);
         }
      } else {
         NetscapeDraftHeaderParser var9 = NetscapeDraftHeaderParser.DEFAULT;
         CharArrayBuffer var10;
         ParserCursor var13;
         if (var1 instanceof FormattedHeader) {
            CharArrayBuffer var12 = ((FormattedHeader)var1).getBuffer();
            ParserCursor var15 = new ParserCursor(((FormattedHeader)var1).getValuePos(), var12.length());
            var10 = var12;
            var13 = var15;
         } else {
            String var14 = var1.getValue();
            if (var14 == null) {
               throw new MalformedCookieException("Header value is null");
            }

            var10 = new CharArrayBuffer(var14.length());
            var10.append(var14);
            var13 = new ParserCursor(0, var10.length());
         }

         HeaderElement var11 = var9.parseHeader(var10, var13);
         return this.netscapeDraft.parse(new HeaderElement[]{var11}, var2);
      }
   }

   public String toString() {
      return "default";
   }

   public void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      Args.notNull(var2, "Cookie origin");
      if (var1.getVersion() > 0) {
         if (var1 instanceof SetCookie2) {
            this.strict.validate(var1, var2);
         } else {
            this.obsoleteStrict.validate(var1, var2);
         }
      } else {
         this.netscapeDraft.validate(var1, var2);
      }
   }
}
