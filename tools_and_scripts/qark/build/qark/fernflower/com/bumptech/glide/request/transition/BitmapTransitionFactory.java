package com.bumptech.glide.request.transition;

import android.graphics.Bitmap;

public class BitmapTransitionFactory extends BitmapContainerTransitionFactory {
   public BitmapTransitionFactory(TransitionFactory var1) {
      super(var1);
   }

   protected Bitmap getBitmap(Bitmap var1) {
      return var1;
   }
}
