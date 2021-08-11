package com.google.android.material.color;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import androidx.core.graphics.ColorUtils;
import com.google.android.material.resources.MaterialAttributes;

public class MaterialColors {
   public static final float ALPHA_DISABLED = 0.38F;
   public static final float ALPHA_DISABLED_LOW = 0.12F;
   public static final float ALPHA_FULL = 1.0F;
   public static final float ALPHA_LOW = 0.32F;
   public static final float ALPHA_MEDIUM = 0.54F;

   public static int getColor(Context var0, int var1, int var2) {
      TypedValue var3 = MaterialAttributes.resolve(var0, var1);
      return var3 != null ? var3.data : var2;
   }

   public static int getColor(Context var0, int var1, String var2) {
      return MaterialAttributes.resolveOrThrow(var0, var1, var2);
   }

   public static int getColor(View var0, int var1) {
      return MaterialAttributes.resolveOrThrow(var0, var1);
   }

   public static int getColor(View var0, int var1, int var2) {
      return getColor(var0.getContext(), var1, var2);
   }

   public static int layer(int var0, int var1) {
      return ColorUtils.compositeColors(var1, var0);
   }

   public static int layer(int var0, int var1, float var2) {
      return layer(var0, ColorUtils.setAlphaComponent(var1, Math.round((float)Color.alpha(var1) * var2)));
   }

   public static int layer(View var0, int var1, int var2) {
      return layer(var0, var1, var2, 1.0F);
   }

   public static int layer(View var0, int var1, int var2, float var3) {
      return layer(getColor(var0, var1), getColor(var0, var2), var3);
   }
}
