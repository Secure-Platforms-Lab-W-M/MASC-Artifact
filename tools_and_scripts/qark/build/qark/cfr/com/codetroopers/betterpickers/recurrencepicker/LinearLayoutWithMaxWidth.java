/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.widget.LinearLayout
 */
package com.codetroopers.betterpickers.recurrencepicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.codetroopers.betterpickers.recurrencepicker.WeekButton;

public class LinearLayoutWithMaxWidth
extends LinearLayout {
    public LinearLayoutWithMaxWidth(Context context) {
        super(context);
    }

    public LinearLayoutWithMaxWidth(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public LinearLayoutWithMaxWidth(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    protected void onMeasure(int n, int n2) {
        WeekButton.setSuggestedWidth(View.MeasureSpec.getSize((int)n) / 7);
        super.onMeasure(n, n2);
    }
}

