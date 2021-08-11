package androidx.transition;

import android.os.Build.VERSION;
import android.view.View;

class ViewUtilsApi23 extends ViewUtilsApi22 {
   private static boolean sTryHiddenSetTransitionVisibility = true;

   public void setTransitionVisibility(View var1, int var2) {
      if (VERSION.SDK_INT == 28) {
         super.setTransitionVisibility(var1, var2);
      } else {
         if (sTryHiddenSetTransitionVisibility) {
            try {
               var1.setTransitionVisibility(var2);
               return;
            } catch (NoSuchMethodError var3) {
               sTryHiddenSetTransitionVisibility = false;
            }
         }

      }
   }
}
