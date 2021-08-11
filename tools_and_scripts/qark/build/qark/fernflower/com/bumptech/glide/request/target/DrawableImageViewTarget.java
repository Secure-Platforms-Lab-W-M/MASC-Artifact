package com.bumptech.glide.request.target;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class DrawableImageViewTarget extends ImageViewTarget {
   public DrawableImageViewTarget(ImageView var1) {
      super(var1);
   }

   @Deprecated
   public DrawableImageViewTarget(ImageView var1, boolean var2) {
      super(var1, var2);
   }

   protected void setResource(Drawable var1) {
      ((ImageView)this.view).setImageDrawable(var1);
   }
}
