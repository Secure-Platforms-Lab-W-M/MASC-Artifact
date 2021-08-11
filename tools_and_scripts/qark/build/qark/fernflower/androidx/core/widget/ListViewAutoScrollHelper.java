package androidx.core.widget;

import android.widget.ListView;

public class ListViewAutoScrollHelper extends AutoScrollHelper {
   private final ListView mTarget;

   public ListViewAutoScrollHelper(ListView var1) {
      super(var1);
      this.mTarget = var1;
   }

   public boolean canTargetScrollHorizontally(int var1) {
      return false;
   }

   public boolean canTargetScrollVertically(int var1) {
      ListView var5 = this.mTarget;
      int var2 = var5.getCount();
      if (var2 == 0) {
         return false;
      } else {
         int var3 = var5.getChildCount();
         int var4 = var5.getFirstVisiblePosition();
         if (var1 > 0) {
            if (var4 + var3 >= var2 && var5.getChildAt(var3 - 1).getBottom() <= var5.getHeight()) {
               return false;
            }
         } else {
            if (var1 >= 0) {
               return false;
            }

            if (var4 <= 0 && var5.getChildAt(0).getTop() >= 0) {
               return false;
            }
         }

         return true;
      }
   }

   public void scrollTargetBy(int var1, int var2) {
      ListViewCompat.scrollListBy(this.mTarget, var2);
   }
}
