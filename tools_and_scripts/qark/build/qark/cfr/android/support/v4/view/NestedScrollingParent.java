/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.View
 */
package android.support.v4.view;

import android.support.annotation.NonNull;
import android.view.View;

public interface NestedScrollingParent {
    public int getNestedScrollAxes();

    public boolean onNestedFling(@NonNull View var1, float var2, float var3, boolean var4);

    public boolean onNestedPreFling(@NonNull View var1, float var2, float var3);

    public void onNestedPreScroll(@NonNull View var1, int var2, int var3, @NonNull int[] var4);

    public void onNestedScroll(@NonNull View var1, int var2, int var3, int var4, int var5);

    public void onNestedScrollAccepted(@NonNull View var1, @NonNull View var2, int var3);

    public boolean onStartNestedScroll(@NonNull View var1, @NonNull View var2, int var3);

    public void onStopNestedScroll(@NonNull View var1);
}

