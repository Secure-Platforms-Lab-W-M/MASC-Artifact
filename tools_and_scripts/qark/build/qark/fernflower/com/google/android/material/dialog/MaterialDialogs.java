package com.google.android.material.dialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import com.google.android.material.R.dimen;
import com.google.android.material.R.styleable;
import com.google.android.material.internal.ThemeEnforcement;

public class MaterialDialogs {
   private MaterialDialogs() {
   }

   public static Rect getDialogBackgroundInsets(Context var0, int var1, int var2) {
      TypedArray var9 = ThemeEnforcement.obtainStyledAttributes(var0, (AttributeSet)null, styleable.MaterialAlertDialog, var1, var2);
      var1 = var9.getDimensionPixelSize(styleable.MaterialAlertDialog_backgroundInsetStart, var0.getResources().getDimensionPixelSize(dimen.mtrl_alert_dialog_background_inset_start));
      int var7 = var9.getDimensionPixelSize(styleable.MaterialAlertDialog_backgroundInsetTop, var0.getResources().getDimensionPixelSize(dimen.mtrl_alert_dialog_background_inset_top));
      int var3 = var9.getDimensionPixelSize(styleable.MaterialAlertDialog_backgroundInsetEnd, var0.getResources().getDimensionPixelSize(dimen.mtrl_alert_dialog_background_inset_end));
      int var8 = var9.getDimensionPixelSize(styleable.MaterialAlertDialog_backgroundInsetBottom, var0.getResources().getDimensionPixelSize(dimen.mtrl_alert_dialog_background_inset_bottom));
      var9.recycle();
      int var6 = var1;
      var2 = var3;
      if (VERSION.SDK_INT >= 17) {
         var6 = var1;
         var2 = var3;
         if (var0.getResources().getConfiguration().getLayoutDirection() == 1) {
            var2 = var1;
            var6 = var3;
         }
      }

      return new Rect(var6, var7, var2, var8);
   }

   public static InsetDrawable insetDrawable(Drawable var0, Rect var1) {
      return new InsetDrawable(var0, var1.left, var1.top, var1.right, var1.bottom);
   }
}
