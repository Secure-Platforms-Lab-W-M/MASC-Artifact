// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.appcompat.graphics.drawable;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import androidx.core.graphics.drawable.DrawableCompat;
import android.graphics.Canvas;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.appcompat.R$style;
import androidx.appcompat.R$attr;
import androidx.appcompat.R$styleable;
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
        final TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes((AttributeSet)null, R$styleable.DrawerArrowToggle, R$attr.drawerArrowStyle, R$style.Base_Widget_AppCompat_DrawerArrowToggle);
        this.setColor(obtainStyledAttributes.getColor(R$styleable.DrawerArrowToggle_color, 0));
        this.setBarThickness(obtainStyledAttributes.getDimension(R$styleable.DrawerArrowToggle_thickness, 0.0f));
        this.setSpinEnabled(obtainStyledAttributes.getBoolean(R$styleable.DrawerArrowToggle_spinBars, true));
        this.setGapSize((float)Math.round(obtainStyledAttributes.getDimension(R$styleable.DrawerArrowToggle_gapBetweenBars, 0.0f)));
        this.mSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.DrawerArrowToggle_drawableSize, 0);
        this.mBarLength = (float)Math.round(obtainStyledAttributes.getDimension(R$styleable.DrawerArrowToggle_barLength, 0.0f));
        this.mArrowHeadLength = (float)Math.round(obtainStyledAttributes.getDimension(R$styleable.DrawerArrowToggle_arrowHeadLength, 0.0f));
        this.mArrowShaftLength = obtainStyledAttributes.getDimension(R$styleable.DrawerArrowToggle_arrowShaftLength, 0.0f);
        obtainStyledAttributes.recycle();
    }
    
    private static float lerp(final float n, final float n2, final float n3) {
        return (n2 - n) * n3 + n;
    }
    
    public void draw(final Canvas canvas) {
        final Rect bounds = this.getBounds();
        final int mDirection = this.mDirection;
        boolean b2;
        if (mDirection != 0) {
            if (mDirection != 1) {
                final boolean b = false;
                b2 = false;
                if (mDirection != 3) {
                    if (DrawableCompat.getLayoutDirection(this) == 1) {
                        b2 = true;
                    }
                }
                else {
                    b2 = b;
                    if (DrawableCompat.getLayoutDirection(this) == 0) {
                        b2 = true;
                    }
                }
            }
            else {
                b2 = true;
            }
        }
        else {
            b2 = false;
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
        final float n4 = (float)Math.round(lerp * Math.cos(lerp3));
        final float n5 = (float)Math.round(lerp * Math.sin(lerp3));
        this.mPath.rewind();
        final float lerp5 = lerp(this.mBarGap + this.mPaint.getStrokeWidth(), -this.mMaxCutForBarSize, this.mProgress);
        final float n6 = -lerp2 / 2.0f;
        this.mPath.moveTo(n6 + n, 0.0f);
        this.mPath.rLineTo(lerp2 - n * 2.0f, 0.0f);
        this.mPath.moveTo(n6, lerp5);
        this.mPath.rLineTo(n4, n5);
        this.mPath.moveTo(n6, -lerp5);
        this.mPath.rLineTo(n4, -n5);
        this.mPath.close();
        canvas.save();
        final float strokeWidth = this.mPaint.getStrokeWidth();
        final float n7 = (float)bounds.height();
        final float mBarGap = this.mBarGap;
        canvas.translate((float)bounds.centerX(), (int)(n7 - 3.0f * strokeWidth - 2.0f * mBarGap) / 4 * 2 + (1.5f * strokeWidth + mBarGap));
        if (this.mSpin) {
            int n8;
            if (this.mVerticalMirror ^ b2) {
                n8 = -1;
            }
            else {
                n8 = 1;
            }
            canvas.rotate(n8 * lerp4);
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
            this.mMaxCutForBarSize = (float)(strokeWidth / 2.0f * Math.cos(DrawerArrowDrawable.ARROW_HEAD_ANGLE));
            this.invalidateSelf();
        }
    }
    
    public void setColor(final int color) {
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
    
    public void setProgress(final float mProgress) {
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
    public @interface ArrowDirection {
    }
}
