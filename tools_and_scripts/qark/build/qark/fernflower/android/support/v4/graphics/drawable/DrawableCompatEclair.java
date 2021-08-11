package android.support.v4.graphics.drawable;

import android.graphics.drawable.Drawable;

class DrawableCompatEclair {
   public static Drawable wrapForTinting(Drawable var0) {
      return (Drawable)(!(var0 instanceof DrawableWrapperEclair) ? new DrawableWrapperEclair(var0) : var0);
   }
}
