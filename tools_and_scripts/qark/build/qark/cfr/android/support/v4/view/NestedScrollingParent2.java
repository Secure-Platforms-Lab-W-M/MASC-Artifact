/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.View
 */
package android.support.v4.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.view.View;

public interface NestedScrollingParent2
extends NestedScrollingParent {
    public void onNestedPreScroll(@NonNull View var1, int var2, int var3, @Nullable int[] var4, int var5);

    public void onNestedScroll(@NonNull View var1, int var2, int var3, int var4, int var5, int var6);

    public void onNestedScrollAccepted(@NonNull View var1, @NonNull View var2, int var3, int var4);

    public boolean onStartNestedScroll(@NonNull View var1, @NonNull View var2, int var3, int var4);

    public void onStopNestedScroll(@NonNull View var1, int var2);
}

