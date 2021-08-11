package androidx.core.view;

import android.os.Build.VERSION;
import android.view.View;
import android.view.Window;

public final class WindowCompat {
   public static final int FEATURE_ACTION_BAR = 8;
   public static final int FEATURE_ACTION_BAR_OVERLAY = 9;
   public static final int FEATURE_ACTION_MODE_OVERLAY = 10;

   private WindowCompat() {
   }

   public static View requireViewById(Window var0, int var1) {
      if (VERSION.SDK_INT >= 28) {
         return var0.requireViewById(var1);
      } else {
         View var2 = var0.findViewById(var1);
         if (var2 != null) {
            return var2;
         } else {
            throw new IllegalArgumentException("ID does not reference a View inside this Window");
         }
      }
   }
}
