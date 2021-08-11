package org.apache.commons.lang3;

public class CharSetUtils {
   public static boolean containsAny(String var0, String... var1) {
      if (!StringUtils.isEmpty(var0)) {
         if (deepEmpty(var1)) {
            return false;
         } else {
            CharSet var5 = CharSet.getInstance(var1);
            char[] var4 = var0.toCharArray();
            int var3 = var4.length;

            for(int var2 = 0; var2 < var3; ++var2) {
               if (var5.contains(var4[var2])) {
                  return true;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public static int count(String var0, String... var1) {
      boolean var6 = StringUtils.isEmpty(var0);
      int var2 = 0;
      if (!var6) {
         if (deepEmpty(var1)) {
            return 0;
         } else {
            CharSet var8 = CharSet.getInstance(var1);
            int var3 = 0;
            char[] var7 = var0.toCharArray();

            int var4;
            for(int var5 = var7.length; var2 < var5; var3 = var4) {
               var4 = var3;
               if (var8.contains(var7[var2])) {
                  var4 = var3 + 1;
               }

               ++var2;
            }

            return var3;
         }
      } else {
         return 0;
      }
   }

   private static boolean deepEmpty(String[] var0) {
      if (var0 != null) {
         int var2 = var0.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            if (StringUtils.isNotEmpty(var0[var1])) {
               return false;
            }
         }
      }

      return true;
   }

   public static String delete(String var0, String... var1) {
      if (!StringUtils.isEmpty(var0)) {
         return deepEmpty(var1) ? var0 : modify(var0, var1, false);
      } else {
         return var0;
      }
   }

   public static String keep(String var0, String... var1) {
      if (var0 == null) {
         return null;
      } else {
         return !var0.isEmpty() && !deepEmpty(var1) ? modify(var0, var1, true) : "";
      }
   }

   private static String modify(String var0, String[] var1, boolean var2) {
      CharSet var8 = CharSet.getInstance(var1);
      StringBuilder var6 = new StringBuilder(var0.length());
      char[] var7 = var0.toCharArray();
      int var5 = var7.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         char var3 = var7[var4];
         if (var8.contains(var3) == var2) {
            var6.append(var3);
         }
      }

      return var6.toString();
   }

   public static String squeeze(String var0, String... var1) {
      if (StringUtils.isEmpty(var0)) {
         return var0;
      } else if (deepEmpty(var1)) {
         return var0;
      } else {
         CharSet var7 = CharSet.getInstance(var1);
         StringBuilder var8 = new StringBuilder(var0.length());
         char[] var9 = var0.toCharArray();
         int var5 = var9.length;
         char var2 = var9[0];
         Character var6 = null;
         Character var10 = null;
         var8.append(var2);
         int var3 = 1;

         for(char var4 = var2; var3 < var5; ++var3) {
            var2 = var9[var3];
            Character var11 = var10;
            if (var2 == var4) {
               label45: {
                  if (var6 != null && var2 == var6) {
                     continue;
                  }

                  if (var10 != null) {
                     var11 = var10;
                     if (var2 == var10) {
                        break label45;
                     }
                  }

                  if (var7.contains(var2)) {
                     var6 = var2;
                     continue;
                  }

                  var11 = var2;
               }
            }

            var8.append(var2);
            var4 = var2;
            var10 = var11;
         }

         return var8.toString();
      }
   }
}
