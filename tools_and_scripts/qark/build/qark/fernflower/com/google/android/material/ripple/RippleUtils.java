package com.google.android.material.ripple;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.util.Log;
import android.util.StateSet;
import androidx.core.graphics.ColorUtils;

public class RippleUtils {
   private static final int[] ENABLED_PRESSED_STATE_SET;
   private static final int[] FOCUSED_STATE_SET;
   private static final int[] HOVERED_FOCUSED_STATE_SET;
   private static final int[] HOVERED_STATE_SET;
   static final String LOG_TAG;
   private static final int[] PRESSED_STATE_SET;
   private static final int[] SELECTED_FOCUSED_STATE_SET;
   private static final int[] SELECTED_HOVERED_FOCUSED_STATE_SET;
   private static final int[] SELECTED_HOVERED_STATE_SET;
   private static final int[] SELECTED_PRESSED_STATE_SET;
   private static final int[] SELECTED_STATE_SET;
   static final String TRANSPARENT_DEFAULT_COLOR_WARNING = "Use a non-transparent color for the default color as it will be used to finish ripple animations.";
   public static final boolean USE_FRAMEWORK_RIPPLE;

   static {
      boolean var0;
      if (VERSION.SDK_INT >= 21) {
         var0 = true;
      } else {
         var0 = false;
      }

      USE_FRAMEWORK_RIPPLE = var0;
      PRESSED_STATE_SET = new int[]{16842919};
      HOVERED_FOCUSED_STATE_SET = new int[]{16843623, 16842908};
      FOCUSED_STATE_SET = new int[]{16842908};
      HOVERED_STATE_SET = new int[]{16843623};
      SELECTED_PRESSED_STATE_SET = new int[]{16842913, 16842919};
      SELECTED_HOVERED_FOCUSED_STATE_SET = new int[]{16842913, 16843623, 16842908};
      SELECTED_FOCUSED_STATE_SET = new int[]{16842913, 16842908};
      SELECTED_HOVERED_STATE_SET = new int[]{16842913, 16843623};
      SELECTED_STATE_SET = new int[]{16842913};
      ENABLED_PRESSED_STATE_SET = new int[]{16842910, 16842919};
      LOG_TAG = RippleUtils.class.getSimpleName();
   }

   private RippleUtils() {
   }

   public static ColorStateList convertToRippleDrawableColor(ColorStateList var0) {
      int var1;
      int[][] var2;
      int[] var3;
      if (USE_FRAMEWORK_RIPPLE) {
         var2 = new int[2][];
         var3 = new int[2];
         var2[0] = SELECTED_STATE_SET;
         var3[0] = getColorForState(var0, SELECTED_PRESSED_STATE_SET);
         var1 = 0 + 1;
         var2[var1] = StateSet.NOTHING;
         var3[var1] = getColorForState(var0, PRESSED_STATE_SET);
         return new ColorStateList(var2, var3);
      } else {
         var2 = new int[10][];
         var3 = new int[10];
         int[] var4 = SELECTED_PRESSED_STATE_SET;
         var2[0] = var4;
         var3[0] = getColorForState(var0, var4);
         var1 = 0 + 1;
         var4 = SELECTED_HOVERED_FOCUSED_STATE_SET;
         var2[var1] = var4;
         var3[var1] = getColorForState(var0, var4);
         ++var1;
         var4 = SELECTED_FOCUSED_STATE_SET;
         var2[var1] = var4;
         var3[var1] = getColorForState(var0, var4);
         ++var1;
         var4 = SELECTED_HOVERED_STATE_SET;
         var2[var1] = var4;
         var3[var1] = getColorForState(var0, var4);
         ++var1;
         var2[var1] = SELECTED_STATE_SET;
         var3[var1] = 0;
         ++var1;
         var4 = PRESSED_STATE_SET;
         var2[var1] = var4;
         var3[var1] = getColorForState(var0, var4);
         ++var1;
         var4 = HOVERED_FOCUSED_STATE_SET;
         var2[var1] = var4;
         var3[var1] = getColorForState(var0, var4);
         ++var1;
         var4 = FOCUSED_STATE_SET;
         var2[var1] = var4;
         var3[var1] = getColorForState(var0, var4);
         ++var1;
         var4 = HOVERED_STATE_SET;
         var2[var1] = var4;
         var3[var1] = getColorForState(var0, var4);
         ++var1;
         var2[var1] = StateSet.NOTHING;
         var3[var1] = 0;
         return new ColorStateList(var2, var3);
      }
   }

   private static int doubleAlpha(int var0) {
      return ColorUtils.setAlphaComponent(var0, Math.min(Color.alpha(var0) * 2, 255));
   }

   private static int getColorForState(ColorStateList var0, int[] var1) {
      int var2;
      if (var0 != null) {
         var2 = var0.getColorForState(var1, var0.getDefaultColor());
      } else {
         var2 = 0;
      }

      return USE_FRAMEWORK_RIPPLE ? doubleAlpha(var2) : var2;
   }

   public static ColorStateList sanitizeRippleDrawableColor(ColorStateList var0) {
      if (var0 != null) {
         if (VERSION.SDK_INT >= 22 && VERSION.SDK_INT <= 27 && Color.alpha(var0.getDefaultColor()) == 0 && Color.alpha(var0.getColorForState(ENABLED_PRESSED_STATE_SET, 0)) != 0) {
            Log.w(LOG_TAG, "Use a non-transparent color for the default color as it will be used to finish ripple animations.");
         }

         return var0;
      } else {
         return ColorStateList.valueOf(0);
      }
   }

   public static boolean shouldDrawRippleCompat(int[] var0) {
      boolean var3 = false;
      boolean var1 = false;
      int var5 = var0.length;
      boolean var8 = false;

      boolean var4;
      for(int var2 = 0; var2 < var5; var3 = var4) {
         int var6 = var0[var2];
         if (var6 == 16842910) {
            var4 = true;
         } else if (var6 == 16842908) {
            var1 = true;
            var4 = var3;
         } else if (var6 == 16842919) {
            var1 = true;
            var4 = var3;
         } else {
            var4 = var3;
            if (var6 == 16843623) {
               var1 = true;
               var4 = var3;
            }
         }

         ++var2;
      }

      boolean var7 = var8;
      if (var3) {
         var7 = var8;
         if (var1) {
            var7 = true;
         }
      }

      return var7;
   }
}
