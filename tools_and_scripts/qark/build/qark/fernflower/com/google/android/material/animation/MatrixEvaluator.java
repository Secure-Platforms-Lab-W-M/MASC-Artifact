package com.google.android.material.animation;

import android.animation.TypeEvaluator;
import android.graphics.Matrix;

public class MatrixEvaluator implements TypeEvaluator {
   private final float[] tempEndValues = new float[9];
   private final Matrix tempMatrix = new Matrix();
   private final float[] tempStartValues = new float[9];

   public Matrix evaluate(float var1, Matrix var2, Matrix var3) {
      var2.getValues(this.tempStartValues);
      var3.getValues(this.tempEndValues);

      for(int var6 = 0; var6 < 9; ++var6) {
         float[] var7 = this.tempEndValues;
         float var4 = var7[var6];
         float[] var8 = this.tempStartValues;
         float var5 = var8[var6];
         var7[var6] = var8[var6] + var1 * (var4 - var5);
      }

      this.tempMatrix.setValues(this.tempEndValues);
      return this.tempMatrix;
   }
}
