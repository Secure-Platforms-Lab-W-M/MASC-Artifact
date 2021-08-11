/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.content.Context
 *  android.content.res.Resources
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.widget.CheckBox
 */
package dnsfilter.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.CheckBox;

public class PaddedCheckBox
extends CheckBox {
    private static int dpAsPx_10;
    private static int dpAsPx_32;

    static {
        dpAsPx_32 = 0;
        dpAsPx_10 = 0;
    }

    public PaddedCheckBox(Context context) {
        super(context);
        this.doPadding();
    }

    public PaddedCheckBox(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.doPadding();
    }

    public PaddedCheckBox(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.doPadding();
    }

    @SuppressLint(value={"NewApi"})
    public PaddedCheckBox(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n, n2);
        this.doPadding();
    }

    private int convertDpToPx(int n) {
        float f = this.getResources().getDisplayMetrics().density;
        return (int)((float)n * f + 0.5f);
    }

    private void doPadding() {
        if (dpAsPx_32 == 0) {
            dpAsPx_32 = this.convertDpToPx(32);
            dpAsPx_10 = this.convertDpToPx(10);
        }
        if (Build.VERSION.SDK_INT >= 17) {
            this.setPadding(dpAsPx_10, dpAsPx_10, dpAsPx_10, dpAsPx_10);
            return;
        }
        this.setPadding(dpAsPx_32, 0, 0, 0);
    }
}

