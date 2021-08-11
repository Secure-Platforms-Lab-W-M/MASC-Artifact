package com.google.android.material.checkbox;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.widget.CompoundButtonCompat;
import com.google.android.material.R.attr;
import com.google.android.material.R.style;
import com.google.android.material.R.styleable;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.resources.MaterialResources;

public class MaterialCheckBox extends AppCompatCheckBox {
   private static final int DEF_STYLE_RES;
   private static final int[][] ENABLED_CHECKED_STATES;
   private ColorStateList materialThemeColorsTintList;
   private boolean useMaterialThemeColors;

   static {
      DEF_STYLE_RES = style.Widget_MaterialComponents_CompoundButton_CheckBox;
      int[] var0 = new int[]{16842910, -16842912};
      int[] var1 = new int[]{-16842910, -16842912};
      ENABLED_CHECKED_STATES = new int[][]{{16842910, 16842912}, var0, {-16842910, 16842912}, var1};
   }

   public MaterialCheckBox(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public MaterialCheckBox(Context var1, AttributeSet var2) {
      this(var1, var2, attr.checkboxStyle);
   }

   public MaterialCheckBox(Context var1, AttributeSet var2, int var3) {
      super(ThemeEnforcement.createThemedContext(var1, var2, var3, DEF_STYLE_RES), var2, var3);
      var1 = this.getContext();
      TypedArray var4 = ThemeEnforcement.obtainStyledAttributes(var1, var2, styleable.MaterialCheckBox, var3, DEF_STYLE_RES);
      if (var4.hasValue(styleable.MaterialCheckBox_buttonTint)) {
         CompoundButtonCompat.setButtonTintList(this, MaterialResources.getColorStateList(var1, var4, styleable.MaterialCheckBox_buttonTint));
      }

      this.useMaterialThemeColors = var4.getBoolean(styleable.MaterialCheckBox_useMaterialThemeColors, false);
      var4.recycle();
   }

   private ColorStateList getMaterialThemeColorsTintList() {
      if (this.materialThemeColorsTintList == null) {
         int[] var4 = new int[ENABLED_CHECKED_STATES.length];
         int var1 = MaterialColors.getColor(this, attr.colorControlActivated);
         int var2 = MaterialColors.getColor(this, attr.colorSurface);
         int var3 = MaterialColors.getColor(this, attr.colorOnSurface);
         var4[0] = MaterialColors.layer(var2, var1, 1.0F);
         var4[1] = MaterialColors.layer(var2, var3, 0.54F);
         var4[2] = MaterialColors.layer(var2, var3, 0.38F);
         var4[3] = MaterialColors.layer(var2, var3, 0.38F);
         this.materialThemeColorsTintList = new ColorStateList(ENABLED_CHECKED_STATES, var4);
      }

      return this.materialThemeColorsTintList;
   }

   public boolean isUseMaterialThemeColors() {
      return this.useMaterialThemeColors;
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      if (this.useMaterialThemeColors && CompoundButtonCompat.getButtonTintList(this) == null) {
         this.setUseMaterialThemeColors(true);
      }

   }

   public void setUseMaterialThemeColors(boolean var1) {
      this.useMaterialThemeColors = var1;
      if (var1) {
         CompoundButtonCompat.setButtonTintList(this, this.getMaterialThemeColorsTintList());
      } else {
         CompoundButtonCompat.setButtonTintList(this, (ColorStateList)null);
      }
   }
}
