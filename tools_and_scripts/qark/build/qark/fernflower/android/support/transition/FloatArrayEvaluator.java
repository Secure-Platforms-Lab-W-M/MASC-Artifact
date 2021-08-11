package android.support.transition;

import android.animation.TypeEvaluator;

class FloatArrayEvaluator implements TypeEvaluator {
   private float[] mArray;

   FloatArrayEvaluator(float[] var1) {
      this.mArray = var1;
   }

   public float[] evaluate(float var1, float[] var2, float[] var3) {
      float[] var6 = this.mArray;
      if (var6 == null) {
         var6 = new float[var2.length];
      }

      for(int var5 = 0; var5 < var6.length; ++var5) {
         float var4 = var2[var5];
         var6[var5] = (var3[var5] - var4) * var1 + var4;
      }

      return var6;
   }
}
