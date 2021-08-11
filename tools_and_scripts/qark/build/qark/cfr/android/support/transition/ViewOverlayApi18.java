/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 *  android.view.View
 *  android.view.ViewOverlay
 */
package android.support.transition;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.transition.ViewOverlayImpl;
import android.view.View;
import android.view.ViewOverlay;

@RequiresApi(value=18)
class ViewOverlayApi18
implements ViewOverlayImpl {
    private final ViewOverlay mViewOverlay;

    ViewOverlayApi18(@NonNull View view) {
        this.mViewOverlay = view.getOverlay();
    }

    @Override
    public void add(@NonNull Drawable drawable2) {
        this.mViewOverlay.add(drawable2);
    }

    @Override
    public void clear() {
        this.mViewOverlay.clear();
    }

    @Override
    public void remove(@NonNull Drawable drawable2) {
        this.mViewOverlay.remove(drawable2);
    }
}

