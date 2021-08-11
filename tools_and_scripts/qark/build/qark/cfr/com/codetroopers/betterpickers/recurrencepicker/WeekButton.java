/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.widget.ToggleButton
 */
package com.codetroopers.betterpickers.recurrencepicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ToggleButton;
import androidx.core.view.ViewCompat;

public class WeekButton
extends ToggleButton {
    private static int mWidth;

    public WeekButton(Context context) {
        super(context);
    }

    public WeekButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public WeekButton(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    public static void setSuggestedWidth(int n) {
        mWidth = n;
    }

    protected void onMeasure(int n, int n2) {
        super.onMeasure(n, n2);
        n = this.getMeasuredHeight();
        n2 = this.getMeasuredWidth();
        int n3 = n;
        int n4 = n2;
        if (n > 0) {
            n3 = n;
            n4 = n2;
            if (n2 > 0) {
                if (n2 < n) {
                    n3 = n;
                    n4 = n2;
                    if (View.MeasureSpec.getMode((int)ViewCompat.getMeasuredHeightAndState((View)this)) != 1073741824) {
                        n3 = n2;
                        n4 = n2;
                    }
                } else {
                    n3 = n;
                    n4 = n2;
                    if (n < n2) {
                        n3 = n;
                        n4 = n2;
                        if (View.MeasureSpec.getMode((int)ViewCompat.getMeasuredWidthAndState((View)this)) != 1073741824) {
                            n4 = n;
                            n3 = n;
                        }
                    }
                }
            }
        }
        this.setMeasuredDimension(n4, n3);
    }
}

