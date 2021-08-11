package androidx.core.view;

import android.view.View;

public interface NestedScrollingParent2 extends NestedScrollingParent {
   void onNestedPreScroll(View var1, int var2, int var3, int[] var4, int var5);

   void onNestedScroll(View var1, int var2, int var3, int var4, int var5, int var6);

   void onNestedScrollAccepted(View var1, View var2, int var3, int var4);

   boolean onStartNestedScroll(View var1, View var2, int var3, int var4);

   void onStopNestedScroll(View var1, int var2);
}
