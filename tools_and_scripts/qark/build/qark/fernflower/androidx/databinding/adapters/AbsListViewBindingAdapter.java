package androidx.databinding.adapters;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public class AbsListViewBindingAdapter {
   public static void setOnScroll(AbsListView var0, final AbsListViewBindingAdapter.OnScroll var1, final AbsListViewBindingAdapter.OnScrollStateChanged var2) {
      var0.setOnScrollListener(new OnScrollListener() {
         public void onScroll(AbsListView var1x, int var2x, int var3, int var4) {
            AbsListViewBindingAdapter.OnScroll var5 = var1;
            if (var5 != null) {
               var5.onScroll(var1x, var2x, var3, var4);
            }

         }

         public void onScrollStateChanged(AbsListView var1x, int var2x) {
            AbsListViewBindingAdapter.OnScrollStateChanged var3 = var2;
            if (var3 != null) {
               var3.onScrollStateChanged(var1x, var2x);
            }

         }
      });
   }

   public interface OnScroll {
      void onScroll(AbsListView var1, int var2, int var3, int var4);
   }

   public interface OnScrollStateChanged {
      void onScrollStateChanged(AbsListView var1, int var2);
   }
}
