package com.bumptech.glide.request.target;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class DrawableThumbnailImageViewTarget extends ThumbnailImageViewTarget {
   public DrawableThumbnailImageViewTarget(ImageView var1) {
      super(var1);
   }

   @Deprecated
   public DrawableThumbnailImageViewTarget(ImageView var1, boolean var2) {
      super(var1, var2);
   }

   protected Drawable getDrawable(Drawable var1) {
      return var1;
   }
}
