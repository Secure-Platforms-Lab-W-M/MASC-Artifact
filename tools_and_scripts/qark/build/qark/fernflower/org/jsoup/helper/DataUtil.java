package org.jsoup.helper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

public final class DataUtil {
   private static final int UNICODE_BOM = 65279;
   static final int boundaryLength = 32;
   private static final int bufferSize = 131072;
   private static final Pattern charsetPattern = Pattern.compile("(?i)\\bcharset=\\s*(?:\"|')?([^\\s,;\"']*)");
   static final String defaultCharset = "UTF-8";
   private static final char[] mimeBoundaryChars = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

   private DataUtil() {
   }

   static void crossStreams(InputStream var0, OutputStream var1) throws IOException {
      byte[] var3 = new byte[131072];

      while(true) {
         int var2 = var0.read(var3);
         if (var2 == -1) {
            return;
         }

         var1.write(var3, 0, var2);
      }
   }

   static ByteBuffer emptyByteBuffer() {
      return ByteBuffer.allocate(0);
   }

   static String getCharsetFromContentType(String var0) {
      if (var0 == null) {
         var0 = null;
         return var0;
      } else {
         Matcher var4 = charsetPattern.matcher(var0);
         if (var4.find()) {
            String var2 = var4.group(1).trim().replace("charset=", "");
            if (var2.length() == 0) {
               return null;
            }

            var0 = var2;

            boolean var1;
            try {
               if (Charset.isSupported(var2)) {
                  return var0;
               }

               var0 = var2.toUpperCase(Locale.ENGLISH);
               var1 = Charset.isSupported(var0);
            } catch (IllegalCharsetNameException var3) {
               return null;
            }

            if (var1) {
               return var0;
            }
         }

         return null;
      }
   }

   public static Document load(File var0, String var1, String var2) throws IOException {
      return parseByteData(readFileToByteBuffer(var0), var1, var2, Parser.htmlParser());
   }

   public static Document load(InputStream var0, String var1, String var2) throws IOException {
      return parseByteData(readToByteBuffer(var0), var1, var2, Parser.htmlParser());
   }

   public static Document load(InputStream var0, String var1, String var2, Parser var3) throws IOException {
      return parseByteData(readToByteBuffer(var0), var1, var2, var3);
   }

   static String mimeBoundary() {
      StringBuilder var1 = new StringBuilder(32);
      Random var2 = new Random();

      for(int var0 = 0; var0 < 32; ++var0) {
         var1.append(mimeBoundaryChars[var2.nextInt(mimeBoundaryChars.length)]);
      }

      return var1.toString();
   }

   static Document parseByteData(ByteBuffer var0, String var1, String var2, Parser var3) {
      String var5 = null;
      var0.mark();
      byte[] var6 = new byte[4];
      if (var0.remaining() >= var6.length) {
         var0.get(var6);
         var0.rewind();
      }

      String var4;
      if (var6[0] == 0 && var6[1] == 0 && var6[2] == -2 && var6[3] == -1 || var6[0] == -1 && var6[1] == -2 && var6[2] == 0 && var6[3] == 0) {
         var4 = "UTF-32";
      } else if ((var6[0] != -2 || var6[1] != -1) && (var6[0] != -1 || var6[1] != -2)) {
         var4 = var1;
         if (var6[0] == -17) {
            var4 = var1;
            if (var6[1] == -69) {
               var4 = var1;
               if (var6[2] == -65) {
                  var4 = "UTF-8";
                  var0.position(3);
               }
            }
         }
      } else {
         var4 = "UTF-16";
      }

      String var7;
      Document var14;
      if (var4 == null) {
         String var8 = Charset.forName("UTF-8").decode(var0).toString();
         Document var9 = var3.parseInput(var8, var2);
         Element var10 = var9.select("meta[http-equiv=content-type], meta[charset]").first();
         var14 = var9;
         var5 = var8;
         var7 = var4;
         if (var10 != null) {
            var1 = null;
            if (var10.hasAttr("http-equiv")) {
               var1 = getCharsetFromContentType(var10.attr("content"));
            }

            String var15 = var1;
            if (var1 == null) {
               var15 = var1;
               if (var10.hasAttr("charset")) {
                  var15 = var1;

                  try {
                     if (Charset.isSupported(var10.attr("charset"))) {
                        var15 = var10.attr("charset");
                     }
                  } catch (IllegalCharsetNameException var11) {
                     var15 = null;
                  }
               }
            }

            var14 = var9;
            var5 = var8;
            var7 = var4;
            if (var15 != null) {
               var14 = var9;
               var5 = var8;
               var7 = var4;
               if (var15.length() != 0) {
                  var14 = var9;
                  var5 = var8;
                  var7 = var4;
                  if (!var15.equals("UTF-8")) {
                     var1 = var15.trim().replaceAll("[\"']", "");
                     var7 = var1;
                     var0.rewind();
                     var5 = Charset.forName(var1).decode(var0).toString();
                     var14 = null;
                  }
               }
            }
         }
      } else {
         Validate.notEmpty(var4, "Must set charset arg to character set of file to parse. Set to null to attempt to detect from HTML");
         String var12 = Charset.forName(var4).decode(var0).toString();
         var14 = var5;
         var5 = var12;
         var7 = var4;
      }

      Document var13 = var14;
      if (var14 == null) {
         var13 = var3.parseInput(var5, var2);
         var13.outputSettings().charset(var7);
      }

      return var13;
   }

   static ByteBuffer readFileToByteBuffer(File param0) throws IOException {
      // $FF: Couldn't be decompiled
   }

   static ByteBuffer readToByteBuffer(InputStream var0) throws IOException {
      return readToByteBuffer(var0, 0);
   }

   static ByteBuffer readToByteBuffer(InputStream var0, int var1) throws IOException {
      boolean var5;
      if (var1 >= 0) {
         var5 = true;
      } else {
         var5 = false;
      }

      Validate.isTrue(var5, "maxSize must be 0 (unlimited) or larger");
      boolean var2;
      if (var1 > 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      byte[] var6 = new byte[131072];
      ByteArrayOutputStream var7 = new ByteArrayOutputStream(131072);

      while(true) {
         int var4 = var0.read(var6);
         if (var4 == -1) {
            break;
         }

         int var3 = var1;
         if (var2) {
            if (var4 > var1) {
               var7.write(var6, 0, var1);
               break;
            }

            var3 = var1 - var4;
         }

         var7.write(var6, 0, var4);
         var1 = var3;
      }

      return ByteBuffer.wrap(var7.toByteArray());
   }
}
