package com.bumptech.glide.util.pool;

public final class GlideTrace {
   private static final int MAX_LENGTH = 127;
   private static final boolean TRACING_ENABLED = false;

   private GlideTrace() {
   }

   public static void beginSection(String var0) {
   }

   public static void beginSectionFormat(String var0, Object var1) {
   }

   public static void beginSectionFormat(String var0, Object var1, Object var2) {
   }

   public static void beginSectionFormat(String var0, Object var1, Object var2, Object var3) {
   }

   public static void endSection() {
   }

   private static String truncateTag(String var0) {
      return var0.length() > 127 ? var0.substring(0, 126) : var0;
   }
}
