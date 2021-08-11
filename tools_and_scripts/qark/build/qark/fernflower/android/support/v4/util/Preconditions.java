package android.support.v4.util;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.text.TextUtils;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class Preconditions {
   public static void checkArgument(boolean var0) {
      if (!var0) {
         throw new IllegalArgumentException();
      }
   }

   public static void checkArgument(boolean var0, Object var1) {
      if (!var0) {
         throw new IllegalArgumentException(String.valueOf(var1));
      }
   }

   public static float checkArgumentFinite(float var0, String var1) {
      StringBuilder var2;
      if (!Float.isNaN(var0)) {
         if (!Float.isInfinite(var0)) {
            return var0;
         } else {
            var2 = new StringBuilder();
            var2.append(var1);
            var2.append(" must not be infinite");
            throw new IllegalArgumentException(var2.toString());
         }
      } else {
         var2 = new StringBuilder();
         var2.append(var1);
         var2.append(" must not be NaN");
         throw new IllegalArgumentException(var2.toString());
      }
   }

   public static float checkArgumentInRange(float var0, float var1, float var2, String var3) {
      if (!Float.isNaN(var0)) {
         if (var0 >= var1) {
            if (var0 <= var2) {
               return var0;
            } else {
               throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%f, %f] (too high)", var3, var1, var2));
            }
         } else {
            throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%f, %f] (too low)", var3, var1, var2));
         }
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append(var3);
         var4.append(" must not be NaN");
         throw new IllegalArgumentException(var4.toString());
      }
   }

   public static int checkArgumentInRange(int var0, int var1, int var2, String var3) {
      if (var0 >= var1) {
         if (var0 <= var2) {
            return var0;
         } else {
            throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too high)", var3, var1, var2));
         }
      } else {
         throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too low)", var3, var1, var2));
      }
   }

   public static long checkArgumentInRange(long var0, long var2, long var4, String var6) {
      if (var0 >= var2) {
         if (var0 <= var4) {
            return var0;
         } else {
            throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too high)", var6, var2, var4));
         }
      } else {
         throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too low)", var6, var2, var4));
      }
   }

   @IntRange(
      from = 0L
   )
   public static int checkArgumentNonnegative(int var0) {
      if (var0 >= 0) {
         return var0;
      } else {
         throw new IllegalArgumentException();
      }
   }

   @IntRange(
      from = 0L
   )
   public static int checkArgumentNonnegative(int var0, String var1) {
      if (var0 >= 0) {
         return var0;
      } else {
         throw new IllegalArgumentException(var1);
      }
   }

   public static long checkArgumentNonnegative(long var0) {
      if (var0 >= 0L) {
         return var0;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public static long checkArgumentNonnegative(long var0, String var2) {
      if (var0 >= 0L) {
         return var0;
      } else {
         throw new IllegalArgumentException(var2);
      }
   }

   public static int checkArgumentPositive(int var0, String var1) {
      if (var0 > 0) {
         return var0;
      } else {
         throw new IllegalArgumentException(var1);
      }
   }

   public static float[] checkArrayElementsInRange(float[] var0, float var1, float var2, String var3) {
      StringBuilder var6 = new StringBuilder();
      var6.append(var3);
      var6.append(" must not be null");
      checkNotNull(var0, var6.toString());

      for(int var5 = 0; var5 < var0.length; ++var5) {
         float var4 = var0[var5];
         if (Float.isNaN(var4)) {
            StringBuilder var7 = new StringBuilder();
            var7.append(var3);
            var7.append("[");
            var7.append(var5);
            var7.append("] must not be NaN");
            throw new IllegalArgumentException(var7.toString());
         }

         if (var4 < var1) {
            throw new IllegalArgumentException(String.format(Locale.US, "%s[%d] is out of range of [%f, %f] (too low)", var3, var5, var1, var2));
         }

         if (var4 > var2) {
            throw new IllegalArgumentException(String.format(Locale.US, "%s[%d] is out of range of [%f, %f] (too high)", var3, var5, var1, var2));
         }
      }

      return var0;
   }

   public static Object[] checkArrayElementsNotNull(Object[] var0, String var1) {
      if (var0 != null) {
         for(int var2 = 0; var2 < var0.length; ++var2) {
            if (var0[var2] == null) {
               throw new NullPointerException(String.format(Locale.US, "%s[%d] must not be null", var1, var2));
            }
         }

         return var0;
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append(var1);
         var3.append(" must not be null");
         NullPointerException var4 = new NullPointerException(var3.toString());
         throw var4;
      }
   }

   @NonNull
   public static Collection checkCollectionElementsNotNull(Collection var0, String var1) {
      if (var0 != null) {
         long var2 = 0L;

         for(Iterator var4 = var0.iterator(); var4.hasNext(); ++var2) {
            if (var4.next() == null) {
               throw new NullPointerException(String.format(Locale.US, "%s[%d] must not be null", var1, var2));
            }
         }

         return var0;
      } else {
         StringBuilder var5 = new StringBuilder();
         var5.append(var1);
         var5.append(" must not be null");
         NullPointerException var6 = new NullPointerException(var5.toString());
         throw var6;
      }
   }

   public static Collection checkCollectionNotEmpty(Collection var0, String var1) {
      StringBuilder var2;
      if (var0 != null) {
         if (!var0.isEmpty()) {
            return var0;
         } else {
            var2 = new StringBuilder();
            var2.append(var1);
            var2.append(" is empty");
            throw new IllegalArgumentException(var2.toString());
         }
      } else {
         var2 = new StringBuilder();
         var2.append(var1);
         var2.append(" must not be null");
         throw new NullPointerException(var2.toString());
      }
   }

   public static int checkFlagsArgument(int var0, int var1) {
      if ((var0 & var1) == var0) {
         return var0;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Requested flags 0x");
         var2.append(Integer.toHexString(var0));
         var2.append(", but only 0x");
         var2.append(Integer.toHexString(var1));
         var2.append(" are allowed");
         throw new IllegalArgumentException(var2.toString());
      }
   }

   @NonNull
   public static Object checkNotNull(Object var0) {
      if (var0 != null) {
         return var0;
      } else {
         throw new NullPointerException();
      }
   }

   @NonNull
   public static Object checkNotNull(Object var0, Object var1) {
      if (var0 != null) {
         return var0;
      } else {
         throw new NullPointerException(String.valueOf(var1));
      }
   }

   public static void checkState(boolean var0) {
      checkState(var0, (String)null);
   }

   public static void checkState(boolean var0, String var1) {
      if (!var0) {
         throw new IllegalStateException(var1);
      }
   }

   @NonNull
   public static CharSequence checkStringNotEmpty(CharSequence var0) {
      if (!TextUtils.isEmpty(var0)) {
         return var0;
      } else {
         throw new IllegalArgumentException();
      }
   }

   @NonNull
   public static CharSequence checkStringNotEmpty(CharSequence var0, Object var1) {
      if (!TextUtils.isEmpty(var0)) {
         return var0;
      } else {
         throw new IllegalArgumentException(String.valueOf(var1));
      }
   }
}
