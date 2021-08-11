package com.google.android.material.textfield;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import androidx.appcompat.widget.AppCompatEditText;
import com.google.android.material.R.attr;

public class TextInputEditText extends AppCompatEditText {
   public TextInputEditText(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public TextInputEditText(Context var1, AttributeSet var2) {
      this(var1, var2, attr.editTextStyle);
   }

   public TextInputEditText(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }

   private CharSequence getHintFromLayout() {
      TextInputLayout var1 = this.getTextInputLayout();
      return var1 != null ? var1.getHint() : null;
   }

   private TextInputLayout getTextInputLayout() {
      for(ViewParent var1 = this.getParent(); var1 instanceof View; var1 = var1.getParent()) {
         if (var1 instanceof TextInputLayout) {
            return (TextInputLayout)var1;
         }
      }

      return null;
   }

   public CharSequence getHint() {
      TextInputLayout var1 = this.getTextInputLayout();
      return var1 != null && var1.isProvidingHint() ? var1.getHint() : super.getHint();
   }

   protected void onAttachedToWindow() {
      super.onAttachedToWindow();
      TextInputLayout var1 = this.getTextInputLayout();
      if (var1 != null && var1.isProvidingHint() && super.getHint() == null && Build.MANUFACTURER.equalsIgnoreCase("Meizu")) {
         this.setHint("");
      }

   }

   public InputConnection onCreateInputConnection(EditorInfo var1) {
      InputConnection var2 = super.onCreateInputConnection(var1);
      if (var2 != null && var1.hintText == null) {
         var1.hintText = this.getHintFromLayout();
      }

      return var2;
   }
}
