package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RatingBar;
import androidx.appcompat.R.attr;

public class AppCompatRatingBar extends RatingBar {
   private final AppCompatProgressBarHelper mAppCompatProgressBarHelper;

   public AppCompatRatingBar(Context var1) {
      this(var1, (AttributeSet)null);
   }

   public AppCompatRatingBar(Context var1, AttributeSet var2) {
      this(var1, var2, attr.ratingBarStyle);
   }

   public AppCompatRatingBar(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
      AppCompatProgressBarHelper var4 = new AppCompatProgressBarHelper(this);
      this.mAppCompatProgressBarHelper = var4;
      var4.loadFromAttributes(var2, var3);
   }

   protected void onMeasure(int var1, int var2) {
      synchronized(this){}

      Throwable var10000;
      label75: {
         boolean var10001;
         Bitmap var3;
         try {
            super.onMeasure(var1, var2);
            var3 = this.mAppCompatProgressBarHelper.getSampleTile();
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
