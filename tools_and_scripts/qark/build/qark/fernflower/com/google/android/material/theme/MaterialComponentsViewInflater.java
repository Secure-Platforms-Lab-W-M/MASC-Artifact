package com.google.android.material.theme;

import android.content.Context;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import androidx.appcompat.app.AppCompatViewInflater;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textview.MaterialTextView;

public class MaterialComponentsViewInflater extends AppCompatViewInflater {
   private static int floatingToolbarItemBackgroundResId = -1;

   protected AppCompatButton createButton(Context var1, AttributeSet var2) {
      return (AppCompatButton)(this.shouldInflateAppCompatButton(var1, var2) ? new AppCompatButton(var1, var2) : new MaterialButton(var1, var2));
   }

   protected AppCompatCheckBox createCheckBox(Context var1, AttributeSet var2) {
      return new MaterialCheckBox(var1, var2);
   }

   protected AppCompatRadioButton createRadioButton(Context var1, AttributeSet var2) {
      return new MaterialRadioButton(var1, var2);
   }

   protected AppCompatTextView createTextView(Context var1, AttributeSet var2) {
      return new MaterialTextView(var1, var2);
   }

   protected boolean shouldInflateAppCompatButton(Context var1, AttributeSet var2) {
      if (VERSION.SDK_INT != 23 && VERSION.SDK_INT != 24 && VERSION.SDK_INT != 25) {
         return false;
      } else {
         if (floatingToolbarItemBackgroundResId == -1) {
            floatingToolbarItemBackgroundResId = var1.getResources().getIdentifier("floatingToolbarItemBackgroundDrawable", "^attr-private", "android");
         }

         int var3 = floatingToolbarItemBackgroundResId;
         if (var3 != 0 && var3 != -1) {
            for(var3 = 0; var3 < var2.getAttributeCount(); ++var3) {
               if (var2.getAttributeNameResource(var3) == 16842964) {
                  int var4 = var2.getAttributeListValue(var3, (String[])null, 0);
                  if (floatingToolbarItemBackgroundResId == var4) {
                     return true;
                  }
               }
            }
         }

         return false;
      }
   }
}
