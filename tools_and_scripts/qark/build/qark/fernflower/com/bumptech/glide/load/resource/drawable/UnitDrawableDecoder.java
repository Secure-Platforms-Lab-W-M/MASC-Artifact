package com.bumptech.glide.load.resource.drawable;

import android.graphics.drawable.Drawable;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;

public class UnitDrawableDecoder implements ResourceDecoder {
   public Resource decode(Drawable var1, int var2, int var3, Options var4) {
      return NonOwnedDrawableResource.newInstance(var1);
   }

   public boolean handles(Drawable var1, Options var2) {
      return true;
   }
}
