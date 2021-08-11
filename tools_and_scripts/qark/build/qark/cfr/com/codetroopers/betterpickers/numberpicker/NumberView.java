/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.AssetManager
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.graphics.Typeface
 *  android.util.AttributeSet
 *  android.view.View
 *  android.widget.LinearLayout
 *  com.codetroopers.betterpickers.R
 *  com.codetroopers.betterpickers.R$color
 *  com.codetroopers.betterpickers.R$id
 *  com.codetroopers.betterpickers.R$styleable
 */
package com.codetroopers.betterpickers.numberpicker;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.codetroopers.betterpickers.R;
import com.codetroopers.betterpickers.widget.ZeroTopPaddingTextView;

public class NumberView
extends LinearLayout {
    private final Typeface mAndroidClockMonoThin;
    private ZeroTopPaddingTextView mDecimal;
    private ZeroTopPaddingTextView mDecimalSeperator;
    private ZeroTopPaddingTextView mMinusLabel;
    private ZeroTopPaddingTextView mNumber;
    private Typeface mOriginalNumberTypeface;
    private ColorStateList mTextColor;

    public NumberView(Context context) {
        this(context, null);
    }

    public NumberView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mAndroidClockMonoThin = Typeface.createFromAsset((AssetManager)context.getAssets(), (String)"fonts/AndroidClockMono-Thin.ttf");
        this.mTextColor = this.getResources().getColorStateList(R.color.dialog_text_color_holo_dark);
    }

    private void restyleViews() {
        ZeroTopPaddingTextView zeroTopPaddingTextView = this.mNumber;
        if (zeroTopPaddingTextView != null) {
            zeroTopPaddingTextView.setTextColor(this.mTextColor);
        }
        if ((zeroTopPaddingTextView = this.mDecimal) != null) {
            zeroTopPaddingTextView.setTextColor(this.mTextColor);
        }
        if ((zeroTopPaddingTextView = this.mDecimalSeperator) != null) {
            zeroTopPaddingTextView.setTextColor(this.mTextColor);
        }
        if ((zeroTopPaddingTextView = this.mMinusLabel) != null) {
            zeroTopPaddingTextView.setTextColor(this.mTextColor);
        }
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mNumber = (ZeroTopPaddingTextView)this.findViewById(R.id.number);
        this.mDecimal = (ZeroTopPaddingTextView)this.findViewById(R.id.decimal);
        this.mDecimalSeperator = (ZeroTopPaddingTextView)this.findViewById(R.id.decimal_separator);
        this.mMinusLabel = (ZeroTopPaddingTextView)this.findViewById(R.id.minus_label);
        ZeroTopPaddingTextView zeroTopPaddingTextView = this.mNumber;
        if (zeroTopPaddingTextView != null) {
            this.mOriginalNumberTypeface = zeroTopPaddingTextView.getTypeface();
        }
        if ((zeroTopPaddingTextView = this.mNumber) != null) {
            zeroTopPaddingTextView.setTypeface(this.mAndroidClockMonoThin);
            this.mNumber.updatePadding();
        }
        if ((zeroTopPaddingTextView = this.mDecimal) != null) {
            zeroTopPaddingTextView.setTypeface(this.mAndroidClockMonoThin);
            this.mDecimal.updatePadding();
        }
        this.restyleViews();
    }

    public void setNumber(String object, String string2, boolean bl, boolean bl2) {
        ZeroTopPaddingTextView zeroTopPaddingTextView = this.mMinusLabel;
        int n = 8;
        int n2 = bl2 ? 0 : 8;
        zeroTopPaddingTextView.setVisibility(n2);
        if (this.mNumber != null) {
            if (object.equals("")) {
                this.mNumber.setText((CharSequence)"-");
                this.mNumber.setTypeface(this.mAndroidClockMonoThin);
                this.mNumber.setEnabled(false);
                this.mNumber.updatePadding();
                this.mNumber.setVisibility(0);
            } else if (bl) {
                this.mNumber.setText((CharSequence)object);
                this.mNumber.setTypeface(this.mOriginalNumberTypeface);
                this.mNumber.setEnabled(true);
                this.mNumber.updatePaddingForBoldDate();
                this.mNumber.setVisibility(0);
            } else {
                this.mNumber.setText((CharSequence)object);
                this.mNumber.setTypeface(this.mAndroidClockMonoThin);
                this.mNumber.setEnabled(true);
                this.mNumber.updatePadding();
                this.mNumber.setVisibility(0);
            }
        }
        if (this.mDecimal != null) {
            if (string2.equals("")) {
                this.mDecimal.setVisibility(8);
            } else {
                this.mDecimal.setText((CharSequence)string2);
                this.mDecimal.setTypeface(this.mAndroidClockMonoThin);
                this.mDecimal.setEnabled(true);
                this.mDecimal.updatePadding();
                this.mDecimal.setVisibility(0);
            }
        }
        if ((object = this.mDecimalSeperator) != null) {
            n2 = n;
            if (bl) {
                n2 = 0;
            }
            object.setVisibility(n2);
        }
    }

    public void setTheme(int n) {
        if (n != -1) {
            this.mTextColor = this.getContext().obtainStyledAttributes(n, R.styleable.BetterPickersDialogFragment).getColorStateList(R.styleable.BetterPickersDialogFragment_bpTextColor);
        }
        this.restyleViews();
    }
}

