package android.support.v7.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.appcompat.R$attr;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RatingBar;

public class AppCompatRatingBar extends RatingBar {
   private final AppCompatProgressBarHelper mAppCompatProgressBarHelper;

   public AppCompatRatingBar(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public AppCompatRatingBar(Context var1, AttributeSet var2) {
      this(var1, var2, R$attr.ratingBarStyle);
   }

   public AppCompatRatingBar(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      this.mAppCompatProgressBarHelper = new AppCompatProgressBarHelper(this);
      this.mAppCompatProgressBarHelper.loadFromAttributes(var2, var3);
   }

   protected void onMeasure(int var1, int var2) {
      synchronized(this){}

      Throwable var10000;
      label75: {
         boolean var10001;
         Bitmap var3;
         try {
            super.onMeasure(var1, var2);
            var3 = this.mAppCompatProgressBarHelper.getSampleTime();
         } catch (Throwable var9) {
            var10000 = var9;
            var10001 = false;
            break label75;
         }

         if (var3 == null) {
            return;
         }

         label66:
         try {
            this.setMeasuredDimension(View.resolveSizeAndState(var3.getWidth() * this.getNumStars(), var1, 0), this.getMeasuredHeight());
            return;
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label66;
         }
      }

      Throwable var10 = var10000;
      throw var10;
   }
}
