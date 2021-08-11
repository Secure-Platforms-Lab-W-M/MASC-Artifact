package com.google.android.material.textfield;

import android.graphics.drawable.Drawable;
import android.view.View.OnClickListener;

class NoEndIconDelegate extends EndIconDelegate {
   NoEndIconDelegate(TextInputLayout var1) {
      super(var1);
   }

   void initialize() {
      this.textInputLayout.setEndIconOnClickListener((OnClickListener)null);
      this.textInputLayout.setEndIconDrawable((Drawable)null);
      this.textInputLayout.setEndIconContentDescription((CharSequence)null);
   }
}
