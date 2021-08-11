/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.View
 *  android.widget.LinearLayout
 */
package com.codetroopers.betterpickers.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public abstract class PickerLinearLayout
extends LinearLayout {
    public PickerLinearLayout(Context context) {
        super(context);
    }

    public PickerLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public abstract View getViewAt(int var1);
}

