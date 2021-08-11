package com.google.android.material.textfield;

import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

class CustomEndIconDelegate extends EndIconDelegate {
   CustomEndIconDelegate(TextInputLayout var1) {
      super(var1);
   }

   void initialize() {
      this.textInputLayout.setEndIconOnClickListener((OnClickListener)null);
      this.textInputLayout.setEndIconOnLongClickListener((OnLongClickListener)null);
   }
}
