package android.support.transition;

import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

class ViewGroupUtils {
   private static final ViewGroupUtilsImpl IMPL;

   static {
      if (VERSION.SDK_INT >= 18) {
         IMPL = new ViewGroupUtilsApi18();
      } else {
         IMPL = new ViewGroupUtilsApi14();
      }
   }

   static ViewGroupOverlayImpl getOverlay(@NonNull ViewGroup var0) {
      return IMPL.getOverlay(var0);
   }

   static void suppressLayout(@NonNull ViewGroup var0, boolean var1) {
      IMPL.suppressLayout(var0, var1);
   }
}
