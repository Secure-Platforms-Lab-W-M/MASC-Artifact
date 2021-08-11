package org.apache.commons.codec;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Charsets {
   @Deprecated
   public static final Charset ISO_8859_1;
   @Deprecated
   public static final Charset US_ASCII;
   @Deprecated
   public static final Charset UTF_16;
   @Deprecated
   public static final Charset UTF_16BE;
   @Deprecated
   public static final Charset UTF_16LE;
   @Deprecated
   public static final Charset UTF_8;

   static {
      ISO_8859_1 = StandardCharsets.ISO_8859_1;
      US_ASCII = StandardCharsets.US_ASCII;
      UTF_16 = StandardCharsets.UTF_16;
      UTF_16BE = StandardCharsets.UTF_16BE;
      UTF_16LE = StandardCharsets.UTF_16LE;
      UTF_8 = StandardCharsets.UTF_8;
   }

   public static Charset toCharset(String var0) {
      return var0 == null ? Charset.defaultCharset() : Charset.forName(var0);
   }

   public static Charset toCharset(Charset var0) {
      return var0 == null ? Charset.defaultCharset() : var0;
   }
}
