/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.graphics.Canvas
 *  android.graphics.Paint
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
import android.util.Log;
import android.view.View;
import androidx.core.content.ContextCompat;
import com.codetroopers.betterpickers.R;

public class CircleView
extends View {
    private static final String TAG = "CircleView";
    private float mAmPmCircleRadiusMultiplier;
    private int mCentralDotColor;
    private int mCircleColor;
    private int mCircleRadius;
    private float mCircleRadiusMultiplier;
    private boolean mDrawValuesReady;
    private boolean mIs24HourMode;
    private boolean mIsInitialized;
    private final Paint mPaint = new Paint();
    private int mXCenter;
    private int mYCenter;

    public CircleView(Context context) {
        super(context);
        context = context.getResources();
        this.mCircleColor = context.getColor(R.color.bpWhite);
        this.mCentralDotColor = context.getColor(R.color.numbers_text_color);
        this.mPaint.setAntiAlias(true);
        this.mIsInitialized = false;
    }

    public void initialize(Context context, boolean bl) {
        if (this.mIsInitialized) {
            Log.e((String)"CircleView", (String)"CircleView may only be initialized once.");
            return;
        }
        context = context.getResources();
        this.mIs24HourMode = bl;
        if (bl) {
            this.mCircleRadiusMultiplier = Float.parseFloat(context.getString(R.string.circle_radius_multiplier_24HourMode));
        } else {
            this.mCircleRadiusMultiplier = Float.parseFloat(context.getString(R.string.circle_radius_multiplier));
            this.mAmPmCircleRadiusMultiplier = Float.parseFloat(context.getString(R.string.ampm_circle_radius_multiplier));
        }
        this.mIsInitialized = true;
    }

    public void onDraw(Canvas canvas) {
        if (this.getWidth() != 0) {
            if (!this.mIsInitialized) {
                return;
            }
            if (!this.mDrawValuesReady) {
                int n;
                this.mXCenter = this.getWidth() / 2;
                this.mYCenter = n = this.getHeight() / 2;
                this.mCircleRadius = n = (int)((float)Math.min(this.mXCenter, n) * this.mCircleRadiusMultiplier);
                if (!this.mIs24HourMode) {
                    n = (int)((float)n * this.mAmPmCircleRadiusMultiplier);
                    this.mYCenter -= n / 2;
                }
                this.mDrawValuesReady = true;
            }
            this.mPaint.setColor(this.mCircleColor);
            canvas.drawCircle((float)this.mXCenter, (float)this.mYCenter, (float)this.mCircleRadius, this.mPaint);
            this.mPaint.setColor(this.mCentralDotColor);
            canvas.drawCircle((float)this.mXCenter, (float)this.mYCenter, 2.0f, this.mPaint);
            return;
        }
    }

    void setTheme(TypedArray typedArray) {
        this.mCircleColor = typedArray.getColor(R.styleable.BetterPickersDialogs_bpRadialBackgroundColor, ContextCompat.getColor(this.getContext(), R.color.radial_gray_light));
        this.mCentralDotColor = typedArray.getColor(R.styleable.BetterPickersDialogs_bpRadialTextColor, ContextCompat.getColor(this.getContext(), R.color.bpBlue));
    }
}

