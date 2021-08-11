package androidx.core.view;

import android.view.View;
import android.view.ViewGroup;

public class NestedScrollingParentHelper {
   private int mNestedScrollAxesNonTouch;
   private int mNestedScrollAxesTouch;

   public NestedScrollingParentHelper(ViewGroup var1) {
   }

   public int getNestedScrollAxes() {
      return this.mNestedScrollAxesTouch | this.mNestedScrollAxesNonTouch;
   }

   public void onNestedScrollAccepted(View var1, View var2, int var3) {
      this.onNestedScrollAccepted(var1, var2, var3, 0);
   }

   public void onNestedScrollAccepted(View var1, View var2, int var3, int var4) {
      if (var4 == 1) {
         this.mNestedScrollAxesNonTouch = var3;
      } else {
         this.mNestedScrollAxesTouch = var3;
      }
   }

   public void onStopNestedScroll(View var1) {
      this.onStopNestedScroll(var1, 0);
   }

   public void onStopNestedScroll(View var1, int var2) {
      if (var2 == 1) {
         this.mNestedScrollAxesNonTouch = 0;
      } else {
         this.mNestedScrollAxesTouch = 0;
      }
   }
}
