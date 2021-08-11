package com.google.android.material.appbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import com.google.android.material.R.attr;
import com.google.android.material.R.style;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.MaterialShapeUtils;

public class MaterialToolbar extends Toolbar {
   private static final int DEF_STYLE_RES;

   static {
      DEF_STYLE_RES = style.Widget_MaterialComponents_Toolbar;
   }

   public MaterialToolbar(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public MaterialToolbar(Context var1, AttributeSet var2) {
      this(var1, var2, attr.toolbarStyle);
   }

   public MaterialToolbar(Context var1, AttributeSet var2, int var3) {
      super(ThemeEnforcement.createThemedContext(var1, var2, var3, DEF_STYLE_RES), var2, var3);
      this.initBackground(this.getContext());
   }

   private void initBackground(Context var1) {
      Drawable var3 = this.getBackground();
      if (var3 == null || var3 instanceof ColorDrawable) {
         MaterialShapeDrawable var4 = new MaterialShapeDrawable();
         int var2;
         if (var3 != null) {
            var2 = ((ColorDrawable)var3).getColor();
         } else {
            var2 = 0;
         }

         var4.setFillColor(ColorStateList.valueOf(var2));
         var4.initializeElevationOverlay(var1);
         var4.setElevation(ViewCompat.getElevation(this));
         ViewCompat.setBackground(this, var4);
      }
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      MaterialShapeUtils.setParentAbsoluteElevation(this);
   }

   public void setElevation(float var1) {
      super.setElevation(var1);
      MaterialShapeUtils.setElevation(this, var1);
   }
}
