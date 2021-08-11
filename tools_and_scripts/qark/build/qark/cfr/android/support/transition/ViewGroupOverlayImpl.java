/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.View
 */
package android.support.transition;

import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.transition.ViewOverlayImpl;
import android.view.View;

@RequiresApi(value=14)
interface ViewGroupOverlayImpl
extends ViewOverlayImpl {
    public void add(@NonNull View var1);

    public void remove(@NonNull View var1);
}

