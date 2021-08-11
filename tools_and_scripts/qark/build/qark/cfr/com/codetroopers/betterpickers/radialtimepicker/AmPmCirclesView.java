/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.graphics.Paint$Align
 *  android.graphics.Typeface
 *  android.util.Log
 *  android.view.View
 *  com.codetroopers.betterpickers.R
 *  com.codetroopers.betterpickers.R$color
 *  com.codetroopers.betterpickers.R$string
 *  com.codetroopers.betterpickers.R$styleable
 */
package com.codetroopers.betterpickers.radialtimepicker;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import androidx.core.content.ContextCompat;
import com.codetroopers.betterpickers.R;
import java.text.DateFormatSymbols;

public class AmPmCirclesView
extends View {
    private static final int AM = 0;
    private static final int PM = 1;
    private static final String TAG = "AmPmCirclesView";
    private int mAmOrPm;
    private int mAmOrPmPressed;
    private int mAmPmCircleRadius;
    private float mAmPmCircleRadiusMultiplier;
    private int mAmPmTextColor;
    private int mAmPmYCenter;
    private String mAmText;
    private int mAmXCenter;
    private float mCircleRadiusMultiplier;
    private boolean mDrawValuesReady;
    private boolean mIsInitialized = false;
    private final Paint mPaint = new Paint();
    private String mPmText;
    private int mPmXCenter;
    private int mSelectedAlpha;
    private int mSelectedColor;
    private int mUnselectedAlpha;
    private int mUnselectedColor;

    public AmPmCirclesView(Context context) {
        super(context);
    }

    public int getIsTouchingAmOrPm(float f, float f2) {
        if (!this.mDrawValuesReady) {
            return -1;
        }
        int n = this.mAmPmYCenter;
        int n2 = this.mAmXCenter;
        if ((int)Math.sqrt((f - (float)n2) * (f - (float)n2) + (float)(n = (int)((f2 - (float)n) * (f2 - (float)n)))) <= this.mAmPmCircleRadius) {
            return 0;
        }
        n2 = this.mPmXCenter;
        if ((int)Math.sqrt((f - (float)n2) * (f - (float)n2) + (float)n) <= this.mAmPmCircleRadius) {
            return 1;
        }
        return -1;
    }

    public void initialize(Context arrstring, int n) {
        if (this.mIsInitialized) {
            Log.e((String)"AmPmCirclesView", (String)"AmPmCirclesView may only be initialized once.");
            return;
        }
        arrstring = arrstring.getResources();
        this.mUnselectedColor = arrstring.getColor(R.color.bpWhite);
        this.mSelectedColor = arrstring.getColor(R.color.bpBlue);
        this.mAmPmTextColor = arrstring.getColor(R.color.ampm_text_color);
        Typeface typeface = Typeface.create((String)arrstring.getString(R.string.sans_serif), (int)0);
        this.mPaint.setTypeface(typeface);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setTextAlign(Paint.Align.CENTER);
        this.mCircleRadiusMultiplier = Float.parseFloat(arrstring.getString(R.string.circle_radius_multiplier));
        this.mAmPmCircleRadiusMultiplier = Float.parseFloat(arrstring.getString(R.string.ampm_circle_radius_multiplier));
        arrstring = new DateFormatSymbols().getAmPmStrings();
        this.mAmText = arrstring[0];
        this.mPmText = arrstring[1];
        this.setAmOrPm(n);
        this.mAmOrPmPressed = -1;
        this.mIsInitialized = true;
    }

    public void onDraw(Canvas canvas) {
        if (this.getWidth() != 0) {
            int n;
            int n2;
            int n3;
            int n4;
            if (!this.mIsInitialized) {
                return;
            }
            if (!this.mDrawValuesReady) {
                n2 = this.getWidth() / 2;
                n = this.getHeight() / 2;
                n4 = (int)((float)Math.min(n2, n) * this.mCircleRadiusMultiplier);
                this.mAmPmCircleRadius = n3 = (int)((float)n4 * this.mAmPmCircleRadiusMultiplier);
                n3 = n3 * 3 / 4;
                this.mPaint.setTextSize((float)n3);
                n3 = this.mAmPmCircleRadius;
                this.mAmPmYCenter = n - n3 / 2 + n4;
                this.mAmXCenter = n2 - n4 + n3;
                this.mPmXCenter = n2 + n4 - n3;
                this.mDrawValuesReady = true;
            }
            int n5 = this.mUnselectedColor;
            int n6 = this.mUnselectedAlpha;
            n3 = this.mUnselectedColor;
            n4 = this.mUnselectedAlpha;
            int n7 = this.mAmOrPm;
            if (n7 == 0) {
                n2 = this.mSelectedColor;
                n = this.mSelectedAlpha;
            } else {
                n2 = n5;
                n = n6;
                if (n7 == 1) {
                    n3 = this.mSelectedColor;
                    n4 = this.mSelectedAlpha;
                    n = n6;
                    n2 = n5;
                }
            }
            n7 = this.mAmOrPmPressed;
            if (n7 == 0) {
                n5 = this.mSelectedColor;
                n6 = this.mSelectedAlpha;
            } else {
                n5 = n2;
                n6 = n;
                if (n7 == 1) {
                    n3 = this.mSelectedColor;
                    n4 = this.mSelectedAlpha;
                    n6 = n;
                    n5 = n2;
                }
            }
            this.mPaint.setColor(n5);
            this.mPaint.setAlpha(n6);
            canvas.drawCircle((float)this.mAmXCenter, (float)this.mAmPmYCenter, (float)this.mAmPmCircleRadius, this.mPaint);
            this.mPaint.setColor(n3);
            this.mPaint.setAlpha(n4);
            canvas.drawCircle((float)this.mPmXCenter, (float)this.mAmPmYCenter, (float)this.mAmPmCircleRadius, this.mPaint);
            this.mPaint.setColor(this.mAmPmTextColor);
            n2 = this.mAmPmYCenter - (int)(this.mPaint.descent() + this.mPaint.ascent()) / 2;
            canvas.drawText(this.mAmText, (float)this.mAmXCenter, (float)n2, this.mPaint);
            canvas.drawText(this.mPmText, (float)this.mPmXCenter, (float)n2, this.mPaint);
            return;
        }
    }

    public void setAmOrPm(int n) {
        this.mAmOrPm = n;
    }

    public void setAmOrPmPressed(int n) {
        this.mAmOrPmPressed = n;
    }

    void setTheme(TypedArray typedArray) {
        this.mUnselectedColor = typedArray.getColor(R.styleable.BetterPickersDialogs_bpAmPmCircleColor, ContextCompat.getColor(this.getContext(), R.color.bpBlue));
        this.mSelectedColor = typedArray.getColor(R.styleable.BetterPickersDialogs_bpAmPmCircleColor, ContextCompat.getColor(this.getContext(), R.color.bpBlue));
        this.mAmPmTextColor = typedArray.getColor(R.styleable.BetterPickersDialogs_bpAmPmTextColor, ContextCompat.getColor(this.getContext(), R.color.bpWhite));
        this.mSelectedAlpha = 200;
        this.mUnselectedAlpha = 50;
    }
}

