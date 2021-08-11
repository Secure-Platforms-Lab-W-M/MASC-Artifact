/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 */
package android.support.transition;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

@RequiresApi(value=14)
interface ViewOverlayImpl {
    public void add(@NonNull Drawable var1);

    public void clear();

    public void remove(@NonNull Drawable var1);
}

