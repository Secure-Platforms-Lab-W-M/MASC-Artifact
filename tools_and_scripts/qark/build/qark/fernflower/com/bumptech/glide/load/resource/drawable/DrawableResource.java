package com.bumptech.glide.load.resource.drawable;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import com.bumptech.glide.load.engine.Initializable;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.util.Preconditions;

public abstract class DrawableResource implements Resource, Initializable {
   protected final Drawable drawable;

   public DrawableResource(Drawable var1) {
      this.drawable = (Drawable)Preconditions.checkNotNull(var1);
   }

   public final Drawable get() {
      ConstantState var1 = this.drawable.getConstantState();
      return var1 == null ? this.drawable : var1.newDrawable();
   }

   public void initialize() {
      Drawable var1 = this.drawable;
      if (var1 instanceof BitmapDrawable) {
         ((BitmapDrawable)var1).getBitmap().prepareToDraw();
      } else {
         if (var1 instanceof GifDrawable) {
            ((GifDrawable)var1).getFirstFrame().prepareToDraw();
         }

      }
   }
}
