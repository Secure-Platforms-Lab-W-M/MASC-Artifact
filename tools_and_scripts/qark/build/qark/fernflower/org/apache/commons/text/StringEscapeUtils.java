package org.apache.commons.text;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import org.apache.commons.text.translate.AggregateTranslator;
import org.apache.commons.text.translate.CharSequenceTranslator;
import org.apache.commons.text.translate.CsvTranslators;
import org.apache.commons.text.translate.EntityArrays;
import org.apache.commons.text.translate.JavaUnicodeEscaper;
import org.apache.commons.text.translate.LookupTranslator;
import org.apache.commons.text.translate.NumericEntityEscaper;
import org.apache.commons.text.translate.NumericEntityUnescaper;
import org.apache.commons.text.translate.OctalUnescaper;
import org.apache.commons.text.translate.UnicodeUnescaper;
import org.apache.commons.text.translate.UnicodeUnpairedSurrogateRemover;

public class StringEscapeUtils {
   public static final CharSequenceTranslator ESCAPE_CSV;
   public static final CharSequenceTranslator ESCAPE_ECMASCRIPT;
   public static final CharSequenceTranslator ESCAPE_HTML3;
   public static final CharSequenceTranslator ESCAPE_HTML4;
   public static final CharSequenceTranslator ESCAPE_JAVA;
   public static final CharSequenceTranslator ESCAPE_JSON;
   public static final CharSequenceTranslator ESCAPE_XML10;
   public static final CharSequenceTranslator ESCAPE_XML11;
   public static final CharSequenceTranslator ESCAPE_XSI;
   public static final CharSequenceTranslator UNESCAPE_CSV;
   public static final CharSequenceTranslator UNESCAPE_ECMASCRIPT;
   public static final CharSequenceTranslator UNESCAPE_HTML3;
   public static final CharSequenceTranslator UNESCAPE_HTML4;
   public static final CharSequenceTranslator UNESCAPE_JAVA;
   public static final CharSequenceTranslator UNESCAPE_JSON;
   public static final CharSequenceTranslator UNESCAPE_XML;
   public static final CharSequenceTranslator UNESCAPE_XSI;

   static {
      HashMap var0 = new HashMap();
      var0.put("\"", "\\\"");
      var0.put("\\", "\\\\");
      ESCAPE_JAVA = new AggregateTranslator(new CharSequenceTranslator[]{new LookupTranslator(Collections.unmodifiableMap(var0)), new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE), JavaUnicodeEscaper.outsideOf(32, 127)});
      var0 = new HashMap();
      var0.put("'", "\\'");
      var0.put("\"", "\\\"");
      var0.put("\\", "\\\\");
      var0.put("/", "\\/");
      ESCAPE_ECMASCRIPT = new AggregateTranslator(new CharSequenceTranslator[]{new LookupTranslator(Collections.unmodifiableMap(var0)), new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE), JavaUnicodeEscaper.outsideOf(32, 127)});
      var0 = new HashMap();
      var0.put("\"", "\\\"");
      var0.put("\\", "\\\\");
      var0.put("/", "\\/");
      ESCAPE_JSON = new AggregateTranslator(new CharSequenceTranslator[]{new LookupTranslator(Collections.unmodifiableMap(var0)), new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE), JavaUnicodeEscaper.outsideOf(32, 126)});
      var0 = new HashMap();
      var0.put("\u0000", "");
      var0.put("\u0001", "");
      var0.put("\u0002", "");
      var0.put("\u0003", "");
      var0.put("\u0004", "");
      var0.put("\u0005", "");
      var0.put("\u0006", "");
      var0.put("\u0007", "");
      var0.put("\b", "");
      var0.put("\u000b", "");
      var0.put("\f", "");
      var0.put("\u000e", "");
      var0.put("\u000f", "");
      var0.put("\u0010", "");
      var0.put("\u0011", "");
      var0.put("\u0012", "");
      var0.put("\u0013", "");
      var0.put("\u0014", "");
      var0.put("\u0015", "");
      var0.put("\u0016", "");
      var0.put("\u0017", "");
      var0.put("\u0018", "");
      var0.put("\u0019", "");
      var0.put("\u001a", "");
      var0.put("\u001b", "");
      var0.put("\u001c", "");
      var0.put("\u001d", "");
      var0.put("\u001e", "");
      var0.put("\u001f", "");
      var0.put("\ufffe", "");
      var0.put("\uffff", "");
      ESCAPE_XML10 = new AggregateTranslator(new CharSequenceTranslator[]{new LookupTranslator(EntityArrays.BASIC_ESCAPE), new LookupTranslator(EntityArrays.APOS_ESCAPE), new LookupTranslator(Collections.unmodifiableMap(var0)), NumericEntityEscaper.between(127, 132), NumericEntityEscaper.between(134, 159), new UnicodeUnpairedSurrogateRemover()});
      var0 = new HashMap();
      var0.put("\u0000", "");
      var0.put("\u000b", "&#11;");
      var0.put("\f", "&#12;");
      var0.put("\ufffe", "");
      var0.put("\uffff", "");
      ESCAPE_XML11 = new AggregateTranslator(new CharSequenceTranslator[]{new LookupTranslator(EntityArrays.BASIC_ESCAPE), new LookupTranslator(EntityArrays.APOS_ESCAPE), new LookupTranslator(Collections.unmodifiableMap(var0)), NumericEntityEscaper.between(1, 8), NumericEntityEscaper.between(14, 31), NumericEntityEscaper.between(127, 132), NumericEntityEscaper.between(134, 159), new UnicodeUnpairedSurrogateRemover()});
      ESCAPE_HTML3 = new AggregateTranslator(new CharSequenceTranslator[]{new LookupTranslator(EntityArrays.BASIC_ESCAPE), new LookupTranslator(EntityArrays.ISO8859_1_ESCAPE)});
      ESCAPE_HTML4 = new AggregateTranslator(new CharSequenceTranslator[]{new LookupTranslator(EntityArrays.BASIC_ESCAPE), new LookupTranslator(EntityArrays.ISO8859_1_ESCAPE), new LookupTranslator(EntityArrays.HTML40_EXTENDED_ESCAPE)});
      ESCAPE_CSV = new CsvTranslators.CsvEscaper();
      var0 = new HashMap();
      var0.put("|", "\\|");
      var0.put("&", "\\&");
      var0.put(";", "\\;");
      var0.put("<", "\\<");
      var0.put(">", "\\>");
      var0.put("(", "\\(");
      var0.put(")", "\\)");
      var0.put("$", "\\$");
      var0.put("`", "\\`");
      var0.put("\\", "\\\\");
      var0.put("\"", "\\\"");
      var0.put("'", "\\'");
      var0.put(" ", "\\ ");
      var0.put("\t", "\\\t");
      var0.put("\r\n", "");
      var0.put("\n", "");
      var0.put("*", "\\*");
      var0.put("?", "\\?");
      var0.put("[", "\\[");
      var0.put("#", "\\#");
      var0.put("~", "\\~");
      var0.put("=", "\\=");
      var0.put("%", "\\%");
      ESCAPE_XSI = new LookupTranslator(Collections.unmodifiableMap(var0));
      var0 = new HashMap();
      var0.put("\\\\", "\\");
      var0.put("\\\"", "\"");
      var0.put("\\'", "'");
      var0.put("\\", "");
      AggregateTranslator var1 = new AggregateTranslator(new CharSequenceTranslator[]{new OctalUnescaper(), new UnicodeUnescaper(), new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_UNESCAPE), new LookupTranslator(Collections.unmodifiableMap(var0))});
      UNESCAPE_JAVA = var1;
      UNESCAPE_ECMASCRIPT = var1;
      UNESCAPE_JSON = var1;
      UNESCAPE_HTML3 = new AggregateTranslator(new CharSequenceTranslator[]{new LookupTranslator(EntityArrays.BASIC_UNESCAPE), new LookupTranslator(EntityArrays.ISO8859_1_UNESCAPE), new NumericEntityUnescaper(new NumericEntityUnescaper.OPTION[0])});
      UNESCAPE_HTML4 = new AggregateTranslator(new CharSequenceTranslator[]{new LookupTranslator(EntityArrays.BASIC_UNESCAPE), new LookupTranslator(EntityArrays.ISO8859_1_UNESCAPE), new LookupTranslator(EntityArrays.HTML40_EXTENDED_UNESCAPE), new NumericEntityUnescaper(new NumericEntityUnescaper.OPTION[0])});
      UNESCAPE_XML = new AggregateTranslator(new CharSequenceTranslator[]{new LookupTranslator(EntityArrays.BASIC_UNESCAPE), new LookupTranslator(EntityArrays.APOS_UNESCAPE), new NumericEntityUnescaper(new NumericEntityUnescaper.OPTION[0])});
      UNESCAPE_CSV = new CsvTranslators.CsvUnescaper();
      UNESCAPE_XSI = new StringEscapeUtils.XsiUnescaper();
   }

   public static StringEscapeUtils.Builder builder(CharSequenceTranslator var0) {
      return new StringEscapeUtils.Builder(var0);
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

   public static final String escapeXSI(String var0) {
      return ESCAPE_XSI.translate(var0);
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

   public static final String unescapeXSI(String var0) {
      return UNESCAPE_XSI.translate(var0);
   }

   public static final String unescapeXml(String var0) {
      return UNESCAPE_XML.translate(var0);
   }

   public static final class Builder {
      // $FF: renamed from: sb java.lang.StringBuilder
      private final StringBuilder field_37;
      private final CharSequenceTranslator translator;

      private Builder(CharSequenceTranslator var1) {
         this.field_37 = new StringBuilder();
         this.translator = var1;
      }

      // $FF: synthetic method
      Builder(CharSequenceTranslator var1, Object var2) {
         this(var1);
      }

      public StringEscapeUtils.Builder append(String var1) {
         this.field_37.append(var1);
         return this;
      }

      public StringEscapeUtils.Builder escape(String var1) {
         this.field_37.append(this.translator.translate(var1));
         return this;
      }

      public String toString() {
         return this.field_37.toString();
      }
   }

   static class XsiUnescaper extends CharSequenceTranslator {
      private static final char BACKSLASH = '\\';

      public int translate(CharSequence var1, int var2, Writer var3) throws IOException {
         if (var2 == 0) {
            String var5 = var1.toString();
            var2 = 0;
            int var4 = 0;

            while(true) {
               var4 = var5.indexOf(92, var4);
               if (var4 == -1) {
                  if (var2 < var5.length()) {
                     var3.write(var5.substring(var2));
                  }

                  return Character.codePointCount(var1, 0, var1.length());
               }

               if (var4 > var2) {
                  var3.write(var5.substring(var2, var4));
               }

               var2 = var4 + 1;
               var4 += 2;
            }
         } else {
            throw new IllegalStateException("XsiUnescaper should never reach the [1] index");
         }
      }
   }
}
