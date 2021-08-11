/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Typeface
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.text.TextPaint
 *  android.util.AttributeSet
 *  android.widget.TextView
 *  com.codetroopers.betterpickers.R
 *  com.codetroopers.betterpickers.R$string
 */
package com.codetroopers.betterpickers.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;
import com.codetroopers.betterpickers.R;

public class ZeroTopPaddingTextView
extends TextView {
    private static final float BOLD_FONT_BOTTOM_PADDING_RATIO = 0.208f;
    private static final float BOLD_FONT_PADDING_RATIO = 0.208f;
    private static final float NORMAL_FONT_BOTTOM_PADDING_RATIO = 0.25f;
    private static final float NORMAL_FONT_PADDING_RATIO = 0.328f;
    private static final float PRE_ICS_BOTTOM_PADDING_RATIO = 0.233f;
    private static final Typeface SAN_SERIF_BOLD = Typeface.create((String)"san-serif", (int)1);
    private static final Typeface SAN_SERIF_CONDENSED_BOLD = Typeface.create((String)"sans-serif-condensed", (int)1);
    private String decimalSeperator = "";
    private int mPaddingRight = 0;
    private String timeSeperator = "";

    public ZeroTopPaddingTextView(Context context) {
        this(context, null);
    }

    public ZeroTopPaddingTextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ZeroTopPaddingTextView(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.init();
        this.setIncludeFontPadding(false);
        this.updatePadding();
    }

    private void init() {
        this.decimalSeperator = this.getResources().getString(R.string.number_picker_seperator);
        this.timeSeperator = this.getResources().getString(R.string.time_picker_time_seperator);
    }

    public void setPaddingRight(int n) {
        this.mPaddingRight = n;
        this.updatePadding();
    }

    public void updatePadding() {
        float f;
        float f2;
        block9 : {
            block10 : {
                f2 = 0.328f;
                float f3 = 0.25f;
                f = f2;
                float f4 = f3;
                if (this.getPaint().getTypeface() != null) {
                    f = f2;
                    f4 = f3;
                    if (this.getPaint().getTypeface().equals((Object)Typeface.DEFAULT_BOLD)) {
                        f = 0.208f;
                        f4 = 0.208f;
                    }
                }
                f3 = f;
                f2 = f4;
                if (this.getTypeface() != null) {
                    f3 = f;
                    f2 = f4;
                    if (this.getTypeface().equals((Object)SAN_SERIF_BOLD)) {
                        f3 = 0.208f;
                        f2 = 0.208f;
                    }
                }
                f = f3;
                f4 = f2;
                if (this.getTypeface() != null) {
                    f = f3;
                    f4 = f2;
                    if (this.getTypeface().equals((Object)SAN_SERIF_CONDENSED_BOLD)) {
                        f = 0.208f;
                        f4 = 0.208f;
                    }
                }
                f2 = f4;
                if (Build.VERSION.SDK_INT >= 14) break block9;
                f2 = f4;
                if (this.getText() == null) break block9;
                if (this.getText().toString().equals(this.decimalSeperator)) break block10;
                f2 = f4;
                if (!this.getText().toString().equals(this.timeSeperator)) break block9;
            }
            f2 = 0.233f;
        }
        this.setPadding(0, (int)((- f) * this.getTextSize()), this.mPaddingRight, (int)((- f2) * this.getTextSize()));
    }

    public void updatePaddingForBoldDate() {
        this.setPadding(0, (int)((- 0.208f) * this.getTextSize()), this.mPaddingRight, (int)((- 0.208f) * this.getTextSize()));
    }
}

