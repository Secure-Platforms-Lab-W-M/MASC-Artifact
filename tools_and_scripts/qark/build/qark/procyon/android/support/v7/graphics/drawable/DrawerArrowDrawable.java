// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.graphics.drawable;

import android.support.annotation.RestrictTo;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import android.graphics.ColorFilter;
import android.support.annotation.FloatRange;
import android.support.annotation.ColorInt;
import android.graphics.Rect;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.graphics.Canvas;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.support.v7.appcompat.R;
import android.graphics.Paint$Cap;
import android.graphics.Paint$Join;
import android.graphics.Paint$Style;
import android.content.Context;
import android.graphics.Path;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class DrawerArrowDrawable extends Drawable
{
    public static final int ARROW_DIRECTION_END = 3;
    public static final int ARROW_DIRECTION_LEFT = 0;
    public static final int ARROW_DIRECTION_RIGHT = 1;
    public static final int ARROW_DIRECTION_START = 2;
    private static final float ARROW_HEAD_ANGLE;
    private float mArrowHeadLength;
    private float mArrowShaftLength;
    private float mBarGap;
    private float mBarLength;
    private int mDirection;
    private float mMaxCutForBarSize;
    private final Paint mPaint;
    private final Path mPath;
    private float mProgress;
    private final int mSize;
    private boolean mSpin;
    private boolean mVerticalMirror;
    
    static {
        ARROW_HEAD_ANGLE = (float)Math.toRadians(45.0);
    }
    
    public DrawerArrowDrawable(final Context context) {
        this.mPaint = new Paint();
        this.mPath = new Path();
        this.mVerticalMirror = false;
        this.mDirection = 2;
        this.mPaint.setStyle(Paint$Style.STROKE);
        this.mPaint.setStrokeJoin(Paint$Join.MITER);
        this.mPaint.setStrokeCap(Paint$Cap.BUTT);
        this.mPaint.setAntiAlias(true);
        final TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes((AttributeSet)null, R.styleable.DrawerArrowToggle, R.attr.drawerArrowStyle, R.style.Base_Widget_AppCompat_DrawerArrowToggle);
        this.setColor(obtainStyledAttributes.getColor(R.styleable.DrawerArrowToggle_color, 0));
        this.setBarThickness(obtainStyledAttributes.getDimension(R.styleable.DrawerArrowToggle_thickness, 0.0f));
        this.setSpinEnabled(obtainStyledAttributes.getBoolean(R.styleable.DrawerArrowToggle_spinBars, true));
        this.setGapSize((float)Math.round(obtainStyledAttributes.getDimension(R.styleable.DrawerArrowToggle_gapBetweenBars, 0.0f)));
        this.mSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.DrawerArrowToggle_drawableSize, 0);
        this.mBarLength = (float)Math.round(obtainStyledAttributes.getDimension(R.styleable.DrawerArrowToggle_barLength, 0.0f));
        this.mArrowHeadLength = (float)Math.round(obtainStyledAttributes.getDimension(R.styleable.DrawerArrowToggle_arrowHeadLength, 0.0f));
        this.mArrowShaftLength = obtainStyledAttributes.getDimension(R.styleable.DrawerArrowToggle_arrowShaftLength, 0.0f);
        obtainStyledAttributes.recycle();
    }
    
    private static float lerp(final float n, final float n2, final float n3) {
        return (n2 - n) * n3 + n;
    }
    
    public void draw(final Canvas canvas) {
        final Rect bounds = this.getBounds();
        final int mDirection = this.mDirection;
        final boolean b = false;
        boolean b2 = false;
        if (mDirection != 3) {
            switch (mDirection) {
                default: {
                    if (DrawableCompat.getLayoutDirection(this) == 1) {
                        b2 = true;
                    }
                    break;
                }
                case 1: {
                    b2 = true;
                    break;
                }
                case 0: {
                    b2 = false;
                    break;
                }
            }
        }
        else {
            b2 = b;
            if (DrawableCompat.getLayoutDirection(this) == 0) {
                b2 = true;
            }
        }
        final float mArrowHeadLength = this.mArrowHeadLength;
        final float lerp = lerp(this.mBarLength, (float)Math.sqrt(mArrowHeadLength * mArrowHeadLength * 2.0f), this.mProgress);
        final float lerp2 = lerp(this.mBarLength, this.mArrowShaftLength, this.mProgress);
        final float n = (float)Math.round(lerp(0.0f, this.mMaxCutForBarSize, this.mProgress));
        final float lerp3 = lerp(0.0f, DrawerArrowDrawable.ARROW_HEAD_ANGLE, this.mProgress);
        float n2;
        if (b2) {
            n2 = 0.0f;
        }
        else {
            n2 = -180.0f;
        }
        float n3;
        if (b2) {
            n3 = 180.0f;
        }
        else {
            n3 = 0.0f;
        }
        final float lerp4 = lerp(n2, n3, this.mProgress);
        final double n4 = lerp;
        final double cos = Math.cos(lerp3);
        Double.isNaN(n4);
        final float n5 = (float)Math.round(n4 * cos);
        final double n6 = lerp;
        final double sin = Math.sin(lerp3);
        Double.isNaN(n6);
        final float n7 = (float)Math.round(n6 * sin);
        this.mPath.rewind();
        final float lerp5 = lerp(this.mBarGap + this.mPaint.getStrokeWidth(), -this.mMaxCutForBarSize, this.mProgress);
        final float n8 = -lerp2 / 2.0f;
        this.mPath.moveTo(n8 + n, 0.0f);
        this.mPath.rLineTo(lerp2 - n * 2.0f, 0.0f);
        this.mPath.moveTo(n8, lerp5);
        this.mPath.rLineTo(n5, n7);
        this.mPath.moveTo(n8, -lerp5);
        this.mPath.rLineTo(n5, -n7);
        this.mPath.close();
        canvas.save();
        final float strokeWidth = this.mPaint.getStrokeWidth();
        final float n9 = (float)bounds.height();
        final float mBarGap = this.mBarGap;
        canvas.translate((float)bounds.centerX(), (int)(n9 - 3.0f * strokeWidth - 2.0f * mBarGap) / 4 * 2 + (1.5f * strokeWidth + mBarGap));
        if (this.mSpin) {
            int n10;
            if (this.mVerticalMirror ^ b2) {
                n10 = -1;
            }
            else {
                n10 = 1;
            }
            canvas.rotate(n10 * lerp4);
        }
        else if (b2) {
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
    
    @ColorInt
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
    
    @FloatRange(from = 0.0, to = 1.0)
    public float getProgress() {
        return this.mProgress;
    }
    
    public boolean isSpinEnabled() {
        return this.mSpin;
    }
    
    public void setAlpha(final int alpha) {
        if (alpha != this.mPaint.getAlpha()) {
            this.mPaint.setAlpha(alpha);
            this.invalidateSelf();
        }
    }
    
    public void setArrowHeadLength(final float mArrowHeadLength) {
        if (this.mArrowHeadLength != mArrowHeadLength) {
            this.mArrowHeadLength = mArrowHeadLength;
            this.invalidateSelf();
        }
    }
    
    public void setArrowShaftLength(final float mArrowShaftLength) {
        if (this.mArrowShaftLength != mArrowShaftLength) {
            this.mArrowShaftLength = mArrowShaftLength;
            this.invalidateSelf();
        }
    }
    
    public void setBarLength(final float mBarLength) {
        if (this.mBarLength != mBarLength) {
            this.mBarLength = mBarLength;
            this.invalidateSelf();
        }
    }
    
    public void setBarThickness(final float strokeWidth) {
        if (this.mPaint.getStrokeWidth() != strokeWidth) {
            this.mPaint.setStrokeWidth(strokeWidth);
            final double n = strokeWidth / 2.0f;
            final double cos = Math.cos(DrawerArrowDrawable.ARROW_HEAD_ANGLE);
            Double.isNaN(n);
            this.mMaxCutForBarSize = (float)(n * cos);
            this.invalidateSelf();
        }
    }
    
    public void setColor(@ColorInt final int color) {
        if (color != this.mPaint.getColor()) {
            this.mPaint.setColor(color);
            this.invalidateSelf();
        }
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
        this.invalidateSelf();
    }
    
    public void setDirection(final int mDirection) {
        if (mDirection != this.mDirection) {
            this.mDirection = mDirection;
            this.invalidateSelf();
        }
    }
    
    public void setGapSize(final float mBarGap) {
        if (mBarGap != this.mBarGap) {
            this.mBarGap = mBarGap;
            this.invalidateSelf();
        }
    }
    
    public void setProgress(@FloatRange(from = 0.0, to = 1.0) final float mProgress) {
        if (this.mProgress != mProgress) {
            this.mProgress = mProgress;
            this.invalidateSelf();
        }
    }
    
    public void setSpinEnabled(final boolean mSpin) {
        if (this.mSpin != mSpin) {
            this.mSpin = mSpin;
            this.invalidateSelf();
        }
    }
    
    public void setVerticalMirror(final boolean mVerticalMirror) {
        if (this.mVerticalMirror != mVerticalMirror) {
            this.mVerticalMirror = mVerticalMirror;
            this.invalidateSelf();
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface ArrowDirection {
    }
}
