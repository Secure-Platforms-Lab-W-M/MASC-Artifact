/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.widget.ImageButton
 */
package android.support.design.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;

class VisibilityAwareImageButton
extends ImageButton {
    private int mUserSetVisibility;

    public VisibilityAwareImageButton(Context context) {
        this(context, null);
    }

    public VisibilityAwareImageButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public VisibilityAwareImageButton(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.mUserSetVisibility = this.getVisibility();
    }

    final int getUserSetVisibility() {
        return this.mUserSetVisibility;
    }

    final void internalSetVisibility(int n, boolean bl) {
        super.setVisibility(n);
        if (bl) {
            this.mUserSetVisibility = n;
            return;
        }
    }

    public void setVisibility(int n) {
        this.internalSetVisibility(n, true);
    }
}

