package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.View;

@TargetApi(19)
@RequiresApi(19)
class ViewCompatKitKat {
   public static int getAccessibilityLiveRegion(View var0) {
      return var0.getAccessibilityLiveRegion();
   }

   public static boolean isAttachedToWindow(View var0) {
      return var0.isAttachedToWindow();
   }

   public static boolean isLaidOut(View var0) {
      return var0.isLaidOut();
   }

   public static boolean isLayoutDirectionResolved(View var0) {
      return var0.isLayoutDirectionResolved();
   }

   public static void setAccessibilityLiveRegion(View var0, int var1) {
      var0.setAccessibilityLiveRegion(var1);
   }
}
