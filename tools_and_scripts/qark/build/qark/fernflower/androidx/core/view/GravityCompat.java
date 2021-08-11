package androidx.core.view;

import android.graphics.Rect;
import android.os.Build.VERSION;
import android.view.Gravity;

public final class GravityCompat {
   public static final int END = 8388613;
   public static final int RELATIVE_HORIZONTAL_GRAVITY_MASK = 8388615;
   public static final int RELATIVE_LAYOUT_DIRECTION = 8388608;
   public static final int START = 8388611;

   private GravityCompat() {
   }

   public static void apply(int var0, int var1, int var2, Rect var3, int var4, int var5, Rect var6, int var7) {
      if (VERSION.SDK_INT >= 17) {
         Gravity.apply(var0, var1, var2, var3, var4, var5, var6, var7);
      } else {
         Gravity.apply(var0, var1, var2, var3, var4, var5, var6);
      }
   }

   public static void apply(int var0, int var1, int var2, Rect var3, Rect var4, int var5) {
      if (VERSION.SDK_INT >= 17) {
         Gravity.apply(var0, var1, var2, var3, var4, var5);
      } else {
         Gravity.apply(var0, var1, var2, var3, var4);
      }
   }

   public static void applyDisplay(int var0, Rect var1, Rect var2, int var3) {
      if (VERSION.SDK_INT >= 17) {
         Gravity.applyDisplay(var0, var1, var2, var3);
      } else {
         Gravity.applyDisplay(var0, var1, var2);
      }
   }

   public static int getAbsoluteGravity(int var0, int var1) {
      return VERSION.SDK_INT >= 17 ? Gravity.getAbsoluteGravity(var0, var1) : -8388609 & var0;
   }
}
