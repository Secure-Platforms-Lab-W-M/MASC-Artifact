package com.sun.media.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Registry {
   private static final Map hash = new HashMap();

   public static boolean commit() throws IOException {
      return false;
   }

   public static Object get(String var0) {
      return var0 == null ? null : hash.get(var0);
   }

   public static boolean getBoolean(String var0, boolean var1) {
      Object var2 = get(var0);
      return var2 == null ? var1 : Boolean.parseBoolean(var2.toString());
   }

   public static int getInt(String var0, int var1) {
      Object var4 = get(var0);
      if (var4 != null) {
         try {
            int var2 = Integer.parseInt(var4.toString());
            return var2;
         } catch (NumberFormatException var3) {
         }
      }

      return var1;
   }

   public static boolean set(String var0, Object var1) {
      if (var0 != null && var1 != null) {
         hash.put(var0, var1);
         return true;
      } else {
         return false;
      }
   }
}
