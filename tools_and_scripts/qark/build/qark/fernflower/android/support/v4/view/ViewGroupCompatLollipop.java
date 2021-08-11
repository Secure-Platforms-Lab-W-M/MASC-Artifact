package android.support.v4.view;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.view.ViewGroup;

@TargetApi(21)
@RequiresApi(21)
class ViewGroupCompatLollipop {
   public static int getNestedScrollAxes(ViewGroup var0) {
      return var0.getNestedScrollAxes();
   }

   public static boolean isTransitionGroup(ViewGroup var0) {
      return var0.isTransitionGroup();
   }

   public static void setTransitionGroup(ViewGroup var0, boolean var1) {
      var0.setTransitionGroup(var1);
   }
}
