package android.support.v7.widget;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.RequiresApi;

@RequiresApi(9)
class ActionBarBackgroundDrawable extends Drawable {
   final ActionBarContainer mContainer;

   public ActionBarBackgroundDrawable(ActionBarContainer var1) {
      this.mContainer = var1;
   }

   public void draw(Canvas var1) {
      if (this.mContainer.mIsSplit) {
         if (this.mContainer.mSplitBackground != null) {
            this.mContainer.mSplitBackground.draw(var1);
            return;
         }
      } else {
         if (this.mContainer.mBackground != null) {
            this.mContainer.mBackground.draw(var1);
         }

         if (this.mContainer.mStackedBackground != null && this.mContainer.mIsStacked) {
            this.mContainer.mStackedBackground.draw(var1);
         }
      }

   }

   public int getOpacity() {
      return 0;
   }

   public void setAlpha(int var1) {
   }

   public void setColorFilter(ColorFilter var1) {
   }
}
