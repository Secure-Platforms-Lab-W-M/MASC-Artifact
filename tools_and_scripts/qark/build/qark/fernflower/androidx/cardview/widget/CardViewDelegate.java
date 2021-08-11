package androidx.cardview.widget;

import android.graphics.drawable.Drawable;
import android.view.View;

interface CardViewDelegate {
   Drawable getCardBackground();

   View getCardView();

   boolean getPreventCornerOverlap();

   boolean getUseCompatPadding();

   void setCardBackground(Drawable var1);

   void setMinWidthHeightInternal(int var1, int var2);

   void setShadowPadding(int var1, int var2, int var3, int var4);
}
