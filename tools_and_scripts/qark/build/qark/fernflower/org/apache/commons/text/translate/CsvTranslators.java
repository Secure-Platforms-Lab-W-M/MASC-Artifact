package org.apache.commons.text.translate;

import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang3.StringUtils;

public final class CsvTranslators {
   private static final char CSV_DELIMITER = ',';
   private static final String CSV_ESCAPED_QUOTE_STR;
   private static final char CSV_QUOTE = '"';
   private static final String CSV_QUOTE_STR = String.valueOf('"');
   private static final char[] CSV_SEARCH_CHARS;

   static {
      StringBuilder var0 = new StringBuilder();
      var0.append(CSV_QUOTE_STR);
      var0.append(CSV_QUOTE_STR);
      CSV_ESCAPED_QUOTE_STR = var0.toString();
      CSV_SEARCH_CHARS = new char[]{',', '"', '\r', '\n'};
   }

   private CsvTranslators() {
   }

   public static class CsvEscaper extends SinglePassTranslator {
      void translateWhole(CharSequence var1, Writer var2) throws IOException {
         String var3 = var1.toString();
         if (StringUtils.containsNone(var3, (char[])CsvTranslators.CSV_SEARCH_CHARS)) {
            var2.write(var3);
         } else {
            var2.write(34);
            var2.write(StringUtils.replace(var3, CsvTranslators.CSV_QUOTE_STR, CsvTranslators.CSV_ESCAPED_QUOTE_STR));
            var2.write(34);
         }
      }
   }

   public static class CsvUnescaper extends SinglePassTranslator {
      void translateWhole(CharSequence var1, Writer var2) throws IOException {
         if (var1.charAt(0) == '"' && var1.charAt(var1.length() - 1) == '"') {
            String var3 = var1.subSequence(1, var1.length() - 1).toString();
            if (StringUtils.containsAny(var3, (char[])CsvTranslators.CSV_SEARCH_CHARS)) {
               var2.write(StringUtils.replace(var3, CsvTranslators.CSV_ESCAPED_QUOTE_STR, CsvTranslators.CSV_QUOTE_STR));
            } else {
               var2.write(var1.toString());
            }
         } else {
            var2.write(var1.toString());
         }
      }
   }
}
