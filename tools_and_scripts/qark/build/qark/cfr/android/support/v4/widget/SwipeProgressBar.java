/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  android.view.View
 *  android.view.animation.AnimationUtils
 *  android.view.animation.Interpolator
 */
package android.support.v4.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

final class SwipeProgressBar {
    private static final int ANIMATION_DURATION_MS = 2000;
    private static final int COLOR1 = -1291845632;
    private static final int COLOR2 = Integer.MIN_VALUE;
    private static final int COLOR3 = 1291845632;
    private static final int COLOR4 = 436207616;
    private static final int FINISH_ANIMATION_DURATION_MS = 1000;
    private static final Interpolator INTERPOLATOR = new FastOutSlowInInterpolator();
    private Rect mBounds = new Rect();
    private final RectF mClipRect = new RectF();
    private int mColor1;
    private int mColor2;
    private int mColor3;
    private int mColor4;
    private long mFinishTime;
    private final Paint mPaint = new Paint();
    private View mParent;
    private boolean mRunning;
    private long mStartTime;
    private float mTriggerPercentage;

    SwipeProgressBar(View view) {
        this.mParent = view;
        this.mColor1 = -1291845632;
        this.mColor2 = Integer.MIN_VALUE;
        this.mColor3 = 1291845632;
        this.mColor4 = 436207616;
    }

    private void drawCircle(Canvas canvas, float f, float f2, int n, float f3) {
        this.mPaint.setColor(n);
        canvas.save();
        canvas.translate(f, f2);
        f2 = INTERPOLATOR.getInterpolation(f3);
        canvas.scale(f2, f2);
        canvas.drawCircle(0.0f, 0.0f, f, this.mPaint);
        canvas.restore();
    }

    private void drawTrigger(Canvas canvas, int n, int n2) {
        this.mPaint.setColor(this.mColor1);
        canvas.drawCircle((float)n, (float)n2, (float)n * this.mTriggerPercentage, this.mPaint);
    }

    void draw(Canvas canvas) {
        int n = this.mBounds.width();
        int n2 = this.mBounds.height();
        int n3 = n / 2;
        int n4 = n2 / 2;
        int n5 = canvas.save();
        canvas.clipRect(this.mBounds);
        if (!this.mRunning && this.mFinishTime <= 0L) {
            float f = this.mTriggerPercentage;
            if (f > 0.0f && (double)f <= 1.0) {
                this.drawTrigger(canvas, n3, n4);
            }
        } else {
            float f;
            long l = AnimationUtils.currentAnimationTimeMillis();
            long l2 = this.mStartTime;
            long l3 = (l - l2) / 2000L;
            float f2 = (float)((l - l2) % 2000L) / 20.0f;
            if (!this.mRunning) {
                l2 = this.mFinishTime;
                if (l - l2 >= 1000L) {
                    this.mFinishTime = 0L;
                    return;
                }
                f = (float)((l - l2) % 1000L) / 10.0f / 100.0f;
                f = (float)(n / 2) * INTERPOLATOR.getInterpolation(f);
                this.mClipRect.set((float)n3 - f, 0.0f, (float)n3 + f, (float)n2);
                canvas.saveLayerAlpha(this.mClipRect, 0, 0);
                n = 1;
            } else {
                n = 0;
            }
            if (l3 == 0L) {
                canvas.drawColor(this.mColor1);
            } else if (f2 >= 0.0f && f2 < 25.0f) {
                canvas.drawColor(this.mColor4);
            } else if (f2 >= 25.0f && f2 < 50.0f) {
                canvas.drawColor(this.mColor1);
            } else if (f2 >= 50.0f && f2 < 75.0f) {
                canvas.drawColor(this.mColor2);
            } else {
                canvas.drawColor(this.mColor3);
            }
            if (f2 >= 0.0f && f2 <= 25.0f) {
                f = (f2 + 25.0f) * 2.0f / 100.0f;
                this.drawCircle(canvas, n3, n4, this.mColor1, f);
            }
            if (f2 >= 0.0f && f2 <= 50.0f) {
                f = f2 * 2.0f / 100.0f;
                this.drawCircle(canvas, n3, n4, this.mColor2, f);
            }
            if (f2 >= 25.0f && f2 <= 75.0f) {
                f = (f2 - 25.0f) * 2.0f / 100.0f;
                this.drawCircle(canvas, n3, n4, this.mColor3, f);
            }
            if (f2 >= 50.0f && f2 <= 100.0f) {
                f = (f2 - 50.0f) * 2.0f / 100.0f;
                this.drawCircle(canvas, n3, n4, this.mColor4, f);
            }
            if (f2 >= 75.0f && f2 <= 100.0f) {
                f2 = (f2 - 75.0f) * 2.0f / 100.0f;
                this.drawCircle(canvas, n3, n4, this.mColor1, f2);
            }
            if (this.mTriggerPercentage > 0.0f && n != 0) {
                canvas.restoreToCount(n5);
                n5 = canvas.save();
                canvas.clipRect(this.mBounds);
                this.drawTrigger(canvas, n3, n4);
            }
            ViewCompat.postInvalidateOnAnimation(this.mParent, this.mBounds.left, this.mBounds.top, this.mBounds.right, this.mBounds.bottom);
        }
        canvas.restoreToCount(n5);
    }

    boolean isRunning() {
        if (!this.mRunning && this.mFinishTime <= 0L) {
            return false;
        }
        return true;
    }

    void setBounds(int n, int n2, int n3, int n4) {
        Rect rect = this.mBounds;
        rect.left = n;
        rect.top = n2;
        rect.right = n3;
        rect.bottom = n4;
    }

    void setColorScheme(int n, int n2, int n3, int n4) {
        this.mColor1 = n;
        this.mColor2 = n2;
        this.mColor3 = n3;
        this.mColor4 = n4;
    }

    void setTriggerPercentage(float f) {
        this.mTriggerPercentage = f;
        this.mStartTime = 0L;
        ViewCompat.postInvalidateOnAnimation(this.mParent, this.mBounds.left, this.mBounds.top, this.mBounds.right, this.mBounds.bottom);
    }

    void start() {
        if (!this.mRunning) {
            this.mTriggerPercentage = 0.0f;
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mRunning = true;
            this.mParent.postInvalidate();
            return;
        }
    }

    void stop() {
        if (this.mRunning) {
            this.mTriggerPercentage = 0.0f;
            this.mFinishTime = AnimationUtils.currentAnimationTimeMillis();
            this.mRunning = false;
            this.mParent.postInvalidate();
            return;
        }
    }
}

