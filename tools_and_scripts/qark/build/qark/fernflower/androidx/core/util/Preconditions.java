package androidx.core.util;

import java.util.Locale;

public final class Preconditions {
   private Preconditions() {
   }

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

   public static int checkArgumentNonnegative(int var0) {
      if (var0 >= 0) {
         return var0;
      } else {
         throw new IllegalArgumentException();
      }
   }

   public static int checkArgumentNonnegative(int var0, String var1) {
      if (var0 >= 0) {
         return var0;
      } else {
         throw new IllegalArgumentException(var1);
      }
   }

   public static Object checkNotNull(Object var0) {
      if (var0 != null) {
         return var0;
      } else {
         throw null;
      }
   }

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
}
