package com.google.android.material.resources;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.TypedValue;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.TintTypedArray;

public class MaterialResources {
   private MaterialResources() {
   }

   public static ColorStateList getColorStateList(Context var0, TypedArray var1, int var2) {
      int var3;
      if (var1.hasValue(var2)) {
         var3 = var1.getResourceId(var2, 0);
         if (var3 != 0) {
            ColorStateList var4 = AppCompatResources.getColorStateList(var0, var3);
            if (var4 != null) {
               return var4;
            }
         }
      }

      if (VERSION.SDK_INT <= 15) {
         var3 = var1.getColor(var2, -1);
         if (var3 != -1) {
            return ColorStateList.valueOf(var3);
         }
      }

      return var1.getColorStateList(var2);
   }

   public static ColorStateList getColorStateList(Context var0, TintTypedArray var1, int var2) {
      int var3;
      if (var1.hasValue(var2)) {
         var3 = var1.getResourceId(var2, 0);
         if (var3 != 0) {
            ColorStateList var4 = AppCompatResources.getColorStateList(var0, var3);
            if (var4 != null) {
               return var4;
            }
         }
      }

      if (VERSION.SDK_INT <= 15) {
         var3 = var1.getColor(var2, -1);
         if (var3 != -1) {
            return ColorStateList.valueOf(var3);
         }
      }

      return var1.getColorStateList(var2);
   }

   public static int getDimensionPixelSize(Context var0, TypedArray var1, int var2, int var3) {
      TypedValue var4 = new TypedValue();
      if (var1.getValue(var2, var4) && var4.type == 2) {
         TypedArray var5 = var0.getTheme().obtainStyledAttributes(new int[]{var4.data});
         var2 = var5.getDimensionPixelSize(0, var3);
         var5.recycle();
         return var2;
      } else {
         return var1.getDimensionPixelSize(var2, var3);
      }
   }

   public static Drawable getDrawable(Context var0, TypedArray var1, int var2) {
      if (var1.hasValue(var2)) {
         int var3 = var1.getResourceId(var2, 0);
         if (var3 != 0) {
            Drawable var4 = AppCompatResources.getDrawable(var0, var3);
            if (var4 != null) {
               return var4;
            }
         }
      }

      return var1.getDrawable(var2);
   }

   static int getIndexWithValue(TypedArray var0, int var1, int var2) {
      return var0.hasValue(var1) ? var1 : var2;
   }

   public static TextAppearance getTextAppearance(Context var0, TypedArray var1, int var2) {
      if (var1.hasValue(var2)) {
         var2 = var1.getResourceId(var2, 0);
         if (var2 != 0) {
            return new TextAppearance(var0, var2);
         }
      }

      return null;
   }
}
