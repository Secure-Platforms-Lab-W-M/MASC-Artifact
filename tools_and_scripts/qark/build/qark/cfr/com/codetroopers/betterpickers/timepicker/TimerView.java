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
package com.codetroopers.betterpickers.timepicker;

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

public class TimerView
extends LinearLayout {
    private final Typeface mAndroidClockMonoThin;
    private ZeroTopPaddingTextView mHoursOnes;
    private ZeroTopPaddingTextView mHoursSeperator;
    private ZeroTopPaddingTextView mHoursTens;
    private ZeroTopPaddingTextView mMinutesOnes;
    private ZeroTopPaddingTextView mMinutesTens;
    private Typeface mOriginalHoursTypeface;
    private ColorStateList mTextColor;

    public TimerView(Context context) {
        this(context, null);
    }

    public TimerView(Context context, AttributeSet attributeSet) {
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
        if ((zeroTopPaddingTextView = this.mHoursTens) != null) {
            zeroTopPaddingTextView.setTextColor(this.mTextColor);
        }
        if ((zeroTopPaddingTextView = this.mMinutesTens) != null) {
            zeroTopPaddingTextView.setTextColor(this.mTextColor);
        }
        if ((zeroTopPaddingTextView = this.mHoursSeperator) != null) {
            zeroTopPaddingTextView.setTextColor(this.mTextColor);
        }
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mHoursTens = (ZeroTopPaddingTextView)this.findViewById(R.id.hours_tens);
        this.mMinutesTens = (ZeroTopPaddingTextView)this.findViewById(R.id.minutes_tens);
        this.mHoursOnes = (ZeroTopPaddingTextView)this.findViewById(R.id.hours_ones);
        this.mMinutesOnes = (ZeroTopPaddingTextView)this.findViewById(R.id.minutes_ones);
        this.mHoursSeperator = (ZeroTopPaddingTextView)this.findViewById(R.id.hours_seperator);
        ZeroTopPaddingTextView zeroTopPaddingTextView = this.mHoursOnes;
        if (zeroTopPaddingTextView != null) {
            this.mOriginalHoursTypeface = zeroTopPaddingTextView.getTypeface();
        }
        if ((zeroTopPaddingTextView = this.mMinutesTens) != null) {
            zeroTopPaddingTextView.setTypeface(this.mAndroidClockMonoThin);
            this.mMinutesTens.updatePadding();
        }
        if ((zeroTopPaddingTextView = this.mMinutesOnes) != null) {
            zeroTopPaddingTextView.setTypeface(this.mAndroidClockMonoThin);
            this.mMinutesOnes.updatePadding();
        }
    }

    public void setTheme(int n) {
        if (n != -1) {
            this.mTextColor = this.getContext().obtainStyledAttributes(n, R.styleable.BetterPickersDialogFragment).getColorStateList(R.styleable.BetterPickersDialogFragment_bpTextColor);
        }
        this.restyleViews();
    }

    public void setTime(int n, int n2, int n3, int n4) {
        ZeroTopPaddingTextView zeroTopPaddingTextView = this.mHoursTens;
        if (zeroTopPaddingTextView != null) {
            if (n == -2) {
                zeroTopPaddingTextView.setVisibility(4);
            } else if (n == -1) {
                zeroTopPaddingTextView.setText((CharSequence)"-");
                this.mHoursTens.setTypeface(this.mAndroidClockMonoThin);
                this.mHoursTens.setEnabled(false);
                this.mHoursTens.updatePadding();
                this.mHoursTens.setVisibility(0);
            } else {
                zeroTopPaddingTextView.setText((CharSequence)String.format("%d", n));
                this.mHoursTens.setTypeface(this.mOriginalHoursTypeface);
                this.mHoursTens.setEnabled(true);
                this.mHoursTens.updatePaddingForBoldDate();
                this.mHoursTens.setVisibility(0);
            }
        }
        if ((zeroTopPaddingTextView = this.mHoursOnes) != null) {
            if (n2 == -1) {
                zeroTopPaddingTextView.setText((CharSequence)"-");
                this.mHoursOnes.setTypeface(this.mAndroidClockMonoThin);
                this.mHoursOnes.setEnabled(false);
                this.mHoursOnes.updatePadding();
            } else {
                zeroTopPaddingTextView.setText((CharSequence)String.format("%d", n2));
                this.mHoursOnes.setTypeface(this.mOriginalHoursTypeface);
                this.mHoursOnes.setEnabled(true);
                this.mHoursOnes.updatePaddingForBoldDate();
            }
        }
        if ((zeroTopPaddingTextView = this.mMinutesTens) != null) {
            if (n3 == -1) {
                zeroTopPaddingTextView.setText((CharSequence)"-");
                this.mMinutesTens.setEnabled(false);
            } else {
                zeroTopPaddingTextView.setEnabled(true);
                this.mMinutesTens.setText((CharSequence)String.format("%d", n3));
            }
        }
        if ((zeroTopPaddingTextView = this.mMinutesOnes) != null) {
            if (n4 == -1) {
                zeroTopPaddingTextView.setText((CharSequence)"-");
                this.mMinutesOnes.setEnabled(false);
                return;
            }
            zeroTopPaddingTextView.setText((CharSequence)String.format("%d", n4));
            this.mMinutesOnes.setEnabled(true);
        }
    }
}

