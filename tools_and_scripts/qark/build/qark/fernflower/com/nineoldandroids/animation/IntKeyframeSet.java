package com.nineoldandroids.animation;

import android.view.animation.Interpolator;
import java.util.ArrayList;

class IntKeyframeSet extends KeyframeSet {
   private int deltaValue;
   private boolean firstTime = true;
   private int firstValue;
   private int lastValue;

   public IntKeyframeSet(Keyframe.IntKeyframe... var1) {
      super(var1);
   }

   public IntKeyframeSet clone() {
      ArrayList var3 = this.mKeyframes;
      int var2 = this.mKeyframes.size();
      Keyframe.IntKeyframe[] var4 = new Keyframe.IntKeyframe[var2];

      for(int var1 = 0; var1 < var2; ++var1) {
         var4[var1] = (Keyframe.IntKeyframe)((Keyframe)var3.get(var1)).clone();
      }

      return new IntKeyframeSet(var4);
   }

   public int getIntValue(float var1) {
      float var2;
      int var5;
      if (this.mNumKeyframes == 2) {
         if (this.firstTime) {
            this.firstTime = false;
            this.firstValue = ((Keyframe.IntKeyframe)this.mKeyframes.get(0)).getIntValue();
            var5 = ((Keyframe.IntKeyframe)this.mKeyframes.get(1)).getIntValue();
            this.lastValue = var5;
            this.deltaValue = var5 - this.firstValue;
         }

         var2 = var1;
         if (this.mInterpolator != null) {
            var2 = this.mInterpolator.getInterpolation(var1);
         }

         return this.mEvaluator == null ? this.firstValue + (int)((float)this.deltaValue * var2) : ((Number)this.mEvaluator.evaluate(var2, this.firstValue, this.lastValue)).intValue();
      } else {
         float var3;
         float var4;
         int var6;
         Keyframe.IntKeyframe var7;
         Keyframe.IntKeyframe var8;
         Interpolator var10;
         if (var1 <= 0.0F) {
            var7 = (Keyframe.IntKeyframe)this.mKeyframes.get(0);
            var8 = (Keyframe.IntKeyframe)this.mKeyframes.get(1);
            var5 = var7.getIntValue();
            var6 = var8.getIntValue();
            var3 = var7.getFraction();
            var4 = var8.getFraction();
            var10 = var8.getInterpolator();
            var2 = var1;
            if (var10 != null) {
               var2 = var10.getInterpolation(var1);
            }

            var1 = (var2 - var3) / (var4 - var3);
            return this.mEvaluator == null ? (int)((float)(var6 - var5) * var1) + var5 : ((Number)this.mEvaluator.evaluate(var1, var5, var6)).intValue();
         } else if (var1 >= 1.0F) {
            var7 = (Keyframe.IntKeyframe)this.mKeyframes.get(this.mNumKeyframes - 2);
            var8 = (Keyframe.IntKeyframe)this.mKeyframes.get(this.mNumKeyframes - 1);
            var5 = var7.getIntValue();
            var6 = var8.getIntValue();
            var3 = var7.getFraction();
            var4 = var8.getFraction();
            var10 = var8.getInterpolator();
            var2 = var1;
            if (var10 != null) {
               var2 = var10.getInterpolation(var1);
            }

            var1 = (var2 - var3) / (var4 - var3);
            return this.mEvaluator == null ? (int)((float)(var6 - var5) * var1) + var5 : ((Number)this.mEvaluator.evaluate(var1, var5, var6)).intValue();
         } else {
            var7 = (Keyframe.IntKeyframe)this.mKeyframes.get(0);

            for(var5 = 1; var5 < this.mNumKeyframes; ++var5) {
               var8 = (Keyframe.IntKeyframe)this.mKeyframes.get(var5);
               if (var1 < var8.getFraction()) {
                  Interpolator var9 = var8.getInterpolator();
                  var2 = var1;
                  if (var9 != null) {
                     var2 = var9.getInterpolation(var1);
                  }

                  var1 = (var2 - var7.getFraction()) / (var8.getFraction() - var7.getFraction());
                  var5 = var7.getIntValue();
                  var6 = var8.getIntValue();
                  if (this.mEvaluator == null) {
                     return (int)((float)(var6 - var5) * var1) + var5;
                  }

                  return ((Number)this.mEvaluator.evaluate(var1, var5, var6)).intValue();
               }

               var7 = var8;
            }

            return ((Number)((Keyframe)this.mKeyframes.get(this.mNumKeyframes - 1)).getValue()).intValue();
         }
      }
   }

   public Object getValue(float var1) {
      return this.getIntValue(var1);
   }
}
