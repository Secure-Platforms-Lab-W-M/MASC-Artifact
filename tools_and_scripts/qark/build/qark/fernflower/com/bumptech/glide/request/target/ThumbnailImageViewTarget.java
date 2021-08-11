package com.bumptech.glide.request.target;

import android.graphics.drawable.Drawable;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public abstract class ThumbnailImageViewTarget extends ImageViewTarget {
   public ThumbnailImageViewTarget(ImageView var1) {
      super(var1);
   }

   @Deprecated
   public ThumbnailImageViewTarget(ImageView var1, boolean var2) {
      super(var1, var2);
   }

   protected abstract Drawable getDrawable(Object var1);

   protected void setResource(Object var1) {
      LayoutParams var3 = ((ImageView)this.view).getLayoutParams();
      Drawable var2 = this.getDrawable(var1);
      var1 = var2;
      if (var3 != null) {
         var1 = var2;
         if (var3.width > 0) {
            var1 = var2;
            if (var3.height > 0) {
               var1 = new FixedSizeDrawable(var2, var3.width, var3.height);
            }
         }
      }

      ((ImageView)this.view).setImageDrawable((Drawable)var1);
   }
}
