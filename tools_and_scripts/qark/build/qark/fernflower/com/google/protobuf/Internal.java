package com.google.protobuf;

import java.io.UnsupportedEncodingException;

public class Internal {
   public static ByteString bytesDefaultValue(String var0) {
      try {
         ByteString var2 = ByteString.copyFrom(var0.getBytes("ISO-8859-1"));
         return var2;
      } catch (UnsupportedEncodingException var1) {
         throw new IllegalStateException("Java VM does not support a standard character set.", var1);
      }
   }

   public static boolean isValidUtf8(ByteString var0) {
      return var0.isValidUtf8();
   }

   public static String stringDefaultValue(String var0) {
      try {
         var0 = new String(var0.getBytes("ISO-8859-1"), "UTF-8");
         return var0;
      } catch (UnsupportedEncodingException var1) {
         throw new IllegalStateException("Java VM does not support a standard character set.", var1);
      }
   }

   public interface EnumLite {
      int getNumber();
   }

   public interface EnumLiteMap {
      Internal.EnumLite findValueByNumber(int var1);
   }
}
