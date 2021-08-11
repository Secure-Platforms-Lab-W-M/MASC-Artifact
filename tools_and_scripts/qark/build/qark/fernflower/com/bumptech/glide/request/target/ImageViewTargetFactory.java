package com.bumptech.glide.request.target;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class ImageViewTargetFactory {
   public ViewTarget buildTarget(ImageView var1, Class var2) {
      if (Bitmap.class.equals(var2)) {
         return new BitmapImageViewTarget(var1);
      } else if (Drawable.class.isAssignableFrom(var2)) {
         return new DrawableImageViewTarget(var1);
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Unhandled class: ");
         var3.append(var2);
         var3.append(", try .as*(Class).transcode(ResourceTranscoder)");
         throw new IllegalArgumentException(var3.toString());
      }
   }
}
