package com.google.android.material.elevation;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import androidx.core.graphics.ColorUtils;
import com.google.android.material.R.attr;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialAttributes;

public class ElevationOverlayProvider {
   private static final float FORMULA_MULTIPLIER = 4.5F;
   private static final float FORMULA_OFFSET = 2.0F;
   private final int colorSurface;
   private final float displayDensity;
   private final int elevationOverlayColor;
   private final boolean elevationOverlayEnabled;

   public ElevationOverlayProvider(Context var1) {
      this.elevationOverlayEnabled = MaterialAttributes.resolveBoolean(var1, attr.elevationOverlayEnabled, false);
      this.elevationOverlayColor = MaterialColors.getColor((Context)var1, attr.elevationOverlayColor, 0);
      this.colorSurface = MaterialColors.getColor((Context)var1, attr.colorSurface, 0);
      this.displayDensity = var1.getResources().getDisplayMetrics().density;
   }

   private boolean isThemeSurfaceColor(int var1) {
      return ColorUtils.setAlphaComponent(var1, 255) == this.colorSurface;
   }

   public int calculateOverlayAlpha(float var1) {
      return Math.round(this.calculateOverlayAlphaFraction(var1) * 255.0F);
   }

   public float calculateOverlayAlphaFraction(float var1) {
      float var2 = this.displayDensity;
      if (var2 > 0.0F) {
         return var1 <= 0.0F ? 0.0F : Math.min(((float)Math.log1p((double)(var1 / var2)) * 4.5F + 2.0F) / 100.0F, 1.0F);
      } else {
         return 0.0F;
      }
   }

   public int compositeOverlay(int var1, float var2) {
      var2 = this.calculateOverlayAlphaFraction(var2);
      int var3 = Color.alpha(var1);
      return ColorUtils.setAlphaComponent(MaterialColors.layer(ColorUtils.setAlphaComponent(var1, 255), this.elevationOverlayColor, var2), var3);
   }

   public int compositeOverlay(int var1, float var2, View var3) {
      return this.compositeOverlay(var1, var2 + this.getParentAbsoluteElevation(var3));
   }

   public int compositeOverlayIfNeeded(int var1, float var2) {
      return this.elevationOverlayEnabled && this.isThemeSurfaceColor(var1) ? this.compositeOverlay(var1, var2) : var1;
   }

   public int compositeOverlayIfNeeded(int var1, float var2, View var3) {
      return this.compositeOverlayIfNeeded(var1, var2 + this.getParentAbsoluteElevation(var3));
   }

   public int compositeOverlayWithThemeSurfaceColorIfNeeded(float var1) {
      return this.compositeOverlayIfNeeded(this.colorSurface, var1);
   }

   public int compositeOverlayWithThemeSurfaceColorIfNeeded(float var1, View var2) {
      return this.compositeOverlayWithThemeSurfaceColorIfNeeded(var1 + this.getParentAbsoluteElevation(var2));
   }

   public float getParentAbsoluteElevation(View var1) {
      return ViewUtils.getParentAbsoluteElevation(var1);
   }

   public int getThemeElevationOverlayColor() {
      return this.elevationOverlayColor;
   }

   public int getThemeSurfaceColor() {
      return this.colorSurface;
   }

   public boolean isThemeElevationOverlayEnabled() {
      return this.elevationOverlayEnabled;
   }
}
