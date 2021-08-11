package com.google.android.material.internal;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class VisibilityAwareImageButton extends ImageButton {
   private int userSetVisibility;

   public VisibilityAwareImageButton(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public VisibilityAwareImageButton(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public VisibilityAwareImageButton(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.userSetVisibility = this.getVisibility();
   }

   public final int getUserSetVisibility() {
      return this.userSetVisibility;
   }

   public final void internalSetVisibility(int var1, boolean var2) {
      super.setVisibility(var1);
      if (var2) {
         this.userSetVisibility = var1;
      }

   }

   public void setVisibility(int var1) {
      this.internalSetVisibility(var1, true);
   }
}
