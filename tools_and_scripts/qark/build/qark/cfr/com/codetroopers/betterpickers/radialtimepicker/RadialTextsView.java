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
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Keyframe;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.animation.AnimatorProxy;

public class RadialTextsView
extends View {
    private static final String TAG = "RadialTextsView";
    private float mAmPmCircleRadiusMultiplier;
    private float mAnimationRadiusMultiplier;
    private float mCircleRadius;
    private float mCircleRadiusMultiplier;
    ObjectAnimator mDisappearAnimator;
    private boolean mDrawValuesReady;
    private boolean mHasInnerCircle;
    private float mInnerNumbersRadiusMultiplier;
    private float[] mInnerTextGridHeights;
    private float[] mInnerTextGridWidths;
    private float mInnerTextSize;
    private float mInnerTextSizeMultiplier;
    private String[] mInnerTexts;
    private InvalidateUpdateListener mInvalidateUpdateListener;
    private boolean mIs24HourMode;
    private boolean mIsInitialized = false;
    private float mNumbersRadiusMultiplier;
    private final Paint mPaint = new Paint();
    ObjectAnimator mReappearAnimator;
    private float[] mTextGridHeights;
    private boolean mTextGridValuesDirty;
    private float[] mTextGridWidths;
    private float mTextSize;
    private float mTextSizeMultiplier;
    private String[] mTexts;
    private float mTransitionEndRadiusMultiplier;
    private float mTransitionMidRadiusMultiplier;
    private Typeface mTypefaceLight;
    private Typeface mTypefaceRegular;
    private int mXCenter;
    private int mYCenter;

    public RadialTextsView(Context context) {
        super(context);
    }

    private void calculateGridSizes(float f, float f2, float f3, float f4, float[] arrf, float[] arrf2) {
        float f5 = (float)Math.sqrt(3.0) * f / 2.0f;
        float f6 = f / 2.0f;
        this.mPaint.setTextSize(f4);
        arrf[0] = (f3 -= (this.mPaint.descent() + this.mPaint.ascent()) / 2.0f) - f;
        arrf2[0] = f2 - f;
        arrf[1] = f3 - f5;
        arrf2[1] = f2 - f5;
        arrf[2] = f3 - f6;
        arrf2[2] = f2 - f6;
        arrf[3] = f3;
        arrf2[3] = f2;
        arrf[4] = f3 + f6;
        arrf2[4] = f2 + f6;
        arrf[5] = f3 + f5;
        arrf2[5] = f2 + f5;
        arrf[6] = f3 + f;
        arrf2[6] = f2 + f;
    }

    private void drawTexts(Canvas canvas, float f, Typeface typeface, String[] arrstring, float[] arrf, float[] arrf2) {
        this.mPaint.setTextSize(f);
        this.mPaint.setTypeface(typeface);
        canvas.drawText(arrstring[0], arrf[3], arrf2[0], this.mPaint);
        canvas.drawText(arrstring[1], arrf[4], arrf2[1], this.mPaint);
        canvas.drawText(arrstring[2], arrf[5], arrf2[2], this.mPaint);
        canvas.drawText(arrstring[3], arrf[6], arrf2[3], this.mPaint);
        canvas.drawText(arrstring[4], arrf[5], arrf2[4], this.mPaint);
        canvas.drawText(arrstring[5], arrf[4], arrf2[5], this.mPaint);
        canvas.drawText(arrstring[6], arrf[3], arrf2[6], this.mPaint);
        canvas.drawText(arrstring[7], arrf[2], arrf2[5], this.mPaint);
        canvas.drawText(arrstring[8], arrf[1], arrf2[4], this.mPaint);
        canvas.drawText(arrstring[9], arrf[0], arrf2[3], this.mPaint);
        canvas.drawText(arrstring[10], arrf[1], arrf2[2], this.mPaint);
        canvas.drawText(arrstring[11], arrf[2], arrf2[1], this.mPaint);
    }

    private void renderAnimations() {
        PropertyValuesHolder propertyValuesHolder = PropertyValuesHolder.ofKeyframe("animationRadiusMultiplier", Keyframe.ofFloat(0.0f, 1.0f), Keyframe.ofFloat(0.2f, this.mTransitionMidRadiusMultiplier), Keyframe.ofFloat(1.0f, this.mTransitionEndRadiusMultiplier));
        PropertyValuesHolder propertyValuesHolder2 = PropertyValuesHolder.ofKeyframe("alpha", Keyframe.ofFloat(0.0f, 1.0f), Keyframe.ofFloat(1.0f, 0.0f));
        Object object = AnimatorProxy.NEEDS_PROXY ? AnimatorProxy.wrap((View)this) : this;
        this.mDisappearAnimator = object = ObjectAnimator.ofPropertyValuesHolder(object, propertyValuesHolder, propertyValuesHolder2).setDuration(500);
        object.addUpdateListener(this.mInvalidateUpdateListener);
        int n = (int)((float)500 * (1.0f + 0.25f));
        float f = (float)500 * 0.25f / (float)n;
        propertyValuesHolder = PropertyValuesHolder.ofKeyframe("animationRadiusMultiplier", Keyframe.ofFloat(0.0f, this.mTransitionEndRadiusMultiplier), Keyframe.ofFloat(f, this.mTransitionEndRadiusMultiplier), Keyframe.ofFloat(1.0f - (1.0f - f) * 0.2f, this.mTransitionMidRadiusMultiplier), Keyframe.ofFloat(1.0f, 1.0f));
        propertyValuesHolder2 = PropertyValuesHolder.ofKeyframe("alpha", Keyframe.ofFloat(0.0f, 0.0f), Keyframe.ofFloat(f, 0.0f), Keyframe.ofFloat(1.0f, 1.0f));
        object = AnimatorProxy.NEEDS_PROXY ? AnimatorProxy.wrap((View)this) : this;
        this.mReappearAnimator = object = ObjectAnimator.ofPropertyValuesHolder(object, propertyValuesHolder, propertyValuesHolder2).setDuration(n);
        object.addUpdateListener(this.mInvalidateUpdateListener);
    }

    public ObjectAnimator getDisappearAnimator() {
        ObjectAnimator objectAnimator;
        if (this.mIsInitialized && this.mDrawValuesReady && (objectAnimator = this.mDisappearAnimator) != null) {
            return objectAnimator;
        }
        Log.e((String)"RadialTextsView", (String)"RadialTextView was not ready for animation.");
        return null;
    }

    public ObjectAnimator getReappearAnimator() {
        ObjectAnimator objectAnimator;
        if (this.mIsInitialized && this.mDrawValuesReady && (objectAnimator = this.mReappearAnimator) != null) {
            return objectAnimator;
        }
        Log.e((String)"RadialTextsView", (String)"RadialTextView was not ready for animation.");
        return null;
    }

    public boolean hasOverlappingRendering() {
        return false;
    }

    public void initialize(Resources resources, String[] arrstring, String[] arrstring2, boolean bl, boolean bl2) {
        if (this.mIsInitialized) {
            Log.e((String)"RadialTextsView", (String)"This RadialTextsView may only be initialized once.");
            return;
        }
        this.mPaint.setColor(resources.getColor(R.color.numbers_text_color));
        String string2 = resources.getString(R.string.radial_numbers_typeface);
        boolean bl3 = false;
        this.mTypefaceLight = Typeface.create((String)string2, (int)0);
        this.mTypefaceRegular = Typeface.create((String)resources.getString(R.string.sans_serif), (int)0);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setTextAlign(Paint.Align.CENTER);
        this.mTexts = arrstring;
        this.mInnerTexts = arrstring2;
        this.mIs24HourMode = bl;
        if (arrstring2 != null) {
            bl3 = true;
        }
        this.mHasInnerCircle = bl3;
        if (bl) {
            this.mCircleRadiusMultiplier = Float.parseFloat(resources.getString(R.string.circle_radius_multiplier_24HourMode));
        } else {
            this.mCircleRadiusMultiplier = Float.parseFloat(resources.getString(R.string.circle_radius_multiplier));
            this.mAmPmCircleRadiusMultiplier = Float.parseFloat(resources.getString(R.string.ampm_circle_radius_multiplier));
        }
        this.mTextGridHeights = new float[7];
        this.mTextGridWidths = new float[7];
        if (this.mHasInnerCircle) {
            this.mNumbersRadiusMultiplier = Float.parseFloat(resources.getString(R.string.numbers_radius_multiplier_outer));
            this.mTextSizeMultiplier = Float.parseFloat(resources.getString(R.string.text_size_multiplier_outer));
            this.mInnerNumbersRadiusMultiplier = Float.parseFloat(resources.getString(R.string.numbers_radius_multiplier_inner));
            this.mInnerTextSizeMultiplier = Float.parseFloat(resources.getString(R.string.text_size_multiplier_inner));
            this.mInnerTextGridHeights = new float[7];
            this.mInnerTextGridWidths = new float[7];
        } else {
            this.mNumbersRadiusMultiplier = Float.parseFloat(resources.getString(R.string.numbers_radius_multiplier_normal));
            this.mTextSizeMultiplier = Float.parseFloat(resources.getString(R.string.text_size_multiplier_normal));
        }
        this.mAnimationRadiusMultiplier = 1.0f;
        int n = -1;
        int n2 = bl2 ? -1 : 1;
        this.mTransitionMidRadiusMultiplier = (float)n2 * 0.05f + 1.0f;
        n2 = n;
        if (bl2) {
            n2 = 1;
        }
        this.mTransitionEndRadiusMultiplier = (float)n2 * 0.3f + 1.0f;
        this.mInvalidateUpdateListener = new InvalidateUpdateListener();
        this.mTextGridValuesDirty = true;
        this.mIsInitialized = true;
    }

    public void onDraw(Canvas canvas) {
        if (this.getWidth() != 0) {
            if (!this.mIsInitialized) {
                return;
            }
            if (!this.mDrawValuesReady) {
                int n;
                float f;
                this.mXCenter = this.getWidth() / 2;
                this.mYCenter = n = this.getHeight() / 2;
                this.mCircleRadius = f = (float)Math.min(this.mXCenter, n) * this.mCircleRadiusMultiplier;
                if (!this.mIs24HourMode) {
                    float f2 = this.mAmPmCircleRadiusMultiplier;
                    this.mYCenter = (int)((float)this.mYCenter - f * f2 / 2.0f);
                }
                f = this.mCircleRadius;
                this.mTextSize = this.mTextSizeMultiplier * f;
                if (this.mHasInnerCircle) {
                    this.mInnerTextSize = f * this.mInnerTextSizeMultiplier;
                }
                this.renderAnimations();
                this.mTextGridValuesDirty = true;
                this.mDrawValuesReady = true;
            }
            if (this.mTextGridValuesDirty) {
                this.calculateGridSizes(this.mCircleRadius * this.mNumbersRadiusMultiplier * this.mAnimationRadiusMultiplier, this.mXCenter, this.mYCenter, this.mTextSize, this.mTextGridHeights, this.mTextGridWidths);
                if (this.mHasInnerCircle) {
                    this.calculateGridSizes(this.mCircleRadius * this.mInnerNumbersRadiusMultiplier * this.mAnimationRadiusMultiplier, this.mXCenter, this.mYCenter, this.mInnerTextSize, this.mInnerTextGridHeights, this.mInnerTextGridWidths);
                }
                this.mTextGridValuesDirty = false;
            }
            this.drawTexts(canvas, this.mTextSize, this.mTypefaceLight, this.mTexts, this.mTextGridWidths, this.mTextGridHeights);
            if (this.mHasInnerCircle) {
                this.drawTexts(canvas, this.mInnerTextSize, this.mTypefaceRegular, this.mInnerTexts, this.mInnerTextGridWidths, this.mInnerTextGridHeights);
            }
            return;
        }
    }

    public void setAnimationRadiusMultiplier(float f) {
        this.mAnimationRadiusMultiplier = f;
        this.mTextGridValuesDirty = true;
    }

    void setTheme(TypedArray typedArray) {
        this.mPaint.setColor(typedArray.getColor(R.styleable.BetterPickersDialogs_bpRadialTextColor, ContextCompat.getColor(this.getContext(), R.color.bpBlue)));
    }

    private class InvalidateUpdateListener
    implements ValueAnimator.AnimatorUpdateListener {
        private InvalidateUpdateListener() {
        }

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            RadialTextsView.this.invalidate();
        }
    }

}

