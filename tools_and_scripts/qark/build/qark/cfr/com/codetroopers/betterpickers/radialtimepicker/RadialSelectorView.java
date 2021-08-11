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
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Keyframe;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.animation.AnimatorProxy;

public class RadialSelectorView
extends View {
    private static final int FULL_ALPHA = 255;
    private static final String TAG = "RadialSelectorView";
    private float mAmPmCircleRadiusMultiplier;
    private float mAnimationRadiusMultiplier;
    private int mCircleRadius;
    private float mCircleRadiusMultiplier;
    private boolean mDrawValuesReady;
    private boolean mForceDrawDot;
    private boolean mHasInnerCircle;
    private float mInnerNumbersRadiusMultiplier;
    private InvalidateUpdateListener mInvalidateUpdateListener;
    private boolean mIs24HourMode;
    private boolean mIsInitialized = false;
    private int mLineLength;
    private float mNumbersRadiusMultiplier;
    private float mOuterNumbersRadiusMultiplier;
    private final Paint mPaint = new Paint();
    private int mSelectionAlpha;
    private int mSelectionDegrees;
    private double mSelectionRadians;
    private int mSelectionRadius;
    private float mSelectionRadiusMultiplier;
    private float mTransitionEndRadiusMultiplier;
    private float mTransitionMidRadiusMultiplier;
    private int mXCenter;
    private int mYCenter;

    public RadialSelectorView(Context context) {
        super(context);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int getDegreesFromCoords(float f, float f2, boolean bl, Boolean[] arrboolean) {
        int n;
        int n2;
        if (!this.mDrawValuesReady) {
            return -1;
        }
        int n3 = this.mYCenter;
        float f3 = n3;
        float f4 = n3;
        n3 = this.mXCenter;
        double d = Math.sqrt((f2 - f3) * (f2 - f4) + (f - (float)n3) * (f - (float)n3));
        boolean bl2 = this.mHasInnerCircle;
        boolean bl3 = true;
        if (bl2) {
            if (bl) {
                bl = (int)Math.abs(d - (double)((int)((float)this.mCircleRadius * this.mInnerNumbersRadiusMultiplier))) <= (int)Math.abs(d - (double)((int)((float)this.mCircleRadius * this.mOuterNumbersRadiusMultiplier)));
                arrboolean[0] = bl;
            } else {
                int n4 = this.mCircleRadius;
                f4 = n4;
                f3 = this.mInnerNumbersRadiusMultiplier;
                n3 = (int)(f4 * f3);
                n2 = this.mSelectionRadius;
                f4 = n4;
                float f5 = this.mOuterNumbersRadiusMultiplier;
                n = (int)(f4 * f5);
                n4 = (int)((float)n4 * ((f5 + f3) / 2.0f));
                if (d >= (double)(n3 - n2) && d <= (double)n4) {
                    arrboolean[0] = true;
                } else {
                    if (d > (double)(n + n2)) return -1;
                    if (d < (double)n4) return -1;
                    arrboolean[0] = false;
                }
            }
        } else if (!bl && (int)Math.abs(d - (double)this.mLineLength) > (int)((float)this.mCircleRadius * (1.0f - this.mNumbersRadiusMultiplier))) {
            return -1;
        }
        n = (int)(180.0 * Math.asin((double)Math.abs(f2 - (float)this.mYCenter) / d) / 3.141592653589793);
        n3 = f > (float)this.mXCenter ? 1 : 0;
        if (f2 >= (float)this.mYCenter) return n + 90;
        if (n3 != 0 && bl3) {
            return 90 - n;
        }
        if (n3 != 0 && !bl3) {
            return n + 90;
        }
        if (n3 == 0 && !bl3) {
            return 270 - n;
        }
        n2 = n;
        if (n3 != 0) return n2;
        n2 = n;
        if (!bl3) return n2;
        return n + 270;
    }

    public ObjectAnimator getDisappearAnimator() {
        if (this.mIsInitialized && this.mDrawValuesReady) {
            PropertyValuesHolder propertyValuesHolder = PropertyValuesHolder.ofKeyframe("animationRadiusMultiplier", Keyframe.ofFloat(0.0f, 1.0f), Keyframe.ofFloat(0.2f, this.mTransitionMidRadiusMultiplier), Keyframe.ofFloat(1.0f, this.mTransitionEndRadiusMultiplier));
            PropertyValuesHolder propertyValuesHolder2 = PropertyValuesHolder.ofKeyframe("alpha", Keyframe.ofFloat(0.0f, 1.0f), Keyframe.ofFloat(1.0f, 0.0f));
            Object object = AnimatorProxy.NEEDS_PROXY ? AnimatorProxy.wrap((View)this) : this;
            object = ObjectAnimator.ofPropertyValuesHolder(object, propertyValuesHolder, propertyValuesHolder2).setDuration(500);
            object.addUpdateListener(this.mInvalidateUpdateListener);
            return object;
        }
        Log.e((String)"RadialSelectorView", (String)"RadialSelectorView was not ready for animation.");
        return null;
    }

    public ObjectAnimator getReappearAnimator() {
        if (this.mIsInitialized && this.mDrawValuesReady) {
            int n = (int)((float)500 * (1.0f + 0.25f));
            float f = (float)500 * 0.25f / (float)n;
            PropertyValuesHolder propertyValuesHolder = PropertyValuesHolder.ofKeyframe("animationRadiusMultiplier", Keyframe.ofFloat(0.0f, this.mTransitionEndRadiusMultiplier), Keyframe.ofFloat(f, this.mTransitionEndRadiusMultiplier), Keyframe.ofFloat(1.0f - (1.0f - f) * 0.2f, this.mTransitionMidRadiusMultiplier), Keyframe.ofFloat(1.0f, 1.0f));
            PropertyValuesHolder propertyValuesHolder2 = PropertyValuesHolder.ofKeyframe("alpha", Keyframe.ofFloat(0.0f, 0.0f), Keyframe.ofFloat(f, 0.0f), Keyframe.ofFloat(1.0f, 1.0f));
            Object object = AnimatorProxy.NEEDS_PROXY ? AnimatorProxy.wrap((View)this) : this;
            object = ObjectAnimator.ofPropertyValuesHolder(object, propertyValuesHolder, propertyValuesHolder2).setDuration(n);
            object.addUpdateListener(this.mInvalidateUpdateListener);
            return object;
        }
        Log.e((String)"RadialSelectorView", (String)"RadialSelectorView was not ready for animation.");
        return null;
    }

    public boolean hasOverlappingRendering() {
        return false;
    }

    public void initialize(Context context, boolean bl, boolean bl2, boolean bl3, int n, boolean bl4) {
        if (this.mIsInitialized) {
            Log.e((String)"RadialSelectorView", (String)"This RadialSelectorView may only be initialized once.");
            return;
        }
        context = context.getResources();
        this.mPaint.setAntiAlias(true);
        this.mIs24HourMode = bl;
        if (bl) {
            this.mCircleRadiusMultiplier = Float.parseFloat(context.getString(R.string.circle_radius_multiplier_24HourMode));
        } else {
            this.mCircleRadiusMultiplier = Float.parseFloat(context.getString(R.string.circle_radius_multiplier));
            this.mAmPmCircleRadiusMultiplier = Float.parseFloat(context.getString(R.string.ampm_circle_radius_multiplier));
        }
        this.mHasInnerCircle = bl2;
        if (bl2) {
            this.mInnerNumbersRadiusMultiplier = Float.parseFloat(context.getString(R.string.numbers_radius_multiplier_inner));
            this.mOuterNumbersRadiusMultiplier = Float.parseFloat(context.getString(R.string.numbers_radius_multiplier_outer));
        } else {
            this.mNumbersRadiusMultiplier = Float.parseFloat(context.getString(R.string.numbers_radius_multiplier_normal));
        }
        this.mSelectionRadiusMultiplier = Float.parseFloat(context.getString(R.string.selection_radius_multiplier));
        this.mAnimationRadiusMultiplier = 1.0f;
        int n2 = -1;
        int n3 = bl3 ? -1 : 1;
        this.mTransitionMidRadiusMultiplier = (float)n3 * 0.05f + 1.0f;
        n3 = n2;
        if (bl3) {
            n3 = 1;
        }
        this.mTransitionEndRadiusMultiplier = (float)n3 * 0.3f + 1.0f;
        this.mInvalidateUpdateListener = new InvalidateUpdateListener();
        this.setSelection(n, bl4, false);
        this.mIsInitialized = true;
    }

    public void onDraw(Canvas canvas) {
        if (this.getWidth() != 0) {
            int n;
            if (!this.mIsInitialized) {
                return;
            }
            boolean bl = this.mDrawValuesReady;
            int n2 = 1;
            if (!bl) {
                this.mXCenter = this.getWidth() / 2;
                this.mYCenter = n = this.getHeight() / 2;
                this.mCircleRadius = n = (int)((float)Math.min(this.mXCenter, n) * this.mCircleRadiusMultiplier);
                if (!this.mIs24HourMode) {
                    n = (int)((float)n * this.mAmPmCircleRadiusMultiplier);
                    this.mYCenter -= n / 2;
                }
                this.mSelectionRadius = (int)((float)this.mCircleRadius * this.mSelectionRadiusMultiplier);
                this.mDrawValuesReady = true;
            }
            this.mLineLength = n = (int)((float)this.mCircleRadius * this.mNumbersRadiusMultiplier * this.mAnimationRadiusMultiplier);
            n = this.mXCenter + (int)((double)n * Math.sin(this.mSelectionRadians));
            int n3 = this.mYCenter - (int)((double)this.mLineLength * Math.cos(this.mSelectionRadians));
            this.mPaint.setAlpha(this.mSelectionAlpha);
            canvas.drawCircle((float)n, (float)n3, (float)this.mSelectionRadius, this.mPaint);
            bl = this.mForceDrawDot;
            if (this.mSelectionDegrees % 30 == 0) {
                n2 = 0;
            }
            if (n2 | bl) {
                this.mPaint.setAlpha(255);
                canvas.drawCircle((float)n, (float)n3, (float)(this.mSelectionRadius * 2 / 7), this.mPaint);
                n2 = n3;
            } else {
                n2 = this.mLineLength - this.mSelectionRadius;
                n = this.mXCenter + (int)((double)n2 * Math.sin(this.mSelectionRadians));
                n2 = this.mYCenter - (int)((double)n2 * Math.cos(this.mSelectionRadians));
            }
            this.mPaint.setAlpha(255);
            this.mPaint.setStrokeWidth(1.0f);
            canvas.drawLine((float)this.mXCenter, (float)this.mYCenter, (float)n, (float)n2, this.mPaint);
            return;
        }
    }

    public void setAnimationRadiusMultiplier(float f) {
        this.mAnimationRadiusMultiplier = f;
    }

    public void setSelection(int n, boolean bl, boolean bl2) {
        this.mSelectionDegrees = n;
        this.mSelectionRadians = (double)n * 3.141592653589793 / 180.0;
        this.mForceDrawDot = bl2;
        if (this.mHasInnerCircle) {
            if (bl) {
                this.mNumbersRadiusMultiplier = this.mInnerNumbersRadiusMultiplier;
                return;
            }
            this.mNumbersRadiusMultiplier = this.mOuterNumbersRadiusMultiplier;
        }
    }

    void setTheme(TypedArray typedArray) {
        this.mPaint.setColor(typedArray.getColor(R.styleable.BetterPickersDialogs_bpRadialPointerColor, ContextCompat.getColor(this.getContext(), R.color.bpBlue)));
        this.mSelectionAlpha = typedArray.getInt(R.styleable.BetterPickersDialogs_bpRadialPointerAlpha, 35);
    }

    private class InvalidateUpdateListener
    implements ValueAnimator.AnimatorUpdateListener {
        private InvalidateUpdateListener() {
        }

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            RadialSelectorView.this.invalidate();
        }
    }

}

