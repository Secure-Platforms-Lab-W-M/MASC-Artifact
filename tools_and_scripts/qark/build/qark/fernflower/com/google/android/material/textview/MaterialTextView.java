package com.google.android.material.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.Resources.Theme;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;
import com.google.android.material.R.attr;
import com.google.android.material.R.styleable;
import com.google.android.material.resources.MaterialAttributes;
import com.google.android.material.resources.MaterialResources;

public class MaterialTextView extends AppCompatTextView {
   public MaterialTextView(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public MaterialTextView(Context var1, AttributeSet var2) {
      this(var1, var2, 16842884);
   }

   public MaterialTextView(Context var1, AttributeSet var2, int var3) {
      this(var1, var2, var3, 0);
   }

   public MaterialTextView(Context var1, AttributeSet var2, int var3, int var4) {
      super(var1, var2, var3);
      if (canApplyTextAppearanceLineHeight(var1)) {
         Theme var5 = var1.getTheme();
         if (!viewAttrsHasLineHeight(var1, var5, var2, var3, var4)) {
            var3 = findViewAppearanceResourceId(var5, var2, var3, var4);
            if (var3 != -1) {
               this.applyLineHeightFromViewAppearance(var5, var3);
            }
         }
      }

   }

   private void applyLineHeightFromViewAppearance(Theme var1, int var2) {
      TypedArray var3 = var1.obtainStyledAttributes(var2, styleable.MaterialTextAppearance);
      var2 = readFirstAvailableDimension(this.getContext(), var3, styleable.MaterialTextAppearance_android_lineHeight, styleable.MaterialTextAppearance_lineHeight);
      var3.recycle();
      if (var2 >= 0) {
         this.setLineHeight(var2);
      }

   }

   private static boolean canApplyTextAppearanceLineHeight(Context var0) {
      return MaterialAttributes.resolveBoolean(var0, attr.textAppearanceLineHeightEnabled, true);
   }

   private static int findViewAppearanceResourceId(Theme var0, AttributeSet var1, int var2, int var3) {
      TypedArray var4 = var0.obtainStyledAttributes(var1, styleable.MaterialTextView, var2, var3);
      var2 = var4.getResourceId(styleable.MaterialTextView_android_textAppearance, -1);
      var4.recycle();
      return var2;
   }

   private static int readFirstAvailableDimension(Context var0, TypedArray var1, int... var2) {
      int var4 = -1;

      for(int var3 = 0; var3 < var2.length && var4 < 0; ++var3) {
         var4 = MaterialResources.getDimensionPixelSize(var0, var1, var2[var3], -1);
      }

      return var4;
   }

   private static boolean viewAttrsHasLineHeight(Context var0, Theme var1, AttributeSet var2, int var3, int var4) {
      TypedArray var6 = var1.obtainStyledAttributes(var2, styleable.MaterialTextView, var3, var4);
      var3 = styleable.MaterialTextView_android_lineHeight;
      boolean var5 = false;
      var3 = readFirstAvailableDimension(var0, var6, var3, styleable.MaterialTextView_lineHeight);
      var6.recycle();
      if (var3 != -1) {
         var5 = true;
      }

      return var5;
   }

   public void setTextAppearance(Context var1, int var2) {
      super.setTextAppearance(var1, var2);
      if (canApplyTextAppearanceLineHeight(var1)) {
         this.applyLineHeightFromViewAppearance(var1.getTheme(), var2);
      }

   }
}
