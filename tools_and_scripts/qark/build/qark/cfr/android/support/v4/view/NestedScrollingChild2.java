/*
 * Decompiled with CFR 0_124.
 */
package android.support.v4.view;

import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;

public interface NestedScrollingChild2
extends NestedScrollingChild {
    public boolean dispatchNestedPreScroll(int var1, int var2, @Nullable int[] var3, @Nullable int[] var4, int var5);

    public boolean dispatchNestedScroll(int var1, int var2, int var3, int var4, @Nullable int[] var5, int var6);

    public boolean hasNestedScrollingParent(int var1);

    public boolean startNestedScroll(int var1, int var2);

    public void stopNestedScroll(int var1);
}

