package androidx.core.util;

import android.os.Build.VERSION;
import java.util.Arrays;
import java.util.Objects;

public class ObjectsCompat {
   private ObjectsCompat() {
   }

   public static boolean equals(Object var0, Object var1) {
      if (VERSION.SDK_INT >= 19) {
         return Objects.equals(var0, var1);
      } else {
         return var0 == var1 || var0 != null && var0.equals(var1);
      }
   }

   public static int hash(Object... var0) {
      return VERSION.SDK_INT >= 19 ? Objects.hash(var0) : Arrays.hashCode(var0);
   }

   public static int hashCode(Object var0) {
      return var0 != null ? var0.hashCode() : 0;
   }
}
