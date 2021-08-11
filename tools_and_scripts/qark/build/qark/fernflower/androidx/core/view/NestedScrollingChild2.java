package androidx.core.view;

public interface NestedScrollingChild2 extends NestedScrollingChild {
   boolean dispatchNestedPreScroll(int var1, int var2, int[] var3, int[] var4, int var5);

   boolean dispatchNestedScroll(int var1, int var2, int var3, int var4, int[] var5, int var6);

   boolean hasNestedScrollingParent(int var1);

   boolean startNestedScroll(int var1, int var2);

   void stopNestedScroll(int var1);
}
