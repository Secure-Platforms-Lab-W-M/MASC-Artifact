package com.google.android.material.switchmaterial;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.appcompat.widget.SwitchCompat;
import com.google.android.material.R.attr;
import com.google.android.material.R.dimen;
import com.google.android.material.R.style;
import com.google.android.material.R.styleable;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.elevation.ElevationOverlayProvider;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;

public class SwitchMaterial extends SwitchCompat {
   private static final int DEF_STYLE_RES;
   private static final int[][] ENABLED_CHECKED_STATES;
   private final ElevationOverlayProvider elevationOverlayProvider;
   private ColorStateList materialThemeColorsThumbTintList;
   private ColorStateList materialThemeColorsTrackTintList;
   private boolean useMaterialThemeColors;

   static {
      DEF_STYLE_RES = style.Widget_MaterialComponents_CompoundButton_Switch;
      ENABLED_CHECKED_STATES = new int[][]{{16842910, 16842912}, {16842910, -16842912}, {-16842910, 16842912}, {-16842910, -16842912}};
   }

   public SwitchMaterial(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public SwitchMaterial(Context var1, AttributeSet var2) {
      this(var1, var2, attr.switchStyle);
   }

   public SwitchMaterial(Context var1, AttributeSet var2, int var3) {
      super(ThemeEnforcement.createThemedContext(var1, var2, var3, DEF_STYLE_RES), var2, var3);
      var1 = this.getContext();
      this.elevationOverlayProvider = new ElevationOverlayProvider(var1);
      TypedArray var4 = ThemeEnforcement.obtainStyledAttributes(var1, var2, styleable.SwitchMaterial, var3, DEF_STYLE_RES);
      this.useMaterialThemeColors = var4.getBoolean(styleable.SwitchMaterial_useMaterialThemeColors, false);
      var4.recycle();
   }

   private ColorStateList getMaterialThemeColorsThumbTintList() {
      if (this.materialThemeColorsThumbTintList == null) {
         int var3 = MaterialColors.getColor(this, attr.colorSurface);
         int var4 = MaterialColors.getColor(this, attr.colorControlActivated);
         float var2 = this.getResources().getDimension(dimen.mtrl_switch_thumb_elevation);
         float var1 = var2;
         if (this.elevationOverlayProvider.isThemeElevationOverlayEnabled()) {
            var1 = var2 + ViewUtils.getParentAbsoluteElevation(this);
         }

         int var5 = this.elevationOverlayProvider.compositeOverlayIfNeeded(var3, var1);
         int[] var6 = new int[ENABLED_CHECKED_STATES.length];
         var6[0] = MaterialColors.layer(var3, var4, 1.0F);
         var6[1] = var5;
         var6[2] = MaterialColors.layer(var3, var4, 0.38F);
         var6[3] = var5;
         this.materialThemeColorsThumbTintList = new ColorStateList(ENABLED_CHECKED_STATES, var6);
      }

      return this.materialThemeColorsThumbTintList;
   }

   private ColorStateList getMaterialThemeColorsTrackTintList() {
      if (this.materialThemeColorsTrackTintList == null) {
         int[] var4 = new int[ENABLED_CHECKED_STATES.length];
         int var1 = MaterialColors.getColor(this, attr.colorSurface);
         int var2 = MaterialColors.getColor(this, attr.colorControlActivated);
         int var3 = MaterialColors.getColor(this, attr.colorOnSurface);
         var4[0] = MaterialColors.layer(var1, var2, 0.54F);
         var4[1] = MaterialColors.layer(var1, var3, 0.32F);
         var4[2] = MaterialColors.layer(var1, var2, 0.12F);
         var4[3] = MaterialColors.layer(var1, var3, 0.12F);
         this.materialThemeColorsTrackTintList = new ColorStateList(ENABLED_CHECKED_STATES, var4);
      }

      return this.materialThemeColorsTrackTintList;
   }

   public boolean isUseMaterialThemeColors() {
      return this.useMaterialThemeColors;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      if (this.useMaterialThemeColors && this.getThumbTintList() == null) {
         this.setThumbTintList(this.getMaterialThemeColorsThumbTintList());
      }

      if (this.useMaterialThemeColors && this.getTrackTintList() == null) {
         this.setTrackTintList(this.getMaterialThemeColorsTrackTintList());
      }

   }

   public void setUseMaterialThemeColors(boolean var1) {
      this.useMaterialThemeColors = var1;
      if (var1) {
         this.setThumbTintList(this.getMaterialThemeColorsThumbTintList());
         this.setTrackTintList(this.getMaterialThemeColorsTrackTintList());
      } else {
         this.setThumbTintList((ColorStateList)null);
         this.setTrackTintList((ColorStateList)null);
      }
   }
}
