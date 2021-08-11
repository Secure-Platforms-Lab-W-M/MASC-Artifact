package com.google.android.material.textfield;

import android.content.Context;
import com.google.android.material.internal.CheckableImageButton;

abstract class EndIconDelegate {
   Context context;
   CheckableImageButton endIconView;
   TextInputLayout textInputLayout;

   EndIconDelegate(TextInputLayout var1) {
      this.textInputLayout = var1;
      this.context = var1.getContext();
      this.endIconView = var1.getEndIconView();
   }

   abstract void initialize();

   boolean isBoxBackgroundModeSupported(int var1) {
      return true;
   }

   boolean shouldTintIconOnError() {
      return false;
   }
}
