package org.apache.http.client.utils;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.ParserCursor;
import org.apache.http.message.TokenParser;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

public class URLEncodedUtils {
   public static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
   private static final String NAME_VALUE_SEPARATOR = "=";
   private static final BitSet PATHSAFE;
   private static final char PATH_SEPARATOR = '/';
   private static final BitSet PATH_SEPARATORS;
   private static final BitSet PATH_SPECIAL;
   private static final BitSet PUNCT;
   private static final char QP_SEP_A = '&';
   private static final char QP_SEP_S = ';';
   private static final int RADIX = 16;
   private static final BitSet RESERVED;
   private static final BitSet UNRESERVED;
   private static final BitSet URIC;
   private static final BitSet URLENCODER;
   private static final BitSet USERINFO;

   static {
      BitSet var1 = new BitSet(256);
      PATH_SEPARATORS = var1;
      var1.set(47);
      UNRESERVED = new BitSet(256);
      PUNCT = new BitSet(256);
      USERINFO = new BitSet(256);
      PATHSAFE = new BitSet(256);
      URIC = new BitSet(256);
      RESERVED = new BitSet(256);
      URLENCODER = new BitSet(256);
      PATH_SPECIAL = new BitSet(256);

      int var0;
      for(var0 = 97; var0 <= 122; ++var0) {
         UNRESERVED.set(var0);
      }

      for(var0 = 65; var0 <= 90; ++var0) {
         UNRESERVED.set(var0);
      }

      for(var0 = 48; var0 <= 57; ++var0) {
         UNRESERVED.set(var0);
      }

      UNRESERVED.set(95);
      UNRESERVED.set(45);
      UNRESERVED.set(46);
      UNRESERVED.set(42);
      URLENCODER.or(UNRESERVED);
      UNRESERVED.set(33);
      UNRESERVED.set(126);
      UNRESERVED.set(39);
      UNRESERVED.set(40);
      UNRESERVED.set(41);
      PUNCT.set(44);
      PUNCT.set(59);
      PUNCT.set(58);
      PUNCT.set(36);
      PUNCT.set(38);
      PUNCT.set(43);
      PUNCT.set(61);
      USERINFO.or(UNRESERVED);
      USERINFO.or(PUNCT);
      PATHSAFE.or(UNRESERVED);
      PATHSAFE.set(59);
      PATHSAFE.set(58);
      PATHSAFE.set(64);
      PATHSAFE.set(38);
      PATHSAFE.set(61);
      PATHSAFE.set(43);
      PATHSAFE.set(36);
      PATHSAFE.set(44);
      PATH_SPECIAL.or(PATHSAFE);
      PATH_SPECIAL.set(47);
      RESERVED.set(59);
      RESERVED.set(47);
      RESERVED.set(63);
      RESERVED.set(58);
      RESERVED.set(64);
      RESERVED.set(38);
      RESERVED.set(61);
      RESERVED.set(43);
      RESERVED.set(36);
      RESERVED.set(44);
      RESERVED.set(91);
      RESERVED.set(93);
      URIC.or(RESERVED);
      URIC.or(UNRESERVED);
   }

   private static List createEmptyList() {
      return new ArrayList(0);
   }

   private static String decodeFormFields(String var0, String var1) {
      if (var0 == null) {
         return null;
      } else {
         Charset var2;
         if (var1 != null) {
            var2 = Charset.forName(var1);
         } else {
            var2 = Consts.UTF_8;
         }

         return urlDecode(var0, var2, true);
      }
   }

   private static String decodeFormFields(String var0, Charset var1) {
      if (var0 == null) {
         return null;
      } else {
         if (var1 == null) {
            var1 = Consts.UTF_8;
         }

         return urlDecode(var0, var1, true);
      }
   }

   static String encPath(String var0, Charset var1) {
      return urlEncode(var0, var1, PATH_SPECIAL, false);
   }

   static String encUric(String var0, Charset var1) {
      return urlEncode(var0, var1, URIC, false);
   }

   static String encUserInfo(String var0, Charset var1) {
      return urlEncode(var0, var1, USERINFO, false);
   }

   private static String encodeFormFields(String var0, String var1) {
      if (var0 == null) {
         return null;
      } else {
         Charset var2;
         if (var1 != null) {
            var2 = Charset.forName(var1);
         } else {
            var2 = Consts.UTF_8;
         }

         return urlEncode(var0, var2, URLENCODER, true);
      }
   }

   private static String encodeFormFields(String var0, Charset var1) {
      if (var0 == null) {
         return null;
      } else {
         if (var1 == null) {
            var1 = Consts.UTF_8;
         }

         return urlEncode(var0, var1, URLENCODER, true);
      }
   }

   public static String format(Iterable var0, char var1, Charset var2) {
      Args.notNull(var0, "Parameters");
      StringBuilder var3 = new StringBuilder();
      Iterator var7 = var0.iterator();

      while(var7.hasNext()) {
         NameValuePair var5 = (NameValuePair)var7.next();
         String var4 = encodeFormFields(var5.getName(), var2);
         String var6 = encodeFormFields(var5.getValue(), var2);
         if (var3.length() > 0) {
            var3.append(var1);
         }

         var3.append(var4);
         if (var6 != null) {
            var3.append("=");
            var3.append(var6);
         }
      }

      return var3.toString();
   }

   public static String format(Iterable var0, Charset var1) {
      return format(var0, '&', var1);
   }

   public static String format(List var0, char var1, String var2) {
      StringBuilder var3 = new StringBuilder();
      Iterator var7 = var0.iterator();

      while(var7.hasNext()) {
         NameValuePair var5 = (NameValuePair)var7.next();
         String var4 = encodeFormFields(var5.getName(), var2);
         String var6 = encodeFormFields(var5.getValue(), var2);
         if (var3.length() > 0) {
            var3.append(var1);
         }

         var3.append(var4);
         if (var6 != null) {
            var3.append("=");
            var3.append(var6);
         }
      }

      return var3.toString();
   }

   public static String format(List var0, String var1) {
      return format(var0, '&', var1);
   }

   public static String formatSegments(Iterable var0, Charset var1) {
      Args.notNull(var0, "Segments");
      StringBuilder var2 = new StringBuilder();
      Iterator var4 = var0.iterator();

      while(var4.hasNext()) {
         String var3 = (String)var4.next();
         var2.append('/');
         var2.append(urlEncode(var3, var1, PATHSAFE, false));
      }

      return var2.toString();
   }

   public static String formatSegments(String... var0) {
      return formatSegments(Arrays.asList(var0), Consts.UTF_8);
   }

   public static boolean isEncoded(HttpEntity var0) {
      Args.notNull(var0, "HTTP entity");
      Header var1 = var0.getContentType();
      if (var1 != null) {
         HeaderElement[] var2 = var1.getElements();
         if (var2.length > 0) {
            return var2[0].getName().equalsIgnoreCase("application/x-www-form-urlencoded");
         }
      }

      return false;
   }

   public static List parse(String var0, Charset var1) {
      if (var0 == null) {
         return createEmptyList();
      } else {
         CharArrayBuffer var2 = new CharArrayBuffer(var0.length());
         var2.append(var0);
         return parse(var2, var1, '&', ';');
      }
   }

   public static List parse(String var0, Charset var1, char... var2) {
      if (var0 == null) {
         return createEmptyList();
      } else {
         CharArrayBuffer var3 = new CharArrayBuffer(var0.length());
         var3.append(var0);
         return parse(var3, var1, var2);
      }
   }

   @Deprecated
   public static List parse(URI var0, String var1) {
      Charset var2;
      if (var1 != null) {
         var2 = Charset.forName(var1);
      } else {
         var2 = null;
      }

      return parse(var0, var2);
   }

   public static List parse(URI var0, Charset var1) {
      Args.notNull(var0, "URI");
      String var2 = var0.getRawQuery();
      return var2 != null && !var2.isEmpty() ? parse(var2, var1) : createEmptyList();
   }

   public static List parse(HttpEntity param0) throws IOException {
      // $FF: Couldn't be decompiled
   }

   public static List parse(CharArrayBuffer var0, Charset var1, char... var2) {
      Args.notNull(var0, "Char array buffer");
      TokenParser var6 = TokenParser.INSTANCE;
      BitSet var7 = new BitSet();
      int var4 = var2.length;

      for(int var3 = 0; var3 < var4; ++var3) {
         var7.set(var2[var3]);
      }

      ParserCursor var8 = new ParserCursor(0, var0.length());
      ArrayList var9 = new ArrayList();

      while(!var8.atEnd()) {
         var7.set(61);
         String var10 = var6.parseToken(var0, var8, var7);
         String var5 = null;
         String var11 = var5;
         if (!var8.atEnd()) {
            char var12 = var0.charAt(var8.getPos());
            var8.updatePos(var8.getPos() + 1);
            var11 = var5;
            if (var12 == '=') {
               var7.clear(61);
               var5 = var6.parseToken(var0, var8, var7);
               var11 = var5;
               if (!var8.atEnd()) {
                  var8.updatePos(var8.getPos() + 1);
                  var11 = var5;
               }
            }
         }

         if (!var10.isEmpty()) {
            var9.add(new BasicNameValuePair(decodeFormFields(var10, var1), decodeFormFields(var11, var1)));
         }
      }

      return var9;
   }

   @Deprecated
   public static void parse(List var0, Scanner var1, String var2) {
      parse(var0, var1, "[&;]", var2);
   }

   @Deprecated
   public static void parse(List var0, Scanner var1, String var2, String var3) {
      var1.useDelimiter(var2);

      String var5;
      for(; var1.hasNext(); var0.add(new BasicNameValuePair(var2, var5))) {
         var5 = var1.next();
         int var4 = var5.indexOf("=");
         if (var4 != -1) {
            var2 = decodeFormFields(var5.substring(0, var4).trim(), var3);
            var5 = decodeFormFields(var5.substring(var4 + 1).trim(), var3);
         } else {
            var2 = decodeFormFields(var5.trim(), var3);
            var5 = null;
         }
      }

   }

   public static List parsePathSegments(CharSequence var0) {
      return parsePathSegments(var0, Consts.UTF_8);
   }

   public static List parsePathSegments(CharSequence var0, Charset var1) {
      Args.notNull(var0, "Char sequence");
      List var3 = splitPathSegments(var0);

      for(int var2 = 0; var2 < var3.size(); ++var2) {
         String var4 = (String)var3.get(var2);
         Charset var5;
         if (var1 != null) {
            var5 = var1;
         } else {
            var5 = Consts.UTF_8;
         }

         var3.set(var2, urlDecode(var4, var5, false));
      }

      return var3;
   }

   static List splitPathSegments(CharSequence var0) {
      return splitSegments(var0, PATH_SEPARATORS);
   }

   static List splitSegments(CharSequence var0, BitSet var1) {
      ParserCursor var3 = new ParserCursor(0, var0.length());
      if (var3.atEnd()) {
         return Collections.emptyList();
      } else {
         if (var1.get(var0.charAt(var3.getPos()))) {
            var3.updatePos(var3.getPos() + 1);
         }

         ArrayList var4 = new ArrayList();

         StringBuilder var5;
         for(var5 = new StringBuilder(); !var3.atEnd(); var3.updatePos(var3.getPos() + 1)) {
            char var2 = var0.charAt(var3.getPos());
            if (var1.get(var2)) {
               var4.add(var5.toString());
               var5.setLength(0);
            } else {
               var5.append(var2);
            }
         }

         var4.add(var5.toString());
         return var4;
      }
   }

   private static String urlDecode(String var0, Charset var1, boolean var2) {
      if (var0 == null) {
         return null;
      } else {
         ByteBuffer var7 = ByteBuffer.allocate(var0.length());
         CharBuffer var8 = CharBuffer.wrap(var0);

         while(true) {
            while(true) {
               while(var8.hasRemaining()) {
                  char var5 = var8.get();
                  if (var5 == '%' && var8.remaining() >= 2) {
                     char var3 = var8.get();
                     char var4 = var8.get();
                     int var9 = Character.digit(var3, 16);
                     int var6 = Character.digit(var4, 16);
                     if (var9 != -1 && var6 != -1) {
                        var7.put((byte)((var9 << 4) + var6));
                     } else {
                        var7.put((byte)37);
                        var7.put((byte)var3);
                        var7.put((byte)var4);
                     }
                  } else if (var2 && var5 == '+') {
                     var7.put((byte)32);
                  } else {
                     var7.put((byte)var5);
                  }
               }

               var7.flip();
               return var1.decode(var7).toString();
            }
         }
      }
   }

   private static String urlEncode(String var0, Charset var1, BitSet var2, boolean var3) {
      if (var0 == null) {
         return null;
      } else {
         StringBuilder var7 = new StringBuilder();
         ByteBuffer var8 = var1.encode(var0);

         while(true) {
            while(var8.hasRemaining()) {
               int var6 = var8.get() & 255;
               if (var2.get(var6)) {
                  var7.append((char)var6);
               } else if (var3 && var6 == 32) {
                  var7.append('+');
               } else {
                  var7.append("%");
                  char var4 = Character.toUpperCase(Character.forDigit(var6 >> 4 & 15, 16));
                  char var5 = Character.toUpperCase(Character.forDigit(var6 & 15, 16));
                  var7.append(var4);
                  var7.append(var5);
               }
            }

            return var7.toString();
         }
      }
   }
}
