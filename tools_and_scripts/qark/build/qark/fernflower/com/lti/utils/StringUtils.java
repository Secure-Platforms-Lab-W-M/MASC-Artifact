package com.lti.utils;

import net.iharder.Base64;

public final class StringUtils {
   private static final int MAX_STANDARD_ASCII = 255;
   private static final int RADIX_16 = 16;

   public static String byteArrayToBase64String(byte[] var0) {
      return Base64.encodeBytes(var0);
   }

   public static String byteArrayToHexString(byte[] var0) {
      return byteArrayToHexString(var0, var0.length);
   }

   public static String byteArrayToHexString(byte[] var0, int var1) {
      return byteArrayToHexString(var0, var1, 0);
   }

   public static String byteArrayToHexString(byte[] var0, int var1, int var2) {
      StringBuffer var6 = new StringBuffer();

      for(int var3 = 0; var3 < var1; ++var3) {
         String var5 = Integer.toHexString(UnsignedUtils.uByteToInt(var0[var2 + var3]));
         String var4 = var5;
         if (var5.length() == 1) {
            StringBuilder var7 = new StringBuilder();
            var7.append("0");
            var7.append(var5);
            var4 = var7.toString();
         }

         var6.append(var4);
      }

      return var6.toString();
   }

   public static String byteToHexString_ZeroPad(byte var0) {
      String var2 = Integer.toHexString(UnsignedUtils.uByteToInt(var0));
      String var1 = var2;
      if (var2.length() == 1) {
         StringBuilder var3 = new StringBuilder();
         var3.append("0");
         var3.append(var2);
         var1 = var3.toString();
      }

      return var1;
   }

   public static String dump(byte[] var0, int var1, int var2) {
      StringBuffer var6 = new StringBuffer();

      int var3;
      for(int var5 = 32; var1 < var2; var5 = var3) {
         int var4 = 0;
         var3 = var5;
         if (var1 + var5 > var2) {
            var3 = var2 - var1;
            var4 = 32 - var3;
         }

         var6.append(byteArrayToHexString(var0, var3, var1));

         for(var5 = 0; var5 < var4; ++var5) {
            var6.append("  ");
         }

         var6.append(" | ");

         for(var4 = 0; var4 < var3; ++var4) {
            byte var7 = var0[var1 + var4];
            if (var7 >= 32 && var7 <= 126) {
               var6.append((char)var7);
            } else {
               var6.append('.');
            }
         }

         var6.append('\n');
         var1 += var3;
      }

      return var6.toString();
   }

   public static byte hexStringToByte(String var0) {
      return (byte)Integer.parseInt(var0, 16);
   }

   public static byte[] hexStringToByteArray(String var0) {
      byte[] var2 = new byte[var0.length() / 2];

      for(int var1 = 0; var1 < var2.length; ++var1) {
         var2[var1] = hexStringToByte(var0.substring(var1 * 2, var1 * 2 + 2));
      }

      return var2;
   }

   public static String replaceSpecialUrlChars(String var0) {
      return replaceSpecialUrlChars(var0, false);
   }

   public static String replaceSpecialUrlChars(String var0, boolean var1) {
      if (var0 == null) {
         return null;
      } else {
         StringBuffer var4 = new StringBuffer();

         for(int var3 = 0; var3 < var0.length(); ++var3) {
            char var2 = var0.charAt(var3);
            if ((var2 < 'a' || var2 > 'z') && (var2 < 'A' || var2 > 'Z') && (var2 < '0' || var2 > '9') && var2 != '.' && var2 != '-' && var2 != '_' && var2 != '~' && (!var1 || var2 != '/' && var2 != ':' && var2 != '\\')) {
               if (var3 > 255) {
                  throw new IllegalArgumentException();
               }

               var4.append('%');
               var4.append(byteToHexString_ZeroPad((byte)var2));
            } else {
               var4.append(var2);
            }
         }

         return var4.toString();
      }
   }

   public static String replaceSpecialXMLChars(String var0) {
      return replaceSpecialXMLChars_Core(var0, true);
   }

   private static String replaceSpecialXMLChars_Core(String var0, boolean var1) {
      if (var0 == null) {
         return null;
      } else {
         StringBuilder var4 = new StringBuilder();

         for(int var3 = 0; var3 < var0.length(); ++var3) {
            char var2 = var0.charAt(var3);
            if (var2 == '&') {
               var4.append("&amp;");
            } else if (var2 == '<') {
               var4.append("&lt;");
            } else if (var2 == '>') {
               var4.append("&gt;");
            } else if (var2 == '"') {
               var4.append("&quot;");
            } else if (var1 && var2 == '\'') {
               var4.append("&apos;");
            } else {
               var4.append(var2);
            }
         }

         return var4.toString();
      }
   }

   public static String restoreSpecialURLChars(String var0) {
      if (var0 == null) {
         return null;
      } else {
         StringBuffer var7 = new StringBuffer();
         StringBuffer var5 = new StringBuffer();
         boolean var4 = false;

         boolean var2;
         for(int var3 = 0; var3 < var0.length(); var4 = var2) {
            char var1 = var0.charAt(var3);
            StringBuffer var6;
            if (!var4) {
               if (var1 == '%') {
                  var2 = true;
                  var6 = new StringBuffer();
               } else {
                  var7.append(var1);
                  var6 = var5;
                  var2 = var4;
               }
            } else {
               var5.append(var1);
               if (var5.length() == 2) {
                  var7.append((char)Integer.parseInt(var5.toString(), 16));
                  var2 = false;
                  var6 = var5;
               } else {
                  var6 = var5;
                  var2 = var4;
                  if (var5.length() == 1) {
                     var6 = var5;
                     var2 = var4;
                     if (var5.charAt(0) == '%') {
                        var7.append('%');
                        var2 = false;
                        var6 = var5;
                     }
                  }
               }
            }

            ++var3;
            var5 = var6;
         }

         if (var4) {
            StringBuilder var8 = new StringBuilder();
            var8.append("&");
            var8.append(var5.toString());
            var7.append(var8.toString());
         }

         return var7.toString();
      }
   }

   public static String restoreSpecialXMLChars(String var0) {
      return restoreSpecialXMLChars_Core(var0, true);
   }

   private static String restoreSpecialXMLChars_Core(String var0, boolean var1) {
      if (var0 == null) {
         return null;
      } else {
         StringBuilder var6 = new StringBuilder();
         StringBuilder var5 = new StringBuilder();
         boolean var3 = false;

         for(int var4 = 0; var4 < var0.length(); ++var4) {
            char var2 = var0.charAt(var4);
            if (!var3) {
               if (var2 == '&') {
                  var3 = true;
                  var5 = new StringBuilder();
               } else {
                  var6.append(var2);
               }
            } else if (var2 == ';') {
               String var7 = var5.toString();
               if (var7.equals("amp")) {
                  var6.append('&');
               } else if (var7.equals("lt")) {
                  var6.append('<');
               } else if (var7.equals("gt")) {
                  var6.append('>');
               } else if (var7.equals("quot")) {
                  var6.append('"');
               } else if (var7.equals("apos") & var1) {
                  var6.append('\'');
               } else {
                  StringBuilder var8 = new StringBuilder();
                  var8.append("&");
                  var8.append(var7);
                  var8.append(';');
                  var6.append(var8.toString());
               }

               var3 = false;
            } else {
               var5.append(var2);
            }
         }

         if (var3) {
            StringBuilder var9 = new StringBuilder();
            var9.append("&");
            var9.append(var5.toString());
            var6.append(var9.toString());
         }

         return var6.toString();
      }
   }
}
