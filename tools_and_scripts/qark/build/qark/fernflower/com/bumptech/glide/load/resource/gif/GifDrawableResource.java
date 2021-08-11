package com.bumptech.glide.load.resource.gif;

import com.bumptech.glide.load.engine.Initializable;
import com.bumptech.glide.load.resource.drawable.DrawableResource;

public class GifDrawableResource extends DrawableResource implements Initializable {
   public GifDrawableResource(GifDrawable var1) {
      super(var1);
   }

   public Class getResourceClass() {
      return GifDrawable.class;
   }

   public int getSize() {
      return ((GifDrawable)this.drawable).getSize();
   }

   public void initialize() {
      ((GifDrawable)this.drawable).getFirstFrame().prepareToDraw();
   }

   public void recycle() {
      ((GifDrawable)this.drawable).stop();
      ((GifDrawable)this.drawable).recycle();
   }
}
