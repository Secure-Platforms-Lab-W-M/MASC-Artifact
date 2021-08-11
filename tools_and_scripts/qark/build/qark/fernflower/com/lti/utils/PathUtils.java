package com.lti.utils;

public final class PathUtils {
   private PathUtils() {
   }

   public static String extractExtension(String var0) {
      int var1 = var0.lastIndexOf(".");
      return var1 < 0 ? "" : var0.substring(var1 + 1, var0.length());
   }

   public static String getTempPath() {
      return System.getProperty("java.io.tmpdir");
   }
}
