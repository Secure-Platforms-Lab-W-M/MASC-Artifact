package com.bumptech.glide.util;

import android.text.TextUtils;
import java.util.Collection;

public final class Preconditions {
   private Preconditions() {
   }

   public static void checkArgument(boolean var0, String var1) {
      if (!var0) {
         throw new IllegalArgumentException(var1);
      }
   }

   public static String checkNotEmpty(String var0) {
      if (!TextUtils.isEmpty(var0)) {
         return var0;
      } else {
         throw new IllegalArgumentException("Must not be null or empty");
      }
   }

   public static Collection checkNotEmpty(Collection var0) {
      if (!var0.isEmpty()) {
         return var0;
      } else {
         throw new IllegalArgumentException("Must not be empty.");
      }
   }

   public static Object checkNotNull(Object var0) {
      return checkNotNull(var0, "Argument must not be null");
   }

   public static Object checkNotNull(Object var0, String var1) {
      if (var0 != null) {
         return var0;
      } else {
         throw new NullPointerException(var1);
      }
   }
}
