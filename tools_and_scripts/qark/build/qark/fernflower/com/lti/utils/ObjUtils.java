package com.lti.utils;

public final class ObjUtils {
   private ObjUtils() {
   }

   public static boolean equal(Object var0, Object var1) {
      if (var0 == null && var1 == null) {
         return true;
      } else {
         return var0 != null && var1 != null ? var0.equals(var1) : false;
      }
   }
}
