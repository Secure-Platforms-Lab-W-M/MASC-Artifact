/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.content.res.TypedArray
 *  android.graphics.Canvas
 *  android.graphics.ColorFilter
 *  android.graphics.Paint
 *  android.graphics.Paint$Cap
 *  android.graphics.Paint$Join
 *  android.graphics.Paint$Style
 *  android.graphics.Path
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  androidx.appcompat.R
 *  androidx.appcompat.R$attr
 *  androidx.appcompat.R$style
 *  androidx.appcompat.R$styleable
 */
package androidx.appcompat.graphics.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import androidx.appcompat.R;
import androidx.core.graphics.drawable.DrawableCompat;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DrawerArrowDrawable
extends Drawable {
    public static final int ARROW_DIRECTION_END = 3;
    public static final int ARROW_DIRECTION_LEFT = 0;
    public static final int ARROW_DIRECTION_RIGHT = 1;
    public static final int ARROW_DIRECTION_START = 2;
    private static final float ARROW_HEAD_ANGLE = (float)Math.toRadians(45.0);
    private float mArrowHeadLength;
    private float mArrowShaftLength;
    private float mBarGap;
    private float mBarLength;
    private int mDirection = 2;
    private float mMaxCutForBarSize;
    private final Paint mPaint = new Paint();
    private final Path mPath = new Path();
    private float mProgress;
    private final int mSize;
    private boolean mSpin;
    private boolean mVerticalMirror = false;

    public DrawerArrowDrawable(Context context) {
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeJoin(Paint.Join.MITER);
        this.mPaint.setStrokeCap(Paint.Cap.BUTT);
        this.mPaint.setAntiAlias(true);
        context = context.getTheme().obtainStyledAttributes(null, R.styleable.DrawerArrowToggle, R.attr.drawerArrowStyle, R.style.Base_Widget_AppCompat_DrawerArrowToggle);
        this.setColor(context.getColor(R.styleable.DrawerArrowToggle_color, 0));
        this.setBarThickness(context.getDimension(R.styleable.DrawerArrowToggle_thickness, 0.0f));
        this.setSpinEnabled(context.getBoolean(R.styleable.DrawerArrowToggle_spinBars, true));
        this.setGapSize(Math.round(context.getDimension(R.styleable.DrawerArrowToggle_gapBetweenBars, 0.0f)));
        this.mSize = context.getDimensionPixelSize(R.styleable.DrawerArrowToggle_drawableSize, 0);
        this.mBarLength = Math.round(context.getDimension(R.styleable.DrawerArrowToggle_barLength, 0.0f));
        this.mArrowHeadLength = Math.round(context.getDimension(R.styleable.DrawerArrowToggle_arrowHeadLength, 0.0f));
        this.mArrowShaftLength = context.getDimension(R.styleable.DrawerArrowToggle_arrowShaftLength, 0.0f);
        context.recycle();
    }

    private static float lerp(float f, float f2, float f3) {
        return (f2 - f) * f3 + f;
    }

    public void draw(Canvas canvas) {
        int n;
        Rect rect = this.getBounds();
        int n2 = this.mDirection;
        if (n2 != 0) {
            if (n2 != 1) {
                int n3 = 0;
                n = 0;
                if (n2 != 3) {
                    if (DrawableCompat.getLayoutDirection(this) == 1) {
                        n = 1;
                    }
                } else {
                    n = n3;
                    if (DrawableCompat.getLayoutDirection(this) == 0) {
                        n = 1;
                    }
                }
            } else {
                n = 1;
            }
        } else {
            n = 0;
        }
        float f = this.mArrowHeadLength;
        f = (float)Math.sqrt(f * f * 2.0f);
        float f2 = DrawerArrowDrawable.lerp(this.mBarLength, f, this.mProgress);
        float f3 = DrawerArrowDrawable.lerp(this.mBarLength, this.mArrowShaftLength, this.mProgress);
        float f4 = Math.round(DrawerArrowDrawable.lerp(0.0f, this.mMaxCutForBarSize, this.mProgress));
        float f5 = DrawerArrowDrawable.lerp(0.0f, ARROW_HEAD_ANGLE, this.mProgress);
        f = n != 0 ? 0.0f : -180.0f;
        float f6 = n != 0 ? 180.0f : 0.0f;
        f = DrawerArrowDrawable.lerp(f, f6, this.mProgress);
        f6 = Math.round((double)f2 * Math.cos(f5));
        f2 = Math.round((double)f2 * Math.sin(f5));
        this.mPath.rewind();
        f5 = DrawerArrowDrawable.lerp(this.mBarGap + this.mPaint.getStrokeWidth(), - this.mMaxCutForBarSize, this.mProgress);
        float f7 = (- f3) / 2.0f;
        this.mPath.moveTo(f7 + f4, 0.0f);
        this.mPath.rLineTo(f3 - f4 * 2.0f, 0.0f);
        this.mPath.moveTo(f7, f5);
        this.mPath.rLineTo(f6, f2);
        this.mPath.moveTo(f7, - f5);
        this.mPath.rLineTo(f6, - f2);
        this.mPath.close();
        canvas.save();
        f6 = this.mPaint.getStrokeWidth();
        f4 = rect.height();
        f3 = this.mBarGap;
        f4 = (int)(f4 - 3.0f * f6 - 2.0f * f3) / 4 * 2;
        canvas.translate((float)rect.centerX(), f4 + (1.5f * f6 + f3));
        if (this.mSpin) {
            n = this.mVerticalMirror ^ n ? -1 : 1;
            canvas.rotate((float)n * f);
        } else if (n != 0) {
            canvas.rotate(180.0f);
        }
        canvas.drawPath(this.mPath, this.mPaint);
        canvas.restore();
    }

    public float getArrowHeadLength() {
        return this.mArrowHeadLength;
    }

    public float getArrowShaftLength() {
        return this.mArrowShaftLength;
    }

    public float getBarLength() {
        return this.mBarLength;
    }

    public float getBarThickness() {
        return this.mPaint.getStrokeWidth();
    }

    public int getColor() {
        return this.mPaint.getColor();
    }

    public int getDirection() {
        return this.mDirection;
    }

    public float getGapSize() {
        return this.mBarGap;
    }

    public int getIntrinsicHeight() {
        return this.mSize;
    }

    public int getIntrinsicWidth() {
        return this.mSize;
    }

    public int getOpacity() {
        return -3;
    }

    public final Paint getPaint() {
        return this.mPaint;
    }

    public float getProgress() {
        return this.mProgress;
    }

    public boolean isSpinEnabled() {
        return this.mSpin;
    }

    public void setAlpha(int n) {
        if (n != this.mPaint.getAlpha()) {
            this.mPaint.setAlpha(n);
            this.invalidateSelf();
        }
    }

    public void setArrowHeadLength(float f) {
        if (this.mArrowHeadLength != f) {
            this.mArrowHeadLength = f;
            this.invalidateSelf();
        }
    }

    public void setArrowShaftLength(float f) {
        if (this.mArrowShaftLength != f) {
            this.mArrowShaftLength = f;
            this.invalidateSelf();
        }
    }

    public void setBarLength(float f) {
        if (this.mBarLength != f) {
            this.mBarLength = f;
            this.invalidateSelf();
        }
    }

    public void setBarThickness(float f) {
        if (this.mPaint.getStrokeWidth() != f) {
            this.mPaint.setStrokeWidth(f);
            this.mMaxCutForBarSize = (float)((double)(f / 2.0f) * Math.cos(ARROW_HEAD_ANGLE));
            this.invalidateSelf();
        }
    }

    public void setColor(int n) {
        if (n != this.mPaint.getColor()) {
            this.mPaint.setColor(n);
            this.invalidateSelf();
        }
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
        this.invalidateSelf();
    }

    public void setDirection(int n) {
        if (n != this.mDirection) {
            this.mDirection = n;
            this.invalidateSelf();
        }
    }

    public void setGapSize(float f) {
        if (f != this.mBarGap) {
            this.mBarGap = f;
            this.invalidateSelf();
        }
    }

    public void setProgress(float f) {
        if (this.mProgress != f) {
            this.mProgress = f;
            this.invalidateSelf();
        }
    }

    public void setSpinEnabled(boolean bl) {
        if (this.mSpin != bl) {
            this.mSpin = bl;
            this.invalidateSelf();
        }
    }

    public void setVerticalMirror(boolean bl) {
        if (this.mVerticalMirror != bl) {
            this.mVerticalMirror = bl;
            this.invalidateSelf();
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface ArrowDirection {
    }

}

