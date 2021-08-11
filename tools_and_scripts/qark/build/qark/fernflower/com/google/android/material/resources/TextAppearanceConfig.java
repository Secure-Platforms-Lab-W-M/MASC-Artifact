package com.google.android.material.resources;

public class TextAppearanceConfig {
   private static boolean shouldLoadFontSynchronously;

   public static void setShouldLoadFontSynchronously(boolean var0) {
      shouldLoadFontSynchronously = var0;
   }

   public static boolean shouldLoadFontSynchronously() {
      return shouldLoadFontSynchronously;
   }
}
