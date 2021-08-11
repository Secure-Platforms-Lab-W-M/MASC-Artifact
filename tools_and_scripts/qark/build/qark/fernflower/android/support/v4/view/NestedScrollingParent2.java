package android.support.v4.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

public interface NestedScrollingParent2 extends NestedScrollingParent {
   void onNestedPreScroll(@NonNull View var1, int var2, int var3, @Nullable int[] var4, int var5);

   void onNestedScroll(@NonNull View var1, int var2, int var3, int var4, int var5, int var6);

   void onNestedScrollAccepted(@NonNull View var1, @NonNull View var2, int var3, int var4);

   boolean onStartNestedScroll(@NonNull View var1, @NonNull View var2, int var3, int var4);

   void onStopNestedScroll(@NonNull View var1, int var2);
}
