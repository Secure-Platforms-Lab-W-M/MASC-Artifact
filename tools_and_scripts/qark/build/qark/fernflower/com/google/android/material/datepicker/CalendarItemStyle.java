package com.google.android.material.datepicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build.VERSION;
import android.widget.TextView;
import androidx.core.util.Preconditions;
import androidx.core.view.ViewCompat;
import com.google.android.material.R.styleable;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;

final class CalendarItemStyle {
   private final ColorStateList backgroundColor;
   private final Rect insets;
   private final ShapeAppearanceModel itemShape;
   private final ColorStateList strokeColor;
   private final int strokeWidth;
   private final ColorStateList textColor;

   private CalendarItemStyle(ColorStateList var1, ColorStateList var2, ColorStateList var3, int var4, ShapeAppearanceModel var5, Rect var6) {
      Preconditions.checkArgumentNonnegative(var6.left);
      Preconditions.checkArgumentNonnegative(var6.top);
      Preconditions.checkArgumentNonnegative(var6.right);
      Preconditions.checkArgumentNonnegative(var6.bottom);
      this.insets = var6;
      this.textColor = var2;
      this.backgroundColor = var1;
      this.strokeColor = var3;
      this.strokeWidth = var4;
      this.itemShape = var5;
   }

   static CalendarItemStyle create(Context var0, int var1) {
      boolean var2;
      if (var1 != 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      Preconditions.checkArgument(var2, "Cannot create a CalendarItemStyle with a styleResId of 0");
      TypedArray var3 = var0.obtainStyledAttributes(var1, styleable.MaterialCalendarItem);
      Rect var4 = new Rect(var3.getDimensionPixelOffset(styleable.MaterialCalendarItem_android_insetLeft, 0), var3.getDimensionPixelOffset(styleable.MaterialCalendarItem_android_insetTop, 0), var3.getDimensionPixelOffset(styleable.MaterialCalendarItem_android_insetRight, 0), var3.getDimensionPixelOffset(styleable.MaterialCalendarItem_android_insetBottom, 0));
      ColorStateList var5 = MaterialResources.getColorStateList(var0, var3, styleable.MaterialCalendarItem_itemFillColor);
      ColorStateList var6 = MaterialResources.getColorStateList(var0, var3, styleable.MaterialCalendarItem_itemTextColor);
      ColorStateList var7 = MaterialResources.getColorStateList(var0, var3, styleable.MaterialCalendarItem_itemStrokeColor);
      var1 = var3.getDimensionPixelSize(styleable.MaterialCalendarItem_itemStrokeWidth, 0);
      ShapeAppearanceModel var8 = ShapeAppearanceModel.builder(var0, var3.getResourceId(styleable.MaterialCalendarItem_itemShapeAppearance, 0), var3.getResourceId(styleable.MaterialCalendarItem_itemShapeAppearanceOverlay, 0)).build();
      var3.recycle();
      return new CalendarItemStyle(var5, var6, var7, var1, var8, var4);
   }

   int getBottomInset() {
      return this.insets.bottom;
   }

   int getLeftInset() {
      return this.insets.left;
   }

   int getRightInset() {
      return this.insets.right;
   }

   int getTopInset() {
      return this.insets.top;
   }

   void styleItem(TextView var1) {
      Object var2 = new MaterialShapeDrawable();
      MaterialShapeDrawable var3 = new MaterialShapeDrawable();
      ((MaterialShapeDrawable)var2).setShapeAppearanceModel(this.itemShape);
      var3.setShapeAppearanceModel(this.itemShape);
      ((MaterialShapeDrawable)var2).setFillColor(this.backgroundColor);
      ((MaterialShapeDrawable)var2).setStroke((float)this.strokeWidth, this.strokeColor);
      var1.setTextColor(this.textColor);
      if (VERSION.SDK_INT >= 21) {
         var2 = new RippleDrawable(this.textColor.withAlpha(30), (Drawable)var2, var3);
      }

      ViewCompat.setBackground(var1, new InsetDrawable((Drawable)var2, this.insets.left, this.insets.top, this.insets.right, this.insets.bottom));
   }
}
