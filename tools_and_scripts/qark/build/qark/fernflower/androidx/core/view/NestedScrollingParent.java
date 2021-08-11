package androidx.core.view;

import android.view.View;

public interface NestedScrollingParent {
   int getNestedScrollAxes();

   boolean onNestedFling(View var1, float var2, float var3, boolean var4);

   boolean onNestedPreFling(View var1, float var2, float var3);

   void onNestedPreScroll(View var1, int var2, int var3, int[] var4);

   void onNestedScroll(View var1, int var2, int var3, int var4, int var5);

   void onNestedScrollAccepted(View var1, View var2, int var3);

   boolean onStartNestedScroll(View var1, View var2, int var3);

   void onStopNestedScroll(View var1);
}
