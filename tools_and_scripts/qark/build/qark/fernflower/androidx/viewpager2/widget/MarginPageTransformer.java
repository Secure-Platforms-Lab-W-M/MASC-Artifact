package androidx.viewpager2.widget;

import android.view.View;
import android.view.ViewParent;
import androidx.core.util.Preconditions;
import androidx.recyclerview.widget.RecyclerView;

public final class MarginPageTransformer implements ViewPager2.PageTransformer {
   private final int mMarginPx;

   public MarginPageTransformer(int var1) {
      Preconditions.checkArgumentNonnegative(var1, "Margin must be non-negative");
      this.mMarginPx = var1;
   }

   private ViewPager2 requireViewPager(View var1) {
      ViewParent var3 = var1.getParent();
      ViewParent var2 = var3.getParent();
      if (var3 instanceof RecyclerView && var2 instanceof ViewPager2) {
         return (ViewPager2)var2;
      } else {
         throw new IllegalStateException("Expected the page view to be managed by a ViewPager2 instance.");
      }
   }

   public void transformPage(View var1, float var2) {
      ViewPager2 var3 = this.requireViewPager(var1);
      var2 = (float)this.mMarginPx * var2;
      if (var3.getOrientation() == 0) {
         if (var3.isRtl()) {
            var2 = -var2;
         }

         var1.setTranslationX(var2);
      } else {
         var1.setTranslationY(var2);
      }
   }
}
