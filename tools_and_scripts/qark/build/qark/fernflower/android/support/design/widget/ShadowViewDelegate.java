package android.support.design.widget;

import android.graphics.drawable.Drawable;

interface ShadowViewDelegate {
   float getRadius();

   boolean isCompatPaddingEnabled();

   void setBackgroundDrawable(Drawable var1);

   void setShadowPadding(int var1, int var2, int var3, int var4);
}
