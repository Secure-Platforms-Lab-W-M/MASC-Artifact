package androidx.transition;

import android.view.View;

class ViewUtilsApi22 extends ViewUtilsApi21 {
   private static boolean sTryHiddenSetLeftTopRightBottom = true;

   public void setLeftTopRightBottom(View var1, int var2, int var3, int var4, int var5) {
      if (sTryHiddenSetLeftTopRightBottom) {
         try {
            var1.setLeftTopRightBottom(var2, var3, var4, var5);
            return;
         } catch (NoSuchMethodError var6) {
            sTryHiddenSetLeftTopRightBottom = false;
         }
      }

   }
}
