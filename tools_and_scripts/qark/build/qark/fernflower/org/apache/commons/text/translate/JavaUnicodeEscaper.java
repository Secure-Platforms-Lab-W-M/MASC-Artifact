package org.apache.commons.text.translate;

public class JavaUnicodeEscaper extends UnicodeEscaper {
   public JavaUnicodeEscaper(int var1, int var2, boolean var3) {
      super(var1, var2, var3);
   }

   public static JavaUnicodeEscaper above(int var0) {
      return outsideOf(0, var0);
   }

   public static JavaUnicodeEscaper below(int var0) {
      return outsideOf(var0, Integer.MAX_VALUE);
   }

   public static JavaUnicodeEscaper between(int var0, int var1) {
      return new JavaUnicodeEscaper(var0, var1, true);
   }

   public static JavaUnicodeEscaper outsideOf(int var0, int var1) {
      return new JavaUnicodeEscaper(var0, var1, false);
   }

   protected String toUtf16Escape(int var1) {
      char[] var2 = Character.toChars(var1);
      StringBuilder var3 = new StringBuilder();
      var3.append("\\u");
      var3.append(hex(var2[0]));
      var3.append("\\u");
      var3.append(hex(var2[1]));
      return var3.toString();
   }
}
