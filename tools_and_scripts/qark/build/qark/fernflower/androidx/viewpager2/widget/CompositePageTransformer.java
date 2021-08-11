package androidx.viewpager2.widget;

import android.view.View;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class CompositePageTransformer implements ViewPager2.PageTransformer {
   private final List mTransformers = new ArrayList();

   public void addTransformer(ViewPager2.PageTransformer var1) {
      this.mTransformers.add(var1);
   }

   public void removeTransformer(ViewPager2.PageTransformer var1) {
      this.mTransformers.remove(var1);
   }

   public void transformPage(View var1, float var2) {
      Iterator var3 = this.mTransformers.iterator();

      while(var3.hasNext()) {
         ((ViewPager2.PageTransformer)var3.next()).transformPage(var1, var2);
      }

   }
}
