/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.widget.HorizontalScrollView
 */
package com.codetroopers.betterpickers.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class AutoScrollHorizontalScrollView
extends HorizontalScrollView {
    public AutoScrollHorizontalScrollView(Context context) {
        super(context);
    }

    public AutoScrollHorizontalScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public AutoScrollHorizontalScrollView(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        super.onLayout(bl, n, n2, n3, n4);
        this.fullScroll(66);
    }
}

