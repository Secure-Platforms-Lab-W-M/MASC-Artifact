package androidx.transition;

import android.view.View;

class ViewUtilsApi19 extends ViewUtilsBase {
   private static boolean sTryHiddenTransitionAlpha = true;

   public void clearNonTransitionAlpha(View var1) {
   }

   public float getTransitionAlpha(View var1) {
      if (sTryHiddenTransitionAlpha) {
         try {
            float var2 = var1.getTransitionAlpha();
            return var2;
         } catch (NoSuchMethodError var4) {
            sTryHiddenTransitionAlpha = false;
         }
      }

      return var1.getAlpha();
   }

   public void saveNonTransitionAlpha(View var1) {
   }

   public void setTransitionAlpha(View var1, float var2) {
      if (sTryHiddenTransitionAlpha) {
         try {
            var1.setTransitionAlpha(var2);
            return;
         } catch (NoSuchMethodError var4) {
            sTryHiddenTransitionAlpha = false;
         }
      }

      var1.setAlpha(var2);
   }
}
