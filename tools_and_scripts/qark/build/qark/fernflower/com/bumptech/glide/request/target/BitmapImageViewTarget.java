package com.bumptech.glide.request.target;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class BitmapImageViewTarget extends ImageViewTarget {
   public BitmapImageViewTarget(ImageView var1) {
      super(var1);
   }

   @Deprecated
   public BitmapImageViewTarget(ImageView var1, boolean var2) {
      super(var1, var2);
   }

   protected void setResource(Bitmap var1) {
      ((ImageView)this.view).setImageBitmap(var1);
   }
}
