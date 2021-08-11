package com.bumptech.glide.request.target;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class BitmapThumbnailImageViewTarget extends ThumbnailImageViewTarget {
   public BitmapThumbnailImageViewTarget(ImageView var1) {
      super(var1);
   }

   @Deprecated
   public BitmapThumbnailImageViewTarget(ImageView var1, boolean var2) {
      super(var1, var2);
   }

   protected Drawable getDrawable(Bitmap var1) {
      return new BitmapDrawable(((ImageView)this.view).getResources(), var1);
   }
}
