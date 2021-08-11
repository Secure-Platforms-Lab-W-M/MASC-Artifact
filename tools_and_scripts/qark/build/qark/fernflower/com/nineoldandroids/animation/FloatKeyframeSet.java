package com.nineoldandroids.animation;

import android.view.animation.Interpolator;
import java.util.ArrayList;

class FloatKeyframeSet extends KeyframeSet {
   private float deltaValue;
   private boolean firstTime = true;
   private float firstValue;
   private float lastValue;

   public FloatKeyframeSet(Keyframe.FloatKeyframe... var1) {
      super(var1);
   }

   public FloatKeyframeSet clone() {
      ArrayList var3 = this.mKeyframes;
      int var2 = this.mKeyframes.size();
      Keyframe.FloatKeyframe[] var4 = new Keyframe.FloatKeyframe[var2];

      for(int var1 = 0; var1 < var2; ++var1) {
         var4[var1] = (Keyframe.FloatKeyframe)((Keyframe)var3.get(var1)).clone();
      }

      return new FloatKeyframeSet(var4);
   }

   public float getFloatValue(float var1) {
      float var2;
      if (this.mNumKeyframes == 2) {
         if (this.firstTime) {
            this.firstTime = false;
            this.firstValue = ((Keyframe.FloatKeyframe)this.mKeyframes.get(0)).getFloatValue();
            var2 = ((Keyframe.FloatKeyframe)this.mKeyframes.get(1)).getFloatValue();
            this.lastValue = var2;
            this.deltaValue = var2 - this.firstValue;
         }

         var2 = var1;
         if (this.mInterpolator != null) {
            var2 = this.mInterpolator.getInterpolation(var1);
         }

         return this.mEvaluator == null ? this.firstValue + this.deltaValue * var2 : ((Number)this.mEvaluator.evaluate(var2, this.firstValue, this.lastValue)).floatValue();
      } else {
         float var3;
         float var4;
         float var5;
         float var6;
         Keyframe.FloatKeyframe var8;
         Keyframe.FloatKeyframe var9;
         Interpolator var11;
         if (var1 <= 0.0F) {
            var8 = (Keyframe.FloatKeyframe)this.mKeyframes.get(0);
            var9 = (Keyframe.FloatKeyframe)this.mKeyframes.get(1);
            var3 = var8.getFloatValue();
            var4 = var9.getFloatValue();
            var5 = var8.getFraction();
            var6 = var9.getFraction();
            var11 = var9.getInterpolator();
            var2 = var1;
            if (var11 != null) {
               var2 = var11.getInterpolation(var1);
            }

            var1 = (var2 - var5) / (var6 - var5);
            return this.mEvaluator == null ? (var4 - var3) * var1 + var3 : ((Number)this.mEvaluator.evaluate(var1, var3, var4)).floatValue();
         } else if (var1 >= 1.0F) {
            var8 = (Keyframe.FloatKeyframe)this.mKeyframes.get(this.mNumKeyframes - 2);
            var9 = (Keyframe.FloatKeyframe)this.mKeyframes.get(this.mNumKeyframes - 1);
            var3 = var8.getFloatValue();
            var4 = var9.getFloatValue();
            var5 = var8.getFraction();
            var6 = var9.getFraction();
            var11 = var9.getInterpolator();
            var2 = var1;
            if (var11 != null) {
               var2 = var11.getInterpolation(var1);
            }

            var1 = (var2 - var5) / (var6 - var5);
            return this.mEvaluator == null ? (var4 - var3) * var1 + var3 : ((Number)this.mEvaluator.evaluate(var1, var3, var4)).floatValue();
         } else {
            var8 = (Keyframe.FloatKeyframe)this.mKeyframes.get(0);

            for(int var7 = 1; var7 < this.mNumKeyframes; ++var7) {
               var9 = (Keyframe.FloatKeyframe)this.mKeyframes.get(var7);
               if (var1 < var9.getFraction()) {
                  Interpolator var10 = var9.getInterpolator();
                  var2 = var1;
                  if (var10 != null) {
                     var2 = var10.getInterpolation(var1);
                  }

                  var1 = (var2 - var8.getFraction()) / (var9.getFraction() - var8.getFraction());
                  var2 = var8.getFloatValue();
                  var3 = var9.getFloatValue();
                  if (this.mEvaluator == null) {
                     return (var3 - var2) * var1 + var2;
                  }

                  return ((Number)this.mEvaluator.evaluate(var1, var2, var3)).floatValue();
               }

               var8 = var9;
            }

            return ((Number)((Keyframe)this.mKeyframes.get(this.mNumKeyframes - 1)).getValue()).floatValue();
         }
      }
   }

   public Object getValue(float var1) {
      return this.getFloatValue(var1);
   }
}
