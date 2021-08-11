package com.bumptech.glide.request.transition;

import android.graphics.drawable.Drawable;
import android.view.View;

public interface Transition {
   boolean transition(Object var1, Transition.ViewAdapter var2);

   public interface ViewAdapter {
      Drawable getCurrentDrawable();

      View getView();

      void setDrawable(Drawable var1);
   }
}
