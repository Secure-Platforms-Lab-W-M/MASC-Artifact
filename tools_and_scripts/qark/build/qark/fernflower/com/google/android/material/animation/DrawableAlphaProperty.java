package com.google.android.material.animation;

import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.Property;
import java.util.WeakHashMap;

public class DrawableAlphaProperty extends Property {
   public static final Property DRAWABLE_ALPHA_COMPAT = new DrawableAlphaProperty();
   private final WeakHashMap alphaCache = new WeakHashMap();

   private DrawableAlphaProperty() {
      super(Integer.class, "drawableAlphaCompat");
   }

   public Integer get(Drawable var1) {
      if (VERSION.SDK_INT >= 19) {
         return var1.getAlpha();
      } else {
         return this.alphaCache.containsKey(var1) ? (Integer)this.alphaCache.get(var1) : 255;
      }
   }

   public void set(Drawable var1, Integer var2) {
      if (VERSION.SDK_INT < 19) {
         this.alphaCache.put(var1, var2);
      }

      var1.setAlpha(var2);
   }
}
