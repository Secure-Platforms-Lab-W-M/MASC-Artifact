/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.ViewGroup
 */
package android.support.transition;

import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.transition.ViewGroupOverlayImpl;
import android.view.ViewGroup;

@RequiresApi(value=14)
interface ViewGroupUtilsImpl {
    public ViewGroupOverlayImpl getOverlay(@NonNull ViewGroup var1);

    public void suppressLayout(@NonNull ViewGroup var1, boolean var2);
}

