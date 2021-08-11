package com.bumptech.glide.request.transition;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;

public class DrawableCrossFadeTransition implements Transition {
   private final int duration;
   private final boolean isCrossFadeEnabled;

   public DrawableCrossFadeTransition(int var1, boolean var2) {
      this.duration = var1;
      this.isCrossFadeEnabled = var2;
   }

   public boolean transition(Drawable var1, Transition.ViewAdapter var2) {
      Drawable var4 = var2.getCurrentDrawable();
      Object var3 = var4;
      if (var4 == null) {
         var3 = new ColorDrawable(0);
      }

      TransitionDrawable var5 = new TransitionDrawable(new Drawable[]{(Drawable)var3, var1});
      var5.setCrossFadeEnabled(this.isCrossFadeEnabled);
      var5.startTransition(this.duration);
      var2.setDrawable(var5);
      return true;
   }
}
