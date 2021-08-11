/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.TimeInterpolator
 *  android.animation.ValueAnimator
 *  android.animation.ValueAnimator$AnimatorUpdateListener
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Canvas
 *  android.graphics.ColorFilter
 *  android.graphics.Paint
 *  android.graphics.Paint$Cap
 *  android.graphics.Paint$Style
 *  android.graphics.Path
 *  android.graphics.Path$FillType
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  android.graphics.drawable.Animatable
 *  android.graphics.drawable.Drawable
 *  android.util.DisplayMetrics
 *  android.view.animation.Interpolator
 *  android.view.animation.LinearInterpolator
 */
package android.support.v4.widget;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.v4.util.Preconditions;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.DisplayMetrics;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class CircularProgressDrawable
extends Drawable
implements Animatable {
    private static final int ANIMATION_DURATION = 1332;
    private static final int ARROW_HEIGHT = 5;
    private static final int ARROW_HEIGHT_LARGE = 6;
    private static final int ARROW_WIDTH = 10;
    private static final int ARROW_WIDTH_LARGE = 12;
    private static final float CENTER_RADIUS = 7.5f;
    private static final float CENTER_RADIUS_LARGE = 11.0f;
    private static final int[] COLORS;
    private static final float COLOR_CHANGE_OFFSET = 0.75f;
    public static final int DEFAULT = 1;
    private static final float GROUP_FULL_ROTATION = 216.0f;
    public static final int LARGE = 0;
    private static final Interpolator LINEAR_INTERPOLATOR;
    private static final Interpolator MATERIAL_INTERPOLATOR;
    private static final float MAX_PROGRESS_ARC = 0.8f;
    private static final float MIN_PROGRESS_ARC = 0.01f;
    private static final float RING_ROTATION = 0.20999998f;
    private static final float SHRINK_OFFSET = 0.5f;
    private static final float STROKE_WIDTH = 2.5f;
    private static final float STROKE_WIDTH_LARGE = 3.0f;
    private Animator mAnimator;
    private boolean mFinishing;
    private Resources mResources;
    private final Ring mRing;
    private float mRotation;
    private float mRotationCount;

    static {
        LINEAR_INTERPOLATOR = new LinearInterpolator();
        MATERIAL_INTERPOLATOR = new FastOutSlowInInterpolator();
        COLORS = new int[]{-16777216};
    }

    public CircularProgressDrawable(Context context) {
        this.mResources = Preconditions.checkNotNull(context).getResources();
        this.mRing = new Ring();
        this.mRing.setColors(COLORS);
        this.setStrokeWidth(2.5f);
        this.setupAnimators();
    }

    private void applyFinishTranslation(float f, Ring ring) {
        this.updateRingColor(f, ring);
        float f2 = (float)(Math.floor(ring.getStartingRotation() / 0.8f) + 1.0);
        ring.setStartTrim(ring.getStartingStartTrim() + (ring.getStartingEndTrim() - 0.01f - ring.getStartingStartTrim()) * f);
        ring.setEndTrim(ring.getStartingEndTrim());
        ring.setRotation(ring.getStartingRotation() + (f2 - ring.getStartingRotation()) * f);
    }

    private void applyTransformation(float f, Ring ring, boolean bl) {
        float f2;
        float f3;
        float f4;
        if (this.mFinishing) {
            this.applyFinishTranslation(f, ring);
            return;
        }
        if (f == 1.0f && !bl) {
            return;
        }
        float f5 = ring.getStartingRotation();
        if (f < 0.5f) {
            f2 = f / 0.5f;
            f4 = ring.getStartingStartTrim();
            f3 = MATERIAL_INTERPOLATOR.getInterpolation(f2);
            f2 = f4;
            f4 = f3 * 0.79f + 0.01f + f4;
        } else {
            f2 = (f - 0.5f) / 0.5f;
            f4 = ring.getStartingStartTrim() + 0.79f;
            f2 = f4 - ((1.0f - MATERIAL_INTERPOLATOR.getInterpolation(f2)) * 0.79f + 0.01f);
        }
        f3 = this.mRotationCount;
        ring.setStartTrim(f2);
        ring.setEndTrim(f4);
        ring.setRotation(0.20999998f * f + f5);
        this.setRotation((f3 + f) * 216.0f);
    }

    private int evaluateColorChange(float f, int n, int n2) {
        int n3 = n >> 24 & 255;
        int n4 = n >> 16 & 255;
        int n5 = n >> 8 & 255;
        return (int)((float)((n2 >> 24 & 255) - n3) * f) + n3 << 24 | (int)((float)((n2 >> 16 & 255) - n4) * f) + n4 << 16 | (int)((float)((n2 >> 8 & 255) - n5) * f) + n5 << 8 | (int)((float)((n2 & 255) - n) * f) + (n &= 255);
    }

    private float getRotation() {
        return this.mRotation;
    }

    private void setRotation(float f) {
        this.mRotation = f;
    }

    private void setSizeParameters(float f, float f2, float f3, float f4) {
        Ring ring = this.mRing;
        float f5 = this.mResources.getDisplayMetrics().density;
        ring.setStrokeWidth(f2 * f5);
        ring.setCenterRadius(f * f5);
        ring.setColorIndex(0);
        ring.setArrowDimensions(f3 * f5, f4 * f5);
    }

    private void setupAnimators() {
        final Ring ring = this.mRing;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat((float[])new float[]{0.0f, 1.0f});
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){

            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float f = ((Float)valueAnimator.getAnimatedValue()).floatValue();
                CircularProgressDrawable.this.updateRingColor(f, ring);
                CircularProgressDrawable.this.applyTransformation(f, ring, false);
                CircularProgressDrawable.this.invalidateSelf();
            }
        });
        valueAnimator.setRepeatCount(-1);
        valueAnimator.setRepeatMode(1);
        valueAnimator.setInterpolator((TimeInterpolator)LINEAR_INTERPOLATOR);
        valueAnimator.addListener(new Animator.AnimatorListener(){

            public void onAnimationCancel(Animator animator2) {
            }

            public void onAnimationEnd(Animator animator2) {
            }

            public void onAnimationRepeat(Animator object) {
                CircularProgressDrawable.this.applyTransformation(1.0f, ring, true);
                ring.storeOriginals();
                ring.goToNextColor();
                if (CircularProgressDrawable.this.mFinishing) {
                    CircularProgressDrawable.this.mFinishing = false;
                    object.cancel();
                    object.setDuration(1332L);
                    object.start();
                    ring.setShowArrow(false);
                    return;
                }
                object = CircularProgressDrawable.this;
                ((CircularProgressDrawable)((Object)object)).mRotationCount = ((CircularProgressDrawable)((Object)object)).mRotationCount + 1.0f;
            }

            public void onAnimationStart(Animator animator2) {
                CircularProgressDrawable.this.mRotationCount = 0.0f;
            }
        });
        this.mAnimator = valueAnimator;
    }

    private void updateRingColor(float f, Ring ring) {
        if (f > 0.75f) {
            ring.setColor(this.evaluateColorChange((f - 0.75f) / 0.25f, ring.getStartingColor(), ring.getNextColor()));
            return;
        }
        ring.setColor(ring.getStartingColor());
    }

    public void draw(Canvas canvas) {
        Rect rect = this.getBounds();
        canvas.save();
        canvas.rotate(this.mRotation, rect.exactCenterX(), rect.exactCenterY());
        this.mRing.draw(canvas, rect);
        canvas.restore();
    }

    public int getAlpha() {
        return this.mRing.getAlpha();
    }

    public boolean getArrowEnabled() {
        return this.mRing.getShowArrow();
    }

    public float getArrowHeight() {
        return this.mRing.getArrowHeight();
    }

    public float getArrowScale() {
        return this.mRing.getArrowScale();
    }

    public float getArrowWidth() {
        return this.mRing.getArrowWidth();
    }

    public int getBackgroundColor() {
        return this.mRing.getBackgroundColor();
    }

    public float getCenterRadius() {
        return this.mRing.getCenterRadius();
    }

    public int[] getColorSchemeColors() {
        return this.mRing.getColors();
    }

    public float getEndTrim() {
        return this.mRing.getEndTrim();
    }

    public int getOpacity() {
        return -3;
    }

    public float getProgressRotation() {
        return this.mRing.getRotation();
    }

    public float getStartTrim() {
        return this.mRing.getStartTrim();
    }

    public Paint.Cap getStrokeCap() {
        return this.mRing.getStrokeCap();
    }

    public float getStrokeWidth() {
        return this.mRing.getStrokeWidth();
    }

    public boolean isRunning() {
        return this.mAnimator.isRunning();
    }

    public void setAlpha(int n) {
        this.mRing.setAlpha(n);
        this.invalidateSelf();
    }

    public void setArrowDimensions(float f, float f2) {
        this.mRing.setArrowDimensions(f, f2);
        this.invalidateSelf();
    }

    public void setArrowEnabled(boolean bl) {
        this.mRing.setShowArrow(bl);
        this.invalidateSelf();
    }

    public void setArrowScale(float f) {
        this.mRing.setArrowScale(f);
        this.invalidateSelf();
    }

    public void setBackgroundColor(int n) {
        this.mRing.setBackgroundColor(n);
        this.invalidateSelf();
    }

    public void setCenterRadius(float f) {
        this.mRing.setCenterRadius(f);
        this.invalidateSelf();
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mRing.setColorFilter(colorFilter);
        this.invalidateSelf();
    }

    public /* varargs */ void setColorSchemeColors(int ... arrn) {
        this.mRing.setColors(arrn);
        this.mRing.setColorIndex(0);
        this.invalidateSelf();
    }

    public void setProgressRotation(float f) {
        this.mRing.setRotation(f);
        this.invalidateSelf();
    }

    public void setStartEndTrim(float f, float f2) {
        this.mRing.setStartTrim(f);
        this.mRing.setEndTrim(f2);
        this.invalidateSelf();
    }

    public void setStrokeCap(Paint.Cap cap) {
        this.mRing.setStrokeCap(cap);
        this.invalidateSelf();
    }

    public void setStrokeWidth(float f) {
        this.mRing.setStrokeWidth(f);
        this.invalidateSelf();
    }

    public void setStyle(int n) {
        if (n == 0) {
            this.setSizeParameters(11.0f, 3.0f, 12.0f, 6.0f);
        } else {
            this.setSizeParameters(7.5f, 2.5f, 10.0f, 5.0f);
        }
        this.invalidateSelf();
    }

    public void start() {
        this.mAnimator.cancel();
        this.mRing.storeOriginals();
        if (this.mRing.getEndTrim() != this.mRing.getStartTrim()) {
            this.mFinishing = true;
            this.mAnimator.setDuration(666L);
            this.mAnimator.start();
            return;
        }
        this.mRing.setColorIndex(0);
        this.mRing.resetOriginals();
        this.mAnimator.setDuration(1332L);
        this.mAnimator.start();
    }

    public void stop() {
        this.mAnimator.cancel();
        this.setRotation(0.0f);
        this.mRing.setShowArrow(false);
        this.mRing.setColorIndex(0);
        this.mRing.resetOriginals();
        this.invalidateSelf();
    }

    @Retention(value=RetentionPolicy.SOURCE)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static @interface ProgressDrawableSize {
    }

    private static class Ring {
        int mAlpha = 255;
        Path mArrow;
        int mArrowHeight;
        final Paint mArrowPaint = new Paint();
        float mArrowScale = 1.0f;
        int mArrowWidth;
        final Paint mCirclePaint = new Paint();
        int mColorIndex;
        int[] mColors;
        int mCurrentColor;
        float mEndTrim = 0.0f;
        final Paint mPaint = new Paint();
        float mRingCenterRadius;
        float mRotation = 0.0f;
        boolean mShowArrow;
        float mStartTrim = 0.0f;
        float mStartingEndTrim;
        float mStartingRotation;
        float mStartingStartTrim;
        float mStrokeWidth = 5.0f;
        final RectF mTempBounds = new RectF();

        Ring() {
            this.mPaint.setStrokeCap(Paint.Cap.SQUARE);
            this.mPaint.setAntiAlias(true);
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mArrowPaint.setStyle(Paint.Style.FILL);
            this.mArrowPaint.setAntiAlias(true);
            this.mCirclePaint.setColor(0);
        }

        void draw(Canvas canvas, Rect rect) {
            RectF rectF = this.mTempBounds;
            float f = this.mRingCenterRadius;
            float f2 = this.mStrokeWidth / 2.0f;
            f = f <= 0.0f ? (float)Math.min(rect.width(), rect.height()) / 2.0f - Math.max((float)this.mArrowWidth * this.mArrowScale / 2.0f, this.mStrokeWidth / 2.0f) : f2 + f;
            rectF.set((float)rect.centerX() - f, (float)rect.centerY() - f, (float)rect.centerX() + f, (float)rect.centerY() + f);
            f = this.mStartTrim;
            f2 = this.mRotation;
            f = (f + f2) * 360.0f;
            f2 = (this.mEndTrim + f2) * 360.0f - f;
            this.mPaint.setColor(this.mCurrentColor);
            this.mPaint.setAlpha(this.mAlpha);
            float f3 = this.mStrokeWidth / 2.0f;
            rectF.inset(f3, f3);
            canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.width() / 2.0f, this.mCirclePaint);
            rectF.inset(- f3, - f3);
            canvas.drawArc(rectF, f, f2, false, this.mPaint);
            this.drawTriangle(canvas, f, f2, rectF);
        }

        void drawTriangle(Canvas canvas, float f, float f2, RectF rectF) {
            if (this.mShowArrow) {
                Path path = this.mArrow;
                if (path == null) {
                    this.mArrow = new Path();
                    this.mArrow.setFillType(Path.FillType.EVEN_ODD);
                } else {
                    path.reset();
                }
                float f3 = Math.min(rectF.width(), rectF.height()) / 2.0f;
                float f4 = (float)this.mArrowWidth * this.mArrowScale / 2.0f;
                this.mArrow.moveTo(0.0f, 0.0f);
                this.mArrow.lineTo((float)this.mArrowWidth * this.mArrowScale, 0.0f);
                path = this.mArrow;
                float f5 = this.mArrowWidth;
                float f6 = this.mArrowScale;
                path.lineTo(f5 * f6 / 2.0f, (float)this.mArrowHeight * f6);
                this.mArrow.offset(rectF.centerX() + f3 - f4, rectF.centerY() + this.mStrokeWidth / 2.0f);
                this.mArrow.close();
                this.mArrowPaint.setColor(this.mCurrentColor);
                this.mArrowPaint.setAlpha(this.mAlpha);
                canvas.save();
                canvas.rotate(f + f2, rectF.centerX(), rectF.centerY());
                canvas.drawPath(this.mArrow, this.mArrowPaint);
                canvas.restore();
                return;
            }
        }

        int getAlpha() {
            return this.mAlpha;
        }

        float getArrowHeight() {
            return this.mArrowHeight;
        }

        float getArrowScale() {
            return this.mArrowScale;
        }

        float getArrowWidth() {
            return this.mArrowWidth;
        }

        int getBackgroundColor() {
            return this.mCirclePaint.getColor();
        }

        float getCenterRadius() {
            return this.mRingCenterRadius;
        }

        int[] getColors() {
            return this.mColors;
        }

        float getEndTrim() {
            return this.mEndTrim;
        }

        int getNextColor() {
            return this.mColors[this.getNextColorIndex()];
        }

        int getNextColorIndex() {
            return (this.mColorIndex + 1) % this.mColors.length;
        }

        float getRotation() {
            return this.mRotation;
        }

        boolean getShowArrow() {
            return this.mShowArrow;
        }

        float getStartTrim() {
            return this.mStartTrim;
        }

        int getStartingColor() {
            return this.mColors[this.mColorIndex];
        }

        float getStartingEndTrim() {
            return this.mStartingEndTrim;
        }

        float getStartingRotation() {
            return this.mStartingRotation;
        }

        float getStartingStartTrim() {
            return this.mStartingStartTrim;
        }

        Paint.Cap getStrokeCap() {
            return this.mPaint.getStrokeCap();
        }

        float getStrokeWidth() {
            return this.mStrokeWidth;
        }

        void goToNextColor() {
            this.setColorIndex(this.getNextColorIndex());
        }

        void resetOriginals() {
            this.mStartingStartTrim = 0.0f;
            this.mStartingEndTrim = 0.0f;
            this.mStartingRotation = 0.0f;
            this.setStartTrim(0.0f);
            this.setEndTrim(0.0f);
            this.setRotation(0.0f);
        }

        void setAlpha(int n) {
            this.mAlpha = n;
        }

        void setArrowDimensions(float f, float f2) {
            this.mArrowWidth = (int)f;
            this.mArrowHeight = (int)f2;
        }

        void setArrowScale(float f) {
            if (f != this.mArrowScale) {
                this.mArrowScale = f;
                return;
            }
        }

        void setBackgroundColor(int n) {
            this.mCirclePaint.setColor(n);
        }

        void setCenterRadius(float f) {
            this.mRingCenterRadius = f;
        }

        void setColor(int n) {
            this.mCurrentColor = n;
        }

        void setColorFilter(ColorFilter colorFilter) {
            this.mPaint.setColorFilter(colorFilter);
        }

        void setColorIndex(int n) {
            this.mColorIndex = n;
            this.mCurrentColor = this.mColors[this.mColorIndex];
        }

        void setColors(@NonNull int[] arrn) {
            this.mColors = arrn;
            this.setColorIndex(0);
        }

        void setEndTrim(float f) {
            this.mEndTrim = f;
        }

        void setRotation(float f) {
            this.mRotation = f;
        }

        void setShowArrow(boolean bl) {
            if (this.mShowArrow != bl) {
                this.mShowArrow = bl;
                return;
            }
        }

        void setStartTrim(float f) {
            this.mStartTrim = f;
        }

        void setStrokeCap(Paint.Cap cap) {
            this.mPaint.setStrokeCap(cap);
        }

        void setStrokeWidth(float f) {
            this.mStrokeWidth = f;
            this.mPaint.setStrokeWidth(f);
        }

        void storeOriginals() {
            this.mStartingStartTrim = this.mStartTrim;
            this.mStartingEndTrim = this.mEndTrim;
            this.mStartingRotation = this.mRotation;
        }
    }

}

