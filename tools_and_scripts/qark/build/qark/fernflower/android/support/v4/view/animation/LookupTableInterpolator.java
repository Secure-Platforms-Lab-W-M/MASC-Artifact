package android.support.v4.view.animation;

import android.view.animation.Interpolator;

abstract class LookupTableInterpolator implements Interpolator {
   private final float mStepSize;
   private final float[] mValues;

   public LookupTableInterpolator(float[] var1) {
      this.mValues = var1;
      this.mStepSize = 1.0F / (float)(this.mValues.length - 1);
   }

   public float getInterpolation(float var1) {
      if (var1 >= 1.0F) {
         return 1.0F;
      } else if (var1 <= 0.0F) {
         return 0.0F;
      } else {
         float[] var5 = this.mValues;
         int var4 = Math.min((int)((float)(var5.length - 1) * var1), var5.length - 2);
         float var2 = (float)var4;
         float var3 = this.mStepSize;
         var1 = (var1 - var2 * var3) / var3;
         var5 = this.mValues;
         return var5[var4] + (var5[var4 + 1] - var5[var4]) * var1;
      }
   }
}
