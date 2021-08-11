package org.apache.http.impl.cookie;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieAttributeHandler;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookiePriorityComparator;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.message.BufferedHeader;
import org.apache.http.message.ParserCursor;
import org.apache.http.message.TokenParser;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

public class RFC6265CookieSpec implements CookieSpec {
   private static final char COMMA_CHAR = ',';
   private static final char DQUOTE_CHAR = '"';
   private static final char EQUAL_CHAR = '=';
   private static final char ESCAPE_CHAR = '\\';
   private static final char PARAM_DELIMITER = ';';
   private static final BitSet SPECIAL_CHARS = TokenParser.INIT_BITSET(new int[]{32, 34, 44, 59, 92});
   private static final BitSet TOKEN_DELIMS = TokenParser.INIT_BITSET(new int[]{61, 59});
   private static final BitSet VALUE_DELIMS = TokenParser.INIT_BITSET(new int[]{59});
   private final Map attribHandlerMap;
   private final CookieAttributeHandler[] attribHandlers;
   private final TokenParser tokenParser;

   protected RFC6265CookieSpec(CommonCookieAttributeHandler... var1) {
      this.attribHandlers = (CookieAttributeHandler[])var1.clone();
      this.attribHandlerMap = new ConcurrentHashMap(var1.length);
      int var3 = var1.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         CommonCookieAttributeHandler var4 = var1[var2];
         this.attribHandlerMap.put(var4.getAttributeName().toLowerCase(Locale.ROOT), var4);
      }

      this.tokenParser = TokenParser.INSTANCE;
   }

   static String getDefaultDomain(CookieOrigin var0) {
      return var0.getHost();
   }

   static String getDefaultPath(CookieOrigin var0) {
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

   boolean containsChars(CharSequence var1, BitSet var2) {
      for(int var3 = 0; var3 < var1.length(); ++var3) {
         if (var2.get(var1.charAt(var3))) {
            return true;
         }
      }

      return false;
   }

   boolean containsSpecialChar(CharSequence var1) {
      return this.containsChars(var1, SPECIAL_CHARS);
   }

   public List formatCookies(List var1) {
      Args.notEmpty((Collection)var1, "List of cookies");
      if (((List)var1).size() > 1) {
         var1 = new ArrayList((Collection)var1);
         Collections.sort((List)var1, CookiePriorityComparator.INSTANCE);
      }

      CharArrayBuffer var5 = new CharArrayBuffer(((List)var1).size() * 20);
      var5.append("Cookie");
      var5.append(": ");

      for(int var3 = 0; var3 < ((List)var1).size(); ++var3) {
         Cookie var6 = (Cookie)((List)var1).get(var3);
         if (var3 > 0) {
            var5.append(';');
            var5.append(' ');
         }

         var5.append(var6.getName());
         String var8 = var6.getValue();
         if (var8 != null) {
            var5.append('=');
            if (!this.containsSpecialChar(var8)) {
               var5.append(var8);
            } else {
               var5.append('"');

               for(int var4 = 0; var4 < var8.length(); ++var4) {
                  char var2 = var8.charAt(var4);
                  if (var2 == '"' || var2 == '\\') {
                     var5.append('\\');
                  }

                  var5.append(var2);
               }

               var5.append('"');
            }
         }
      }

      ArrayList var7 = new ArrayList(1);
      var7.add(new BufferedHeader(var5));
      return var7;
   }

   public final int getVersion() {
      return 0;
   }

   public final Header getVersionHeader() {
      return null;
   }

   public final boolean match(Cookie var1, CookieOrigin var2) {
      Args.notNull(var1, "Cookie");
      Args.notNull(var2, "Cookie origin");
      CookieAttributeHandler[] var5 = this.attribHandlers;
      int var4 = var5.length;

      for(int var3 = 0; var3 < var4; ++var3) {
         if (!var5[var3].match(var1, var2)) {
            return false;
         }
      }

      return true;
   }

   public final List parse(Header var1, CookieOrigin var2) throws MalformedCookieException {
      Args.notNull(var1, "Header");
      Args.notNull(var2, "Cookie origin");
      StringBuilder var10;
      if (var1.getName().equalsIgnoreCase("Set-Cookie")) {
         CharArrayBuffer var4;
         ParserCursor var5;
         if (var1 instanceof FormattedHeader) {
            var4 = ((FormattedHeader)var1).getBuffer();
            var5 = new ParserCursor(((FormattedHeader)var1).getValuePos(), var4.length());
         } else {
            String var15 = var1.getValue();
            if (var15 == null) {
               throw new MalformedCookieException("Header value is null");
            }

            var4 = new CharArrayBuffer(var15.length());
            var4.append(var15);
            var5 = new ParserCursor(0, var4.length());
         }

         String var6 = this.tokenParser.parseToken(var4, var5, TOKEN_DELIMS);
         if (var6.isEmpty()) {
            return Collections.emptyList();
         } else if (var5.atEnd()) {
            return Collections.emptyList();
         } else {
            char var3 = var4.charAt(var5.getPos());
            var5.updatePos(var5.getPos() + 1);
            if (var3 == '=') {
               String var9 = this.tokenParser.parseValue(var4, var5, VALUE_DELIMS);
               if (!var5.atEnd()) {
                  var5.updatePos(var5.getPos() + 1);
               }

               BasicClientCookie var17 = new BasicClientCookie(var6, var9);
               var17.setPath(getDefaultPath(var2));
               var17.setDomain(getDefaultDomain(var2));
               var17.setCreationDate(new Date());
               LinkedHashMap var7 = new LinkedHashMap();

               String var12;
               while(!var5.atEnd()) {
                  String var8 = this.tokenParser.parseToken(var4, var5, TOKEN_DELIMS).toLowerCase(Locale.ROOT);
                  var2 = null;
                  var9 = var2;
                  if (!var5.atEnd()) {
                     var3 = var4.charAt(var5.getPos());
                     var5.updatePos(var5.getPos() + 1);
                     var9 = var2;
                     if (var3 == '=') {
                        var12 = this.tokenParser.parseToken(var4, var5, VALUE_DELIMS);
                        var9 = var12;
                        if (!var5.atEnd()) {
                           var5.updatePos(var5.getPos() + 1);
                           var9 = var12;
                        }
                     }
                  }

                  var17.setAttribute(var8, var9);
                  var7.put(var8, var9);
               }

               if (var7.containsKey("max-age")) {
                  var7.remove("expires");
               }

               Iterator var11 = var7.entrySet().iterator();

               while(var11.hasNext()) {
                  Entry var14 = (Entry)var11.next();
                  var12 = (String)var14.getKey();
                  String var16 = (String)var14.getValue();
                  CookieAttributeHandler var13 = (CookieAttributeHandler)this.attribHandlerMap.get(var12);
                  if (var13 != null) {
                     var13.parse(var17, var16);
                  }
               }

               return Collections.singletonList(var17);
            } else {
               var10 = new StringBuilder();
               var10.append("Cookie value is invalid: '");
               var10.append(var1.toString());
               var10.append("'");
               throw new MalformedCookieException(var10.toString());
            }
         }
      } else {
         var10 = new StringBuilder();
         var10.append("Unrecognized cookie header: '");
         var10.append(var1.toString());
         var10.append("'");
         throw new MalformedCookieException(var10.toString());
      }
   }

   public final void validate(Cookie var1, CookieOrigin var2) throws MalformedCookieException {
      Args.notNull(var1, "Cookie");
      Args.notNull(var2, "Cookie origin");
      CookieAttributeHandler[] var5 = this.attribHandlers;
      int var4 = var5.length;

      for(int var3 = 0; var3 < var4; ++var3) {
         var5[var3].validate(var1, var2);
      }

   }
}
