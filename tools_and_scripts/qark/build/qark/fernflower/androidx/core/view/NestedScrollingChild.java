package androidx.core.view;

public interface NestedScrollingChild {
   boolean dispatchNestedFling(float var1, float var2, boolean var3);

   boolean dispatchNestedPreFling(float var1, float var2);

   boolean dispatchNestedPreScroll(int var1, int var2, int[] var3, int[] var4);

   boolean dispatchNestedScroll(int var1, int var2, int var3, int var4, int[] var5);

   boolean hasNestedScrollingParent();

   boolean isNestedScrollingEnabled();

   void setNestedScrollingEnabled(boolean var1);

   boolean startNestedScroll(int var1);

   void stopNestedScroll();
}
