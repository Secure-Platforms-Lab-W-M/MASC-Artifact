/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 *  android.view.View
 */
package androidx.cardview.widget;

import android.graphics.drawable.Drawable;
import android.view.View;

interface CardViewDelegate {
    public Drawable getCardBackground();

    public View getCardView();

    public boolean getPreventCornerOverlap();

    public boolean getUseCompatPadding();

    public void setCardBackground(Drawable var1);

    public void setMinWidthHeightInternal(int var1, int var2);

    public void setShadowPadding(int var1, int var2, int var3, int var4);
}

