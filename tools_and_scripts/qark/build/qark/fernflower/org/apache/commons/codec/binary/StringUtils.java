package org.apache.commons.codec.binary;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StringUtils {
   public static boolean equals(CharSequence var0, CharSequence var1) {
      if (var0 == var1) {
         return true;
      } else if (var0 != null) {
         if (var1 == null) {
            return false;
         } else if (var0 instanceof String && var1 instanceof String) {
            return var0.equals(var1);
         } else {
            return var0.length() == var1.length() && CharSequenceUtils.regionMatches(var0, false, 0, var1, 0, var0.length());
         }
      } else {
         return false;
      }
   }

   private static ByteBuffer getByteBuffer(String var0, Charset var1) {
      return var0 == null ? null : ByteBuffer.wrap(var0.getBytes(var1));
   }

   public static ByteBuffer getByteBufferUtf8(String var0) {
      return getByteBuffer(var0, StandardCharsets.UTF_8);
   }

   private static byte[] getBytes(String var0, Charset var1) {
      return var0 == null ? null : var0.getBytes(var1);
   }

   public static byte[] getBytesIso8859_1(String var0) {
      return getBytes(var0, StandardCharsets.ISO_8859_1);
   }

   public static byte[] getBytesUnchecked(String var0, String var1) {
      if (var0 == null) {
         return null;
      } else {
         try {
            byte[] var3 = var0.getBytes(var1);
            return var3;
         } catch (UnsupportedEncodingException var2) {
            throw newIllegalStateException(var1, var2);
         }
      }
   }

   public static byte[] getBytesUsAscii(String var0) {
      return getBytes(var0, StandardCharsets.US_ASCII);
   }

   public static byte[] getBytesUtf16(String var0) {
      return getBytes(var0, StandardCharsets.UTF_16);
   }

   public static byte[] getBytesUtf16Be(String var0) {
      return getBytes(var0, StandardCharsets.UTF_16BE);
   }

   public static byte[] getBytesUtf16Le(String var0) {
      return getBytes(var0, StandardCharsets.UTF_16LE);
   }

   public static byte[] getBytesUtf8(String var0) {
      return getBytes(var0, StandardCharsets.UTF_8);
   }

   private static IllegalStateException newIllegalStateException(String var0, UnsupportedEncodingException var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append(var0);
      var2.append(": ");
      var2.append(var1);
      return new IllegalStateException(var2.toString());
   }

   public static String newString(byte[] var0, String var1) {
      if (var0 == null) {
         return null;
      } else {
         try {
            String var3 = new String(var0, var1);
            return var3;
         } catch (UnsupportedEncodingException var2) {
            throw newIllegalStateException(var1, var2);
         }
      }
   }

   private static String newString(byte[] var0, Charset var1) {
      return var0 == null ? null : new String(var0, var1);
   }

   public static String newStringIso8859_1(byte[] var0) {
      return newString(var0, StandardCharsets.ISO_8859_1);
   }

   public static String newStringUsAscii(byte[] var0) {
      return newString(var0, StandardCharsets.US_ASCII);
   }

   public static String newStringUtf16(byte[] var0) {
      return newString(var0, StandardCharsets.UTF_16);
   }

   public static String newStringUtf16Be(byte[] var0) {
      return newString(var0, StandardCharsets.UTF_16BE);
   }

   public static String newStringUtf16Le(byte[] var0) {
      return newString(var0, StandardCharsets.UTF_16LE);
   }

   public static String newStringUtf8(byte[] var0) {
      return newString(var0, StandardCharsets.UTF_8);
   }
}
