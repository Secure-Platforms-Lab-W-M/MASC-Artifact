package androidx.core.view;

import android.os.Build.VERSION;
import android.view.ScaleGestureDetector;

public final class ScaleGestureDetectorCompat {
   private ScaleGestureDetectorCompat() {
   }

   public static boolean isQuickScaleEnabled(ScaleGestureDetector var0) {
      return VERSION.SDK_INT >= 19 ? var0.isQuickScaleEnabled() : false;
   }

   @Deprecated
   public static boolean isQuickScaleEnabled(Object var0) {
      return isQuickScaleEnabled((ScaleGestureDetector)var0);
   }

   public static void setQuickScaleEnabled(ScaleGestureDetector var0, boolean var1) {
      if (VERSION.SDK_INT >= 19) {
         var0.setQuickScaleEnabled(var1);
      }

   }

   @Deprecated
   public static void setQuickScaleEnabled(Object var0, boolean var1) {
      setQuickScaleEnabled((ScaleGestureDetector)var0, var1);
   }
}
