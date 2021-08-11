/*
 * Decompiled with CFR 0_124.
 */
package androidx.core.view;

public interface NestedScrollingChild {
    public boolean dispatchNestedFling(float var1, float var2, boolean var3);

    public boolean dispatchNestedPreFling(float var1, float var2);

    public boolean dispatchNestedPreScroll(int var1, int var2, int[] var3, int[] var4);

    public boolean dispatchNestedScroll(int var1, int var2, int var3, int var4, int[] var5);

    public boolean hasNestedScrollingParent();

    public boolean isNestedScrollingEnabled();

    public void setNestedScrollingEnabled(boolean var1);

    public boolean startNestedScroll(int var1);

    public void stopNestedScroll();
}

