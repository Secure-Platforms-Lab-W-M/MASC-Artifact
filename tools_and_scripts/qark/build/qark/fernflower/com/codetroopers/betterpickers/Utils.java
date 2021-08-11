package com.codetroopers.betterpickers;

import android.os.Build.VERSION;
import android.view.View;
import com.nineoldandroids.animation.Keyframe;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;
import com.nineoldandroids.view.animation.AnimatorProxy;

public class Utils {
   public static final int FULL_ALPHA = 255;
   public static final int MONDAY_BEFORE_JULIAN_EPOCH = 2440585;
   public static final int PULSE_ANIMATOR_DURATION = 544;
   static final String SHARED_PREFS_NAME = "com.android.calendar_preferences";

   public static int formatDisabledDayForKey(int var0, int var1, int var2) {
      return var0 * 10000 + var1 * 100 + var2;
   }

   public static int getDaysInMonth(int var0, int var1) {
      switch(var0) {
      case 0:
      case 2:
      case 4:
      case 6:
      case 7:
      case 9:
      case 11:
         return 31;
      case 1:
         return (var1 % 4 != 0 || var1 % 100 == 0) && var1 % 400 != 0 ? 28 : 29;
      case 3:
      case 5:
      case 8:
      case 10:
         return 30;
      default:
         throw new IllegalArgumentException("Invalid Month");
      }
   }

   public static int getJulianMondayFromWeeksSinceEpoch(int var0) {
      return var0 * 7 + 2440585;
   }

   public static ObjectAnimator getPulseAnimator(View var0, float var1, float var2) {
      Keyframe var4 = Keyframe.ofFloat(0.0F, 1.0F);
      Keyframe var5 = Keyframe.ofFloat(0.275F, var1);
      Keyframe var6 = Keyframe.ofFloat(0.69F, var2);
      Keyframe var7 = Keyframe.ofFloat(1.0F, 1.0F);
      PropertyValuesHolder var3 = PropertyValuesHolder.ofKeyframe("scaleX", var4, var5, var6, var7);
      PropertyValuesHolder var9 = PropertyValuesHolder.ofKeyframe("scaleY", var4, var5, var6, var7);
      if (AnimatorProxy.NEEDS_PROXY) {
         var0 = AnimatorProxy.wrap((View)var0);
      }

      ObjectAnimator var8 = ObjectAnimator.ofPropertyValuesHolder(var0, var3, var9);
      var8.setDuration(544L);
      return var8;
   }

   public static int getWeeksSinceEpochFromJulianDay(int var0, int var1) {
      int var2 = 4 - var1;
      var1 = var2;
      if (var2 < 0) {
         var1 = var2 + 7;
      }

      return (var0 - (2440588 - var1)) / 7;
   }

   public static boolean isJellybeanOrLater() {
      return VERSION.SDK_INT >= 16;
   }

   public static void tryAccessibilityAnnounce(View var0, CharSequence var1) {
      if (isJellybeanOrLater() && var0 != null && var1 != null) {
         var0.announceForAccessibility(var1);
      }

   }
}
