package com.yalantis.ucrop.util;

import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

public class SelectedStateListDrawable extends StateListDrawable {
   private int mSelectionColor;

   public SelectedStateListDrawable(Drawable var1, int var2) {
      this.mSelectionColor = var2;
      this.addState(new int[]{16842913}, var1);
      this.addState(new int[0], var1);
   }

   public boolean isStateful() {
      return true;
   }

   protected boolean onStateChange(int[] var1) {
      boolean var3 = false;
      int var4 = var1.length;

      for(int var2 = 0; var2 < var4; ++var2) {
         if (var1[var2] == 16842913) {
            var3 = true;
         }
      }

      if (var3) {
         super.setColorFilter(this.mSelectionColor, Mode.SRC_ATOP);
      } else {
         super.clearColorFilter();
      }

      return super.onStateChange(var1);
   }
}
