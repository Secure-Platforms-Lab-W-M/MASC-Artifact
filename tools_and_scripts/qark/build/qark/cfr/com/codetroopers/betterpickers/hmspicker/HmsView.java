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
package com.codetroopers.betterpickers.hmspicker;

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

public class HmsView
extends LinearLayout {
    private final Typeface mAndroidClockMonoThin;
    private ZeroTopPaddingTextView mHoursOnes;
    private ZeroTopPaddingTextView mMinusLabel;
    private ZeroTopPaddingTextView mMinutesOnes;
    private ZeroTopPaddingTextView mMinutesTens;
    private Typeface mOriginalHoursTypeface;
    private ZeroTopPaddingTextView mSecondsOnes;
    private ZeroTopPaddingTextView mSecondsTens;
    private ColorStateList mTextColor;

    public HmsView(Context context) {
        this(context, null);
    }

    public HmsView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mAndroidClockMonoThin = Typeface.createFromAsset((AssetManager)context.getAssets(), (String)"fonts/AndroidClockMono-Thin.ttf");
        this.mTextColor = this.getResources().getColorStateList(R.color.dialog_text_color_holo_dark);
    }

    private void restyleViews() {
        ZeroTopPaddingTextView zeroTopPaddingTextView = this.mHoursOnes;
        if (zeroTopPaddingTextView != null) {
            zeroTopPaddingTextView.setTextColor(this.mTextColor);
        }
        if ((zeroTopPaddingTextView = this.mMinutesOnes) != null) {
            zeroTopPaddingTextView.setTextColor(this.mTextColor);
        }
        if ((zeroTopPaddingTextView = this.mMinutesTens) != null) {
            zeroTopPaddingTextView.setTextColor(this.mTextColor);
        }
        if ((zeroTopPaddingTextView = this.mSecondsOnes) != null) {
            zeroTopPaddingTextView.setTextColor(this.mTextColor);
        }
        if ((zeroTopPaddingTextView = this.mSecondsTens) != null) {
            zeroTopPaddingTextView.setTextColor(this.mTextColor);
        }
        if ((zeroTopPaddingTextView = this.mMinusLabel) != null) {
            zeroTopPaddingTextView.setTextColor(this.mTextColor);
        }
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mHoursOnes = (ZeroTopPaddingTextView)this.findViewById(R.id.hours_ones);
        this.mMinutesTens = (ZeroTopPaddingTextView)this.findViewById(R.id.minutes_tens);
        this.mMinutesOnes = (ZeroTopPaddingTextView)this.findViewById(R.id.minutes_ones);
        this.mSecondsTens = (ZeroTopPaddingTextView)this.findViewById(R.id.seconds_tens);
        this.mSecondsOnes = (ZeroTopPaddingTextView)this.findViewById(R.id.seconds_ones);
        this.mMinusLabel = (ZeroTopPaddingTextView)this.findViewById(R.id.minus_label);
        ZeroTopPaddingTextView zeroTopPaddingTextView = this.mHoursOnes;
        if (zeroTopPaddingTextView != null) {
            this.mOriginalHoursTypeface = zeroTopPaddingTextView.getTypeface();
            this.mHoursOnes.updatePaddingForBoldDate();
        }
        if ((zeroTopPaddingTextView = this.mMinutesTens) != null) {
            zeroTopPaddingTextView.updatePaddingForBoldDate();
        }
        if ((zeroTopPaddingTextView = this.mMinutesOnes) != null) {
            zeroTopPaddingTextView.updatePaddingForBoldDate();
        }
        if ((zeroTopPaddingTextView = this.mSecondsTens) != null) {
            zeroTopPaddingTextView.setTypeface(this.mAndroidClockMonoThin);
            this.mSecondsTens.updatePadding();
        }
        if ((zeroTopPaddingTextView = this.mSecondsOnes) != null) {
            zeroTopPaddingTextView.setTypeface(this.mAndroidClockMonoThin);
            this.mSecondsOnes.updatePadding();
        }
    }

    public void setTheme(int n) {
        if (n != -1) {
            this.mTextColor = this.getContext().obtainStyledAttributes(n, R.styleable.BetterPickersDialogFragment).getColorStateList(R.styleable.BetterPickersDialogFragment_bpTextColor);
        }
        this.restyleViews();
    }

    public void setTime(int n, int n2, int n3, int n4, int n5) {
        this.setTime(false, n, n2, n3, n4, n5);
    }

    public void setTime(boolean bl, int n, int n2, int n3, int n4, int n5) {
        ZeroTopPaddingTextView zeroTopPaddingTextView = this.mMinusLabel;
        int n6 = bl ? 0 : 8;
        zeroTopPaddingTextView.setVisibility(n6);
        zeroTopPaddingTextView = this.mHoursOnes;
        if (zeroTopPaddingTextView != null) {
            zeroTopPaddingTextView.setText((CharSequence)String.format("%d", n));
        }
        if ((zeroTopPaddingTextView = this.mMinutesTens) != null) {
            zeroTopPaddingTextView.setText((CharSequence)String.format("%d", n2));
        }
        if ((zeroTopPaddingTextView = this.mMinutesOnes) != null) {
            zeroTopPaddingTextView.setText((CharSequence)String.format("%d", n3));
        }
        if ((zeroTopPaddingTextView = this.mSecondsTens) != null) {
            zeroTopPaddingTextView.setText((CharSequence)String.format("%d", n4));
        }
        if ((zeroTopPaddingTextView = this.mSecondsOnes) != null) {
            zeroTopPaddingTextView.setText((CharSequence)String.format("%d", n5));
        }
    }
}

