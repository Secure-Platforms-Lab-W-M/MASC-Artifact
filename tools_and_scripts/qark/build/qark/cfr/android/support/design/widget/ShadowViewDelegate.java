/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 */
package android.support.design.widget;

import android.graphics.drawable.Drawable;

interface ShadowViewDelegate {
    public float getRadius();

    public boolean isCompatPaddingEnabled();

    public void setBackgroundDrawable(Drawable var1);

    public void setShadowPadding(int var1, int var2, int var3, int var4);
}

