package androidx.transition;

import android.graphics.Matrix;
import android.view.View;

class ViewUtilsApi29 extends ViewUtilsApi23 {
   public float getTransitionAlpha(View var1) {
      return var1.getTransitionAlpha();
   }

   public void setAnimationMatrix(View var1, Matrix var2) {
      var1.setAnimationMatrix(var2);
   }

   public void setLeftTopRightBottom(View var1, int var2, int var3, int var4, int var5) {
      var1.setLeftTopRightBottom(var2, var3, var4, var5);
   }

   public void setTransitionAlpha(View var1, float var2) {
      var1.setTransitionAlpha(var2);
   }

   public void setTransitionVisibility(View var1, int var2) {
      var1.setTransitionVisibility(var2);
   }

   public void transformMatrixToGlobal(View var1, Matrix var2) {
      var1.transformMatrixToGlobal(var2);
   }

   public void transformMatrixToLocal(View var1, Matrix var2) {
      var1.transformMatrixToLocal(var2);
   }
}
