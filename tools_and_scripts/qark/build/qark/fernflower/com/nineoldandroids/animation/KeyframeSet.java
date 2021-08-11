package com.nineoldandroids.animation;

import android.view.animation.Interpolator;
import java.util.ArrayList;
import java.util.Arrays;

class KeyframeSet {
   TypeEvaluator mEvaluator;
   Keyframe mFirstKeyframe;
   Interpolator mInterpolator;
   ArrayList mKeyframes;
   Keyframe mLastKeyframe;
   int mNumKeyframes;

   public KeyframeSet(Keyframe... var1) {
      this.mNumKeyframes = var1.length;
      ArrayList var2 = new ArrayList();
      this.mKeyframes = var2;
      var2.addAll(Arrays.asList(var1));
      this.mFirstKeyframe = (Keyframe)this.mKeyframes.get(0);
      Keyframe var3 = (Keyframe)this.mKeyframes.get(this.mNumKeyframes - 1);
      this.mLastKeyframe = var3;
      this.mInterpolator = var3.getInterpolator();
   }

   public static KeyframeSet ofFloat(float... var0) {
      int var2 = var0.length;
      Keyframe.FloatKeyframe[] var3 = new Keyframe.FloatKeyframe[Math.max(var2, 2)];
      if (var2 == 1) {
         var3[0] = (Keyframe.FloatKeyframe)Keyframe.ofFloat(0.0F);
         var3[1] = (Keyframe.FloatKeyframe)Keyframe.ofFloat(1.0F, var0[0]);
      } else {
         var3[0] = (Keyframe.FloatKeyframe)Keyframe.ofFloat(0.0F, var0[0]);

         for(int var1 = 1; var1 < var2; ++var1) {
            var3[var1] = (Keyframe.FloatKeyframe)Keyframe.ofFloat((float)var1 / (float)(var2 - 1), var0[var1]);
         }
      }

      return new FloatKeyframeSet(var3);
   }

   public static KeyframeSet ofInt(int... var0) {
      int var2 = var0.length;
      Keyframe.IntKeyframe[] var3 = new Keyframe.IntKeyframe[Math.max(var2, 2)];
      if (var2 == 1) {
         var3[0] = (Keyframe.IntKeyframe)Keyframe.ofInt(0.0F);
         var3[1] = (Keyframe.IntKeyframe)Keyframe.ofInt(1.0F, var0[0]);
      } else {
         var3[0] = (Keyframe.IntKeyframe)Keyframe.ofInt(0.0F, var0[0]);

         for(int var1 = 1; var1 < var2; ++var1) {
            var3[var1] = (Keyframe.IntKeyframe)Keyframe.ofInt((float)var1 / (float)(var2 - 1), var0[var1]);
         }
      }

      return new IntKeyframeSet(var3);
   }

   public static KeyframeSet ofKeyframe(Keyframe... var0) {
      int var5 = var0.length;
      boolean var4 = false;
      boolean var3 = false;
      boolean var2 = false;

      int var1;
      for(var1 = 0; var1 < var5; ++var1) {
         if (var0[var1] instanceof Keyframe.FloatKeyframe) {
            var4 = true;
         } else if (var0[var1] instanceof Keyframe.IntKeyframe) {
            var3 = true;
         } else {
            var2 = true;
         }
      }

      if (var4 && !var3 && !var2) {
         Keyframe.FloatKeyframe[] var7 = new Keyframe.FloatKeyframe[var5];

         for(var1 = 0; var1 < var5; ++var1) {
            var7[var1] = (Keyframe.FloatKeyframe)var0[var1];
         }

         return new FloatKeyframeSet(var7);
      } else if (var3 && !var4 && !var2) {
         Keyframe.IntKeyframe[] var6 = new Keyframe.IntKeyframe[var5];

         for(var1 = 0; var1 < var5; ++var1) {
            var6[var1] = (Keyframe.IntKeyframe)var0[var1];
         }

         return new IntKeyframeSet(var6);
      } else {
         return new KeyframeSet(var0);
      }
   }

   public static KeyframeSet ofObject(Object... var0) {
      int var2 = var0.length;
      Keyframe.ObjectKeyframe[] var3 = new Keyframe.ObjectKeyframe[Math.max(var2, 2)];
      if (var2 == 1) {
         var3[0] = (Keyframe.ObjectKeyframe)Keyframe.ofObject(0.0F);
         var3[1] = (Keyframe.ObjectKeyframe)Keyframe.ofObject(1.0F, var0[0]);
      } else {
         var3[0] = (Keyframe.ObjectKeyframe)Keyframe.ofObject(0.0F, var0[0]);

         for(int var1 = 1; var1 < var2; ++var1) {
            var3[var1] = (Keyframe.ObjectKeyframe)Keyframe.ofObject((float)var1 / (float)(var2 - 1), var0[var1]);
         }
      }

      return new KeyframeSet(var3);
   }

   public KeyframeSet clone() {
      ArrayList var3 = this.mKeyframes;
      int var2 = this.mKeyframes.size();
      Keyframe[] var4 = new Keyframe[var2];

      for(int var1 = 0; var1 < var2; ++var1) {
         var4[var1] = ((Keyframe)var3.get(var1)).clone();
      }

      return new KeyframeSet(var4);
   }

   public Object getValue(float var1) {
      int var3 = this.mNumKeyframes;
      float var2;
      if (var3 == 2) {
         Interpolator var7 = this.mInterpolator;
         var2 = var1;
         if (var7 != null) {
            var2 = var7.getInterpolation(var1);
         }

         return this.mEvaluator.evaluate(var2, this.mFirstKeyframe.getValue(), this.mLastKeyframe.getValue());
      } else {
         Keyframe var4;
         Interpolator var8;
         if (var1 <= 0.0F) {
            var4 = (Keyframe)this.mKeyframes.get(1);
            var8 = var4.getInterpolator();
            var2 = var1;
            if (var8 != null) {
               var2 = var8.getInterpolation(var1);
            }

            var1 = this.mFirstKeyframe.getFraction();
            var1 = (var2 - var1) / (var4.getFraction() - var1);
            return this.mEvaluator.evaluate(var1, this.mFirstKeyframe.getValue(), var4.getValue());
         } else if (var1 >= 1.0F) {
            var4 = (Keyframe)this.mKeyframes.get(var3 - 2);
            var8 = this.mLastKeyframe.getInterpolator();
            var2 = var1;
            if (var8 != null) {
               var2 = var8.getInterpolation(var1);
            }

            var1 = var4.getFraction();
            var1 = (var2 - var1) / (this.mLastKeyframe.getFraction() - var1);
            return this.mEvaluator.evaluate(var1, var4.getValue(), this.mLastKeyframe.getValue());
         } else {
            var4 = this.mFirstKeyframe;

            for(var3 = 1; var3 < this.mNumKeyframes; ++var3) {
               Keyframe var5 = (Keyframe)this.mKeyframes.get(var3);
               if (var1 < var5.getFraction()) {
                  Interpolator var6 = var5.getInterpolator();
                  var2 = var1;
                  if (var6 != null) {
                     var2 = var6.getInterpolation(var1);
                  }

                  var1 = var4.getFraction();
                  var1 = (var2 - var1) / (var5.getFraction() - var1);
                  return this.mEvaluator.evaluate(var1, var4.getValue(), var5.getValue());
               }

               var4 = var5;
            }

            return this.mLastKeyframe.getValue();
         }
      }
   }

   public void setEvaluator(TypeEvaluator var1) {
      this.mEvaluator = var1;
   }

   public String toString() {
      String var2 = " ";

      for(int var1 = 0; var1 < this.mNumKeyframes; ++var1) {
         StringBuilder var3 = new StringBuilder();
         var3.append(var2);
         var3.append(((Keyframe)this.mKeyframes.get(var1)).getValue());
         var3.append("  ");
         var2 = var3.toString();
      }

      return var2;
   }
}
