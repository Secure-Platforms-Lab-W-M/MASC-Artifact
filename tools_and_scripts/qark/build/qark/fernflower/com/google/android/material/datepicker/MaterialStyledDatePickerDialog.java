package com.google.android.material.datepicker;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import com.google.android.material.R.attr;
import com.google.android.material.R.style;
import com.google.android.material.dialog.InsetDialogOnTouchListener;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.material.resources.MaterialAttributes;
import com.google.android.material.shape.MaterialShapeDrawable;

public class MaterialStyledDatePickerDialog extends DatePickerDialog {
   private static final int DEF_STYLE_ATTR = 16843612;
   private static final int DEF_STYLE_RES;
   private final Drawable background;
   private final Rect backgroundInsets;

   static {
      DEF_STYLE_RES = style.MaterialAlertDialog_MaterialComponents_Picker_Date_Spinner;
   }

   public MaterialStyledDatePickerDialog(Context var1) {
      this(var1, 0);
   }

   public MaterialStyledDatePickerDialog(Context var1, int var2) {
      this(var1, var2, (OnDateSetListener)null, -1, -1, -1);
   }

   public MaterialStyledDatePickerDialog(Context var1, int var2, OnDateSetListener var3, int var4, int var5, int var6) {
      super(var1, var2, var3, var4, var5, var6);
      Context var8 = this.getContext();
      var2 = MaterialAttributes.resolveOrThrow(this.getContext(), attr.colorSurface, this.getClass().getCanonicalName());
      MaterialShapeDrawable var7 = new MaterialShapeDrawable(var8, (AttributeSet)null, 16843612, DEF_STYLE_RES);
      if (VERSION.SDK_INT >= 21) {
         var7.setFillColor(ColorStateList.valueOf(var2));
      } else {
         var7.setFillColor(ColorStateList.valueOf(0));
      }

      Rect var9 = MaterialDialogs.getDialogBackgroundInsets(var8, 16843612, DEF_STYLE_RES);
      this.backgroundInsets = var9;
      this.background = MaterialDialogs.insetDrawable(var7, var9);
   }

   public MaterialStyledDatePickerDialog(Context var1, OnDateSetListener var2, int var3, int var4, int var5) {
      this(var1, 0, var2, var3, var4, var5);
   }

   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.getWindow().setBackgroundDrawable(this.background);
      this.getWindow().getDecorView().setOnTouchListener(new InsetDialogOnTouchListener(this, this.backgroundInsets));
   }
}
