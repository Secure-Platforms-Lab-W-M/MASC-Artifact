package org.apache.commons.lang3;

import org.apache.commons.lang3.math.NumberUtils;

public class BooleanUtils {
   public static Boolean and(Boolean... var0) {
      if (var0 != null) {
         if (var0.length != 0) {
            try {
               if (and(ArrayUtils.toPrimitive(var0))) {
                  return Boolean.TRUE;
               } else {
                  Boolean var2 = Boolean.FALSE;
                  return var2;
               }
            } catch (NullPointerException var1) {
               throw new IllegalArgumentException("The array must not contain any null elements");
            }
         } else {
            throw new IllegalArgumentException("Array is empty");
         }
      } else {
         throw new IllegalArgumentException("The Array must not be null");
      }
   }

   public static boolean and(boolean... var0) {
      if (var0 != null) {
         if (var0.length != 0) {
            int var2 = var0.length;

            for(int var1 = 0; var1 < var2; ++var1) {
               if (!var0[var1]) {
                  return false;
               }
            }

            return true;
         } else {
            throw new IllegalArgumentException("Array is empty");
         }
      } else {
         throw new IllegalArgumentException("The Array must not be null");
      }
   }

   public static int compare(boolean var0, boolean var1) {
      if (var0 == var1) {
         return 0;
      } else {
         return var0 ? 1 : -1;
      }
   }

   public static boolean isFalse(Boolean var0) {
      return Boolean.FALSE.equals(var0);
   }

   public static boolean isNotFalse(Boolean var0) {
      return isFalse(var0) ^ true;
   }

   public static boolean isNotTrue(Boolean var0) {
      return isTrue(var0) ^ true;
   }

   public static boolean isTrue(Boolean var0) {
      return Boolean.TRUE.equals(var0);
   }

   public static Boolean negate(Boolean var0) {
      if (var0 == null) {
         return null;
      } else {
         return var0 ? Boolean.FALSE : Boolean.TRUE;
      }
   }

   // $FF: renamed from: or (java.lang.Boolean[]) java.lang.Boolean
   public static Boolean method_32(Boolean... var0) {
      if (var0 != null) {
         if (var0.length != 0) {
            try {
               if (method_33(ArrayUtils.toPrimitive(var0))) {
                  return Boolean.TRUE;
               } else {
                  Boolean var2 = Boolean.FALSE;
                  return var2;
               }
            } catch (NullPointerException var1) {
               throw new IllegalArgumentException("The array must not contain any null elements");
            }
         } else {
            throw new IllegalArgumentException("Array is empty");
         }
      } else {
         throw new IllegalArgumentException("The Array must not be null");
      }
   }

   // $FF: renamed from: or (boolean[]) boolean
   public static boolean method_33(boolean... var0) {
      if (var0 != null) {
         if (var0.length != 0) {
            int var2 = var0.length;

            for(int var1 = 0; var1 < var2; ++var1) {
               if (var0[var1]) {
                  return true;
               }
            }

            return false;
         } else {
            throw new IllegalArgumentException("Array is empty");
         }
      } else {
         throw new IllegalArgumentException("The Array must not be null");
      }
   }

   public static boolean toBoolean(int var0) {
      return var0 != 0;
   }

   public static boolean toBoolean(int var0, int var1, int var2) {
      if (var0 == var1) {
         return true;
      } else if (var0 == var2) {
         return false;
      } else {
         throw new IllegalArgumentException("The Integer did not match either specified value");
      }
   }

   public static boolean toBoolean(Boolean var0) {
      return var0 != null && var0;
   }

   public static boolean toBoolean(Integer var0, Integer var1, Integer var2) {
      if (var0 == null) {
         if (var1 == null) {
            return true;
         }

         if (var2 == null) {
            return false;
         }
      } else {
         if (var0.equals(var1)) {
            return true;
         }

         if (var0.equals(var2)) {
            return false;
         }
      }

      throw new IllegalArgumentException("The Integer did not match either specified value");
   }

   public static boolean toBoolean(String var0) {
      return toBooleanObject(var0) == Boolean.TRUE;
   }

   public static boolean toBoolean(String var0, String var1, String var2) {
      if (var0 == var1) {
         return true;
      } else if (var0 == var2) {
         return false;
      } else {
         if (var0 != null) {
            if (var0.equals(var1)) {
               return true;
            }

            if (var0.equals(var2)) {
               return false;
            }
         }

         throw new IllegalArgumentException("The String did not match either specified value");
      }
   }

   public static boolean toBooleanDefaultIfNull(Boolean var0, boolean var1) {
      return var0 == null ? var1 : var0;
   }

   public static Boolean toBooleanObject(int var0) {
      return var0 == 0 ? Boolean.FALSE : Boolean.TRUE;
   }

   public static Boolean toBooleanObject(int var0, int var1, int var2, int var3) {
      if (var0 == var1) {
         return Boolean.TRUE;
      } else if (var0 == var2) {
         return Boolean.FALSE;
      } else if (var0 == var3) {
         return null;
      } else {
         throw new IllegalArgumentException("The Integer did not match any specified value");
      }
   }

   public static Boolean toBooleanObject(Integer var0) {
      if (var0 == null) {
         return null;
      } else {
         return var0 == 0 ? Boolean.FALSE : Boolean.TRUE;
      }
   }

   public static Boolean toBooleanObject(Integer var0, Integer var1, Integer var2, Integer var3) {
      if (var0 == null) {
         if (var1 == null) {
            return Boolean.TRUE;
         }

         if (var2 == null) {
            return Boolean.FALSE;
         }

         if (var3 == null) {
            return null;
         }
      } else {
         if (var0.equals(var1)) {
            return Boolean.TRUE;
         }

         if (var0.equals(var2)) {
            return Boolean.FALSE;
         }

         if (var0.equals(var3)) {
            return null;
         }
      }

      throw new IllegalArgumentException("The Integer did not match any specified value");
   }

   public static Boolean toBooleanObject(String var0) {
      if (var0 == "true") {
         return Boolean.TRUE;
      } else if (var0 == null) {
         return null;
      } else {
         int var1 = var0.length();
         char var6;
         if (var1 == 1) {
            var6 = var0.charAt(0);
            if (var6 != 'y' && var6 != 'Y' && var6 != 't' && var6 != 'T') {
               if (var6 != 'n' && var6 != 'N' && var6 != 'f' && var6 != 'F') {
                  return null;
               }

               return Boolean.FALSE;
            }

            return Boolean.TRUE;
         } else {
            char var2;
            if (var1 != 2) {
               char var3;
               if (var1 != 3) {
                  char var4;
                  if (var1 != 4) {
                     if (var1 == 5) {
                        var6 = var0.charAt(0);
                        var2 = var0.charAt(1);
                        var3 = var0.charAt(2);
                        var4 = var0.charAt(3);
                        char var5 = var0.charAt(4);
                        if ((var6 == 'f' || var6 == 'F') && (var2 == 'a' || var2 == 'A') && (var3 == 'l' || var3 == 'L') && (var4 == 's' || var4 == 'S') && (var5 == 'e' || var5 == 'E')) {
                           return Boolean.FALSE;
                        }
                     }
                  } else {
                     var6 = var0.charAt(0);
                     var2 = var0.charAt(1);
                     var3 = var0.charAt(2);
                     var4 = var0.charAt(3);
                     if ((var6 == 't' || var6 == 'T') && (var2 == 'r' || var2 == 'R') && (var3 == 'u' || var3 == 'U') && (var4 == 'e' || var4 == 'E')) {
                        return Boolean.TRUE;
                     }
                  }
               } else {
                  var6 = var0.charAt(0);
                  var2 = var0.charAt(1);
                  var3 = var0.charAt(2);
                  if ((var6 == 'y' || var6 == 'Y') && (var2 == 'e' || var2 == 'E') && (var3 == 's' || var3 == 'S')) {
                     return Boolean.TRUE;
                  }

                  if ((var6 == 'o' || var6 == 'O') && (var2 == 'f' || var2 == 'F') && (var3 == 'f' || var3 == 'F')) {
                     return Boolean.FALSE;
                  }
               }
            } else {
               var6 = var0.charAt(0);
               var2 = var0.charAt(1);
               if (var6 != 'o' && var6 != 'O' || var2 != 'n' && var2 != 'N') {
                  if (var6 != 'n' && var6 != 'N' || var2 != 'o' && var2 != 'O') {
                     return null;
                  }

                  return Boolean.FALSE;
               }

               return Boolean.TRUE;
            }
         }

         return null;
      }
   }

   public static Boolean toBooleanObject(String var0, String var1, String var2, String var3) {
      if (var0 == null) {
         if (var1 == null) {
            return Boolean.TRUE;
         }

         if (var2 == null) {
            return Boolean.FALSE;
         }

         if (var3 == null) {
            return null;
         }
      } else {
         if (var0.equals(var1)) {
            return Boolean.TRUE;
         }

         if (var0.equals(var2)) {
            return Boolean.FALSE;
         }

         if (var0.equals(var3)) {
            return null;
         }
      }

      throw new IllegalArgumentException("The String did not match any specified value");
   }

   public static int toInteger(Boolean var0, int var1, int var2, int var3) {
      if (var0 == null) {
         return var3;
      } else {
         return var0 ? var1 : var2;
      }
   }

   public static int toInteger(boolean var0) {
      throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
   }

   public static int toInteger(boolean var0, int var1, int var2) {
      return var0 ? var1 : var2;
   }

   public static Integer toIntegerObject(Boolean var0) {
      if (var0 == null) {
         return null;
      } else {
         return var0 ? NumberUtils.INTEGER_ONE : NumberUtils.INTEGER_ZERO;
      }
   }

   public static Integer toIntegerObject(Boolean var0, Integer var1, Integer var2, Integer var3) {
      if (var0 == null) {
         return var3;
      } else {
         return var0 ? var1 : var2;
      }
   }

   public static Integer toIntegerObject(boolean var0) {
      return var0 ? NumberUtils.INTEGER_ONE : NumberUtils.INTEGER_ZERO;
   }

   public static Integer toIntegerObject(boolean var0, Integer var1, Integer var2) {
      return var0 ? var1 : var2;
   }

   public static String toString(Boolean var0, String var1, String var2, String var3) {
      if (var0 == null) {
         return var3;
      } else {
         return var0 ? var1 : var2;
      }
   }

   public static String toString(boolean var0, String var1, String var2) {
      return var0 ? var1 : var2;
   }

   public static String toStringOnOff(Boolean var0) {
      return toString(var0, "on", "off", (String)null);
   }

   public static String toStringOnOff(boolean var0) {
      return toString(var0, "on", "off");
   }

   public static String toStringTrueFalse(Boolean var0) {
      return toString(var0, "true", "false", (String)null);
   }

   public static String toStringTrueFalse(boolean var0) {
      return toString(var0, "true", "false");
   }

   public static String toStringYesNo(Boolean var0) {
      return toString(var0, "yes", "no", (String)null);
   }

   public static String toStringYesNo(boolean var0) {
      return toString(var0, "yes", "no");
   }

   public static Boolean xor(Boolean... var0) {
      if (var0 != null) {
         if (var0.length != 0) {
            try {
               if (xor(ArrayUtils.toPrimitive(var0))) {
                  return Boolean.TRUE;
               } else {
                  Boolean var2 = Boolean.FALSE;
                  return var2;
               }
            } catch (NullPointerException var1) {
               throw new IllegalArgumentException("The array must not contain any null elements");
            }
         } else {
            throw new IllegalArgumentException("Array is empty");
         }
      } else {
         throw new IllegalArgumentException("The Array must not be null");
      }
   }

   public static boolean xor(boolean... var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("The Array must not be null");
      } else if (var0.length == 0) {
         throw new IllegalArgumentException("Array is empty");
      } else {
         boolean var3 = false;
         int var2 = var0.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            var3 ^= var0[var1];
         }

         return var3;
      }
   }
}
