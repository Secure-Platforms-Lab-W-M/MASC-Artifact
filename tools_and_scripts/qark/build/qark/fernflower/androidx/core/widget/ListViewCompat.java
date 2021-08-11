package androidx.core.widget;

import android.os.Build.VERSION;
import android.view.View;
import android.widget.ListView;

public final class ListViewCompat {
   private ListViewCompat() {
   }

   public static boolean canScrollList(ListView var0, int var1) {
      if (VERSION.SDK_INT >= 19) {
         return var0.canScrollList(var1);
      } else {
         int var3 = var0.getChildCount();
         boolean var5 = false;
         boolean var4 = false;
         if (var3 == 0) {
            return false;
         } else {
            int var2 = var0.getFirstVisiblePosition();
            if (var1 > 0) {
               var1 = var0.getChildAt(var3 - 1).getBottom();
               if (var2 + var3 < var0.getCount() || var1 > var0.getHeight() - var0.getListPaddingBottom()) {
                  var4 = true;
               }

               return var4;
            } else {
               var1 = var0.getChildAt(0).getTop();
               if (var2 <= 0) {
                  var4 = var5;
                  if (var1 >= var0.getListPaddingTop()) {
                     return var4;
                  }
               }

               var4 = true;
               return var4;
            }
         }
      }
   }

   public static void scrollListBy(ListView var0, int var1) {
      if (VERSION.SDK_INT >= 19) {
         var0.scrollListBy(var1);
      } else {
         int var2 = var0.getFirstVisiblePosition();
         if (var2 != -1) {
            View var3 = var0.getChildAt(0);
            if (var3 != null) {
               var0.setSelectionFromTop(var2, var3.getTop() - var1);
            }
         }
      }
   }
}
