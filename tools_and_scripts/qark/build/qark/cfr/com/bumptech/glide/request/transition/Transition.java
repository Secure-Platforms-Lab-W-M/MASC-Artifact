/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 *  android.view.View
 */
package com.bumptech.glide.request.transition;

import android.graphics.drawable.Drawable;
import android.view.View;

public interface Transition<R> {
    public boolean transition(R var1, ViewAdapter var2);

    public static interface ViewAdapter {
        public Drawable getCurrentDrawable();

        public View getView();

        public void setDrawable(Drawable var1);
    }

}

