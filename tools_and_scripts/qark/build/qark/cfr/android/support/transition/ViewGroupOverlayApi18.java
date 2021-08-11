/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroupOverlay
 */
package android.support.transition;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.transition.ViewGroupOverlayImpl;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;

@RequiresApi(value=18)
class ViewGroupOverlayApi18
implements ViewGroupOverlayImpl {
    private final ViewGroupOverlay mViewGroupOverlay;

    ViewGroupOverlayApi18(@NonNull ViewGroup viewGroup) {
        this.mViewGroupOverlay = viewGroup.getOverlay();
    }

    @Override
    public void add(@NonNull Drawable drawable2) {
        this.mViewGroupOverlay.add(drawable2);
    }

    @Override
    public void add(@NonNull View view) {
        this.mViewGroupOverlay.add(view);
    }

    @Override
    public void clear() {
        this.mViewGroupOverlay.clear();
    }

    @Override
    public void remove(@NonNull Drawable drawable2) {
        this.mViewGroupOverlay.remove(drawable2);
    }

    @Override
    public void remove(@NonNull View view) {
        this.mViewGroupOverlay.remove(view);
    }
}

