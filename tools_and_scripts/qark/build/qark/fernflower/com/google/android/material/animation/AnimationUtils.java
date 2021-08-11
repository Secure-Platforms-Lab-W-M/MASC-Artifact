package com.google.android.material.animation;

import android.animation.TimeInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

public class AnimationUtils {
   public static final TimeInterpolator DECELERATE_INTERPOLATOR = new DecelerateInterpolator();
   public static final TimeInterpolator FAST_OUT_LINEAR_IN_INTERPOLATOR = new FastOutLinearInInterpolator();
   public static final TimeInterpolator FAST_OUT_SLOW_IN_INTERPOLATOR = new FastOutSlowInInterpolator();
   public static final TimeInterpolator LINEAR_INTERPOLATOR = new LinearInterpolator();
   public static final TimeInterpolator LINEAR_OUT_SLOW_IN_INTERPOLATOR = new LinearOutSlowInInterpolator();

   public static float lerp(float var0, float var1, float var2) {
      return (var1 - var0) * var2 + var0;
   }

   public static int lerp(int var0, int var1, float var2) {
      return Math.round((float)(var1 - var0) * var2) + var0;
   }
}
