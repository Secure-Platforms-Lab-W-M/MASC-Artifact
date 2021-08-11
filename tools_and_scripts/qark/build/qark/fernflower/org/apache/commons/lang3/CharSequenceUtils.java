package org.apache.commons.lang3;

public class CharSequenceUtils {
   private static final int NOT_FOUND = -1;

   static int indexOf(CharSequence var0, int var1, int var2) {
      if (var0 instanceof String) {
         return ((String)var0).indexOf(var1, var2);
      } else {
         int var4 = var0.length();
         int var3 = var2;
         if (var2 < 0) {
            var3 = 0;
         }

         if (var1 < 65536) {
            for(var2 = var3; var2 < var4; ++var2) {
               if (var0.charAt(var2) == var1) {
                  return var2;
               }
            }
         }

         if (var1 <= 1114111) {
            char[] var5 = Character.toChars(var1);

            for(var1 = var3; var1 < var4 - 1; ++var1) {
               char var6 = var0.charAt(var1);
               char var7 = var0.charAt(var1 + 1);
               if (var6 == var5[0] && var7 == var5[1]) {
                  return var1;
               }
            }
         }

         return -1;
      }
   }

   static int indexOf(CharSequence var0, CharSequence var1, int var2) {
      return var0.toString().indexOf(var1.toString(), var2);
   }

   static int lastIndexOf(CharSequence var0, int var1, int var2) {
      if (var0 instanceof String) {
         return ((String)var0).lastIndexOf(var1, var2);
      } else {
         int var4 = var0.length();
         if (var2 < 0) {
            return -1;
         } else {
            int var3 = var2;
            if (var2 >= var4) {
               var3 = var4 - 1;
            }

            if (var1 < 65536) {
               for(var2 = var3; var2 >= 0; --var2) {
                  if (var0.charAt(var2) == var1) {
                     return var2;
                  }
               }
            }

            if (var1 <= 1114111) {
               char[] var5 = Character.toChars(var1);
               if (var3 == var4 - 1) {
                  return -1;
               }

               for(var1 = var3; var1 >= 0; --var1) {
                  char var6 = var0.charAt(var1);
                  char var7 = var0.charAt(var1 + 1);
                  if (var5[0] == var6 && var5[1] == var7) {
                     return var1;
                  }
               }
            }

            return -1;
         }
      }
   }

   static int lastIndexOf(CharSequence var0, CharSequence var1, int var2) {
      return var0.toString().lastIndexOf(var1.toString(), var2);
   }

   static boolean regionMatches(CharSequence var0, boolean var1, int var2, CharSequence var3, int var4, int var5) {
      if (var0 instanceof String && var3 instanceof String) {
         return ((String)var0).regionMatches(var1, var2, (String)var3, var4, var5);
      } else {
         int var9 = var4;
         int var8 = var5;
         int var12 = var0.length();
         int var11 = var3.length();
         if (var2 >= 0 && var4 >= 0) {
            if (var5 < 0) {
               return false;
            } else if (var12 - var2 >= var5) {
               var2 = var2;
               if (var11 - var4 < var5) {
                  return false;
               } else {
                  while(var8 > 0) {
                     char var6 = var0.charAt(var2);
                     char var7 = var3.charAt(var9);
                     if (var6 != var7) {
                        if (!var1) {
                           return false;
                        }

                        if (Character.toUpperCase(var6) != Character.toUpperCase(var7) && Character.toLowerCase(var6) != Character.toLowerCase(var7)) {
                           return false;
                        }
                     }

                     ++var2;
                     --var8;
                     ++var9;
                  }

                  return true;
               }
            } else {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   public static CharSequence subSequence(CharSequence var0, int var1) {
      return var0 == null ? null : var0.subSequence(var1, var0.length());
   }

   static char[] toCharArray(CharSequence var0) {
      if (var0 instanceof String) {
         return ((String)var0).toCharArray();
      } else {
         int var2 = var0.length();
         char[] var3 = new char[var0.length()];

         for(int var1 = 0; var1 < var2; ++var1) {
            var3[var1] = var0.charAt(var1);
         }

         return var3;
      }
   }
}
