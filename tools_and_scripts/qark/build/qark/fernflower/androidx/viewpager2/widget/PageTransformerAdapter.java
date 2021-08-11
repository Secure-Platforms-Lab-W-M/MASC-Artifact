package androidx.viewpager2.widget;

import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.Locale;

final class PageTransformerAdapter extends ViewPager2.OnPageChangeCallback {
   private final LinearLayoutManager mLayoutManager;
   private ViewPager2.PageTransformer mPageTransformer;

   PageTransformerAdapter(LinearLayoutManager var1) {
      this.mLayoutManager = var1;
   }

   ViewPager2.PageTransformer getPageTransformer() {
      return this.mPageTransformer;
   }

   public void onPageScrollStateChanged(int var1) {
   }

   public void onPageScrolled(int var1, float var2, int var3) {
      if (this.mPageTransformer != null) {
         var2 = -var2;

         for(var3 = 0; var3 < this.mLayoutManager.getChildCount(); ++var3) {
            View var5 = this.mLayoutManager.getChildAt(var3);
            if (var5 == null) {
               throw new IllegalStateException(String.format(Locale.US, "LayoutManager returned a null child at pos %d/%d while transforming pages", var3, this.mLayoutManager.getChildCount()));
            }

            float var4 = (float)(this.mLayoutManager.getPosition(var5) - var1);
            this.mPageTransformer.transformPage(var5, var4 + var2);
         }

      }
   }

   public void onPageSelected(int var1) {
   }

   void setPageTransformer(ViewPager2.PageTransformer var1) {
      this.mPageTransformer = var1;
   }
}
