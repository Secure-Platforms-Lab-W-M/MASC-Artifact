package org.apache.commons.lang3;

import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.EntityArrays;
import org.apache.commons.lang3.text.translate.JavaUnicodeEscaper;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.apache.commons.lang3.text.translate.NumericEntityEscaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.OctalUnescaper;
import org.apache.commons.lang3.text.translate.UnicodeUnescaper;
import org.apache.commons.lang3.text.translate.UnicodeUnpairedSurrogateRemover;

@Deprecated
public class StringEscapeUtils {
   public static final CharSequenceTranslator ESCAPE_CSV;
   public static final CharSequenceTranslator ESCAPE_ECMASCRIPT = new AggregateTranslator(new CharSequenceTranslator[]{new LookupTranslator(new String[][]{{"'", "\\'"}, {"\"", "\\\""}, {"\\", "\\\\"}, {"/", "\\/"}}), new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE()), JavaUnicodeEscaper.outsideOf(32, 127)});
   public static final CharSequenceTranslator ESCAPE_HTML3;
   public static final CharSequenceTranslator ESCAPE_HTML4;
   public static final CharSequenceTranslator ESCAPE_JAVA = (new LookupTranslator(new String[][]{{"\"", "\\\""}, {"\\", "\\\\"}})).with(new CharSequenceTranslator[]{new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE())}).with(JavaUnicodeEscaper.outsideOf(32, 127));
   public static final CharSequenceTranslator ESCAPE_JSON = new AggregateTranslator(new CharSequenceTranslator[]{new LookupTranslator(new String[][]{{"\"", "\\\""}, {"\\", "\\\\"}, {"/", "\\/"}}), new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE()), JavaUnicodeEscaper.outsideOf(32, 127)});
   @Deprecated
   public static final CharSequenceTranslator ESCAPE_XML = new AggregateTranslator(new CharSequenceTranslator[]{new LookupTranslator(EntityArrays.BASIC_ESCAPE()), new LookupTranslator(EntityArrays.APOS_ESCAPE())});
   public static final CharSequenceTranslator ESCAPE_XML10;
   public static final CharSequenceTranslator ESCAPE_XML11;
   public static final CharSequenceTranslator UNESCAPE_CSV;
   public static final CharSequenceTranslator UNESCAPE_ECMASCRIPT;
   public static final CharSequenceTranslator UNESCAPE_HTML3;
   public static final CharSequenceTranslator UNESCAPE_HTML4;
   public static final CharSequenceTranslator UNESCAPE_JAVA;
   public static final CharSequenceTranslator UNESCAPE_JSON;
   public static final CharSequenceTranslator UNESCAPE_XML;

   static {
      LookupTranslator var0 = new LookupTranslator(EntityArrays.BASIC_ESCAPE());
      LookupTranslator var1 = new LookupTranslator(EntityArrays.APOS_ESCAPE());
      String[] var2 = new String[]{"\u0000", ""};
      String[] var3 = new String[]{"\u0001", ""};
      String[] var4 = new String[]{"\u0002", ""};
      String[] var5 = new String[]{"\u0003", ""};
      String[] var6 = new String[]{"\u0004", ""};
      String[] var7 = new String[]{"\u0005", ""};
      String[] var8 = new String[]{"\u0007", ""};
      String[] var9 = new String[]{"\b", ""};
      String[] var10 = new String[]{"\u000b", ""};
      String[] var11 = new String[]{"\f", ""};
      String[] var12 = new String[]{"\u0010", ""};
      String[] var13 = new String[]{"\u0012", ""};
      String[] var14 = new String[]{"\u0013", ""};
      String[] var15 = new String[]{"\u0016", ""};
      String[] var16 = new String[]{"\u0017", ""};
      String[] var17 = new String[]{"\u0018", ""};
      String[] var18 = new String[]{"\u0019", ""};
      String[] var19 = new String[]{"\u001a", ""};
      String[] var20 = new String[]{"\u001c", ""};
      String[] var21 = new String[]{"\u001d", ""};
      String[] var22 = new String[]{"\u001e", ""};
      String[] var23 = new String[]{"\u001f", ""};
      String[] var24 = new String[]{"\uffff", ""};
      ESCAPE_XML10 = new AggregateTranslator(new CharSequenceTranslator[]{var0, var1, new LookupTranslator(new String[][]{var2, var3, var4, var5, var6, var7, {"\u0006", ""}, var8, var9, var10, var11, {"\u000e", ""}, {"\u000f", ""}, var12, {"\u0011", ""}, var13, var14, {"\u0014", ""}, {"\u0015", ""}, var15, var16, var17, var18, var19, {"\u001b", ""}, var20, var21, var22, var23, {"\ufffe", ""}, var24}), NumericEntityEscaper.between(127, 132), NumericEntityEscaper.between(134, 159), new UnicodeUnpairedSurrogateRemover()});
      ESCAPE_XML11 = new AggregateTranslator(new CharSequenceTranslator[]{new LookupTranslator(EntityArrays.BASIC_ESCAPE()), new LookupTranslator(EntityArrays.APOS_ESCAPE()), new LookupTranslator(new String[][]{{"\u0000", ""}, {"\u000b", "&#11;"}, {"\f", "&#12;"}, {"\ufffe", ""}, {"\uffff", ""}}), NumericEntityEscaper.between(1, 8), NumericEntityEscaper.between(14, 31), NumericEntityEscaper.between(127, 132), NumericEntityEscaper.between(134, 159), new UnicodeUnpairedSurrogateRemover()});
      ESCAPE_HTML3 = new AggregateTranslator(new CharSequenceTranslator[]{new LookupTranslator(EntityArrays.BASIC_ESCAPE()), new LookupTranslator(EntityArrays.ISO8859_1_ESCAPE())});
      ESCAPE_HTML4 = new AggregateTranslator(new CharSequenceTranslator[]{new LookupTranslator(EntityArrays.BASIC_ESCAPE()), new LookupTranslator(EntityArrays.ISO8859_1_ESCAPE()), new LookupTranslator(EntityArrays.HTML40_EXTENDED_ESCAPE())});
      ESCAPE_CSV = new StringEscapeUtils.CsvEscaper();
      OctalUnescaper var25 = new OctalUnescaper();
      UnicodeUnescaper var27 = new UnicodeUnescaper();
      LookupTranslator var28 = new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_UNESCAPE());
      var3 = new String[]{"\\\\", "\\"};
      var4 = new String[]{"\\\"", "\""};
      var5 = new String[]{"\\", ""};
      AggregateTranslator var26 = new AggregateTranslator(new CharSequenceTranslator[]{var25, var27, var28, new LookupTranslator(new String[][]{var3, var4, {"\\'", "'"}, var5})});
      UNESCAPE_JAVA = var26;
      UNESCAPE_ECMASCRIPT = var26;
      UNESCAPE_JSON = var26;
      UNESCAPE_HTML3 = new AggregateTranslator(new CharSequenceTranslator[]{new LookupTranslator(EntityArrays.BASIC_UNESCAPE()), new LookupTranslator(EntityArrays.ISO8859_1_UNESCAPE()), new NumericEntityUnescaper(new NumericEntityUnescaper.OPTION[0])});
      UNESCAPE_HTML4 = new AggregateTranslator(new CharSequenceTranslator[]{new LookupTranslator(EntityArrays.BASIC_UNESCAPE()), new LookupTranslator(EntityArrays.ISO8859_1_UNESCAPE()), new LookupTranslator(EntityArrays.HTML40_EXTENDED_UNESCAPE()), new NumericEntityUnescaper(new NumericEntityUnescaper.OPTION[0])});
      UNESCAPE_XML = new AggregateTranslator(new CharSequenceTranslator[]{new LookupTranslator(EntityArrays.BASIC_UNESCAPE()), new LookupTranslator(EntityArrays.APOS_UNESCAPE()), new NumericEntityUnescaper(new NumericEntityUnescaper.OPTION[0])});
      UNESCAPE_CSV = new StringEscapeUtils.CsvUnescaper();
   }

   public static final String escapeCsv(String var0) {
      return ESCAPE_CSV.translate(var0);
   }

   public static final String escapeEcmaScript(String var0) {
      return ESCAPE_ECMASCRIPT.translate(var0);
   }

   public static final String escapeHtml3(String var0) {
      return ESCAPE_HTML3.translate(var0);
   }

   public static final String escapeHtml4(String var0) {
      return ESCAPE_HTML4.translate(var0);
   }

   public static final String escapeJava(String var0) {
      return ESCAPE_JAVA.translate(var0);
   }

   public static final String escapeJson(String var0) {
      return ESCAPE_JSON.translate(var0);
   }

   @Deprecated
   public static final String escapeXml(String var0) {
      return ESCAPE_XML.translate(var0);
   }

   public static String escapeXml10(String var0) {
      return ESCAPE_XML10.translate(var0);
   }

   public static String escapeXml11(String var0) {
      return ESCAPE_XML11.translate(var0);
   }

   public static final String unescapeCsv(String var0) {
      return UNESCAPE_CSV.translate(var0);
   }

   public static final String unescapeEcmaScript(String var0) {
      return UNESCAPE_ECMASCRIPT.translate(var0);
   }

   public static final String unescapeHtml3(String var0) {
      return UNESCAPE_HTML3.translate(var0);
   }

   public static final String unescapeHtml4(String var0) {
      return UNESCAPE_HTML4.translate(var0);
   }

   public static final String unescapeJava(String var0) {
      return UNESCAPE_JAVA.translate(var0);
   }

   public static final String unescapeJson(String var0) {
      return UNESCAPE_JSON.translate(var0);
   }

   public static final String unescapeXml(String var0) {
      return UNESCAPE_XML.translate(var0);
   }

   static class CsvEscaper extends CharSequenceTranslator {
      private static final char CSV_DELIMITER = ',';
      private static final char CSV_QUOTE = '"';
      private static final String CSV_QUOTE_STR = String.valueOf('"');
      private static final char[] CSV_SEARCH_CHARS = new char[]{',', '"', '\r', '\n'};

      public int translate(CharSequence var1, int var2, Writer var3) throws IOException {
         if (var2 == 0) {
            if (StringUtils.containsNone(var1.toString(), (char[])CSV_SEARCH_CHARS)) {
               var3.write(var1.toString());
            } else {
               var3.write(34);
               String var4 = var1.toString();
               String var5 = CSV_QUOTE_STR;
               StringBuilder var6 = new StringBuilder();
               var6.append(CSV_QUOTE_STR);
               var6.append(CSV_QUOTE_STR);
               var3.write(StringUtils.replace(var4, var5, var6.toString()));
               var3.write(34);
            }

            return Character.codePointCount(var1, 0, var1.length());
         } else {
            throw new IllegalStateException("CsvEscaper should never reach the [1] index");
         }
      }
   }

   static class CsvUnescaper extends CharSequenceTranslator {
      private static final char CSV_DELIMITER = ',';
      private static final char CSV_QUOTE = '"';
      private static final String CSV_QUOTE_STR = String.valueOf('"');
      private static final char[] CSV_SEARCH_CHARS = new char[]{',', '"', '\r', '\n'};

      public int translate(CharSequence var1, int var2, Writer var3) throws IOException {
         if (var2 == 0) {
            if (var1.charAt(0) == '"' && var1.charAt(var1.length() - 1) == '"') {
               String var4 = var1.subSequence(1, var1.length() - 1).toString();
               if (StringUtils.containsAny(var4, (char[])CSV_SEARCH_CHARS)) {
                  StringBuilder var5 = new StringBuilder();
                  var5.append(CSV_QUOTE_STR);
                  var5.append(CSV_QUOTE_STR);
                  var3.write(StringUtils.replace(var4, var5.toString(), CSV_QUOTE_STR));
               } else {
                  var3.write(var1.toString());
               }

               return Character.codePointCount(var1, 0, var1.length());
            } else {
               var3.write(var1.toString());
               return Character.codePointCount(var1, 0, var1.length());
            }
         } else {
            throw new IllegalStateException("CsvUnescaper should never reach the [1] index");
         }
      }
   }
}
