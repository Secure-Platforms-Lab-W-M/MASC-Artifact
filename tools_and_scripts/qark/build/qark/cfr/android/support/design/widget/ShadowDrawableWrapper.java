/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Canvas
 *  android.graphics.LinearGradient
 *  android.graphics.Paint
 *  android.graphics.Paint$Style
 *  android.graphics.Path
 *  android.graphics.Path$FillType
 *  android.graphics.RadialGradient
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  android.graphics.Shader
 *  android.graphics.Shader$TileMode
 *  android.graphics.drawable.Drawable
 */
package android.support.design.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.design.R;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.drawable.DrawableWrapper;

class ShadowDrawableWrapper
extends DrawableWrapper {
    static final double COS_45 = Math.cos(Math.toRadians(45.0));
    static final float SHADOW_BOTTOM_SCALE = 1.0f;
    static final float SHADOW_HORIZ_SCALE = 0.5f;
    static final float SHADOW_MULTIPLIER = 1.5f;
    static final float SHADOW_TOP_SCALE = 0.25f;
    private boolean mAddPaddingForCorners = true;
    final RectF mContentBounds;
    float mCornerRadius;
    final Paint mCornerShadowPaint;
    Path mCornerShadowPath;
    private boolean mDirty = true;
    final Paint mEdgeShadowPaint;
    float mMaxShadowSize;
    private boolean mPrintedShadowClipWarning = false;
    float mRawMaxShadowSize;
    float mRawShadowSize;
    private float mRotation;
    private final int mShadowEndColor;
    private final int mShadowMiddleColor;
    float mShadowSize;
    private final int mShadowStartColor;

    public ShadowDrawableWrapper(Context context, Drawable drawable2, float f, float f2, float f3) {
        super(drawable2);
        this.mShadowStartColor = ContextCompat.getColor(context, R.color.design_fab_shadow_start_color);
        this.mShadowMiddleColor = ContextCompat.getColor(context, R.color.design_fab_shadow_mid_color);
        this.mShadowEndColor = ContextCompat.getColor(context, R.color.design_fab_shadow_end_color);
        this.mCornerShadowPaint = new Paint(5);
        this.mCornerShadowPaint.setStyle(Paint.Style.FILL);
        this.mCornerRadius = Math.round(f);
        this.mContentBounds = new RectF();
        this.mEdgeShadowPaint = new Paint(this.mCornerShadowPaint);
        this.mEdgeShadowPaint.setAntiAlias(false);
        this.setShadowSize(f2, f3);
    }

    private void buildComponents(Rect rect) {
        float f = this.mRawMaxShadowSize * 1.5f;
        this.mContentBounds.set((float)rect.left + this.mRawMaxShadowSize, (float)rect.top + f, (float)rect.right - this.mRawMaxShadowSize, (float)rect.bottom - f);
        this.getWrappedDrawable().setBounds((int)this.mContentBounds.left, (int)this.mContentBounds.top, (int)this.mContentBounds.right, (int)this.mContentBounds.bottom);
        this.buildShadowCorners();
    }

    private void buildShadowCorners() {
        int n;
        int n2;
        int n3;
        float f;
        float f2 = this.mCornerRadius;
        RectF rectF = new RectF(- f2, - f2, f2, f2);
        RectF rectF2 = new RectF(rectF);
        f2 = this.mShadowSize;
        rectF2.inset(- f2, - f2);
        Path path = this.mCornerShadowPath;
        if (path == null) {
            this.mCornerShadowPath = new Path();
        } else {
            path.reset();
        }
        this.mCornerShadowPath.setFillType(Path.FillType.EVEN_ODD);
        this.mCornerShadowPath.moveTo(- this.mCornerRadius, 0.0f);
        this.mCornerShadowPath.rLineTo(- this.mShadowSize, 0.0f);
        this.mCornerShadowPath.arcTo(rectF2, 180.0f, 90.0f, false);
        this.mCornerShadowPath.arcTo(rectF, 270.0f, -90.0f, false);
        this.mCornerShadowPath.close();
        f2 = - rectF2.top;
        if (f2 > 0.0f) {
            f = this.mCornerRadius / f2;
            float f3 = (1.0f - f) / 2.0f;
            path = this.mCornerShadowPaint;
            n2 = this.mShadowStartColor;
            n = this.mShadowMiddleColor;
            n3 = this.mShadowEndColor;
            Shader.TileMode tileMode = Shader.TileMode.CLAMP;
            path.setShader((Shader)new RadialGradient(0.0f, 0.0f, f2, new int[]{0, n2, n, n3}, new float[]{0.0f, f, f + f3, 1.0f}, tileMode));
        }
        path = this.mEdgeShadowPaint;
        f2 = rectF.top;
        f = rectF2.top;
        n2 = this.mShadowStartColor;
        n = this.mShadowMiddleColor;
        n3 = this.mShadowEndColor;
        rectF = Shader.TileMode.CLAMP;
        path.setShader((Shader)new LinearGradient(0.0f, f2, 0.0f, f, new int[]{n2, n, n3}, new float[]{0.0f, 0.5f, 1.0f}, (Shader.TileMode)rectF));
        this.mEdgeShadowPaint.setAntiAlias(false);
    }

    public static float calculateHorizontalPadding(float f, float f2, boolean bl) {
        if (bl) {
            double d = f;
            double d2 = COS_45;
            double d3 = f2;
            Double.isNaN(d3);
            Double.isNaN(d);
            return (float)(d + (1.0 - d2) * d3);
        }
        return f;
    }

    public static float calculateVerticalPadding(float f, float f2, boolean bl) {
        if (bl) {
            double d = 1.5f * f;
            double d2 = COS_45;
            double d3 = f2;
            Double.isNaN(d3);
            Double.isNaN(d);
            return (float)(d + (1.0 - d2) * d3);
        }
        return 1.5f * f;
    }

    private void drawShadow(Canvas canvas) {
        int n = canvas.save();
        canvas.rotate(this.mRotation, this.mContentBounds.centerX(), this.mContentBounds.centerY());
        float f = - this.mCornerRadius - this.mShadowSize;
        float f2 = this.mCornerRadius;
        float f3 = this.mContentBounds.width();
        boolean bl = true;
        int n2 = f3 - f2 * 2.0f > 0.0f ? 1 : 0;
        if (this.mContentBounds.height() - f2 * 2.0f <= 0.0f) {
            bl = false;
        }
        float f4 = this.mRawShadowSize;
        f3 = f2 / (f2 + (f4 - 0.5f * f4));
        float f5 = f2 / (f2 + (f4 - 0.25f * f4));
        f4 = f2 / (f2 + (f4 - f4 * 1.0f));
        int n3 = canvas.save();
        canvas.translate(this.mContentBounds.left + f2, this.mContentBounds.top + f2);
        canvas.scale(f3, f5);
        canvas.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
        if (n2 != 0) {
            canvas.scale(1.0f / f3, 1.0f);
            canvas.drawRect(0.0f, f, this.mContentBounds.width() - f2 * 2.0f, - this.mCornerRadius, this.mEdgeShadowPaint);
        }
        canvas.restoreToCount(n3);
        n3 = canvas.save();
        canvas.translate(this.mContentBounds.right - f2, this.mContentBounds.bottom - f2);
        canvas.scale(f3, f4);
        canvas.rotate(180.0f);
        canvas.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
        if (n2 != 0) {
            canvas.scale(1.0f / f3, 1.0f);
            canvas.drawRect(0.0f, f, this.mContentBounds.width() - f2 * 2.0f, - this.mCornerRadius + this.mShadowSize, this.mEdgeShadowPaint);
        }
        canvas.restoreToCount(n3);
        n2 = canvas.save();
        canvas.translate(this.mContentBounds.left + f2, this.mContentBounds.bottom - f2);
        canvas.scale(f3, f4);
        canvas.rotate(270.0f);
        canvas.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
        if (bl) {
            canvas.scale(1.0f / f4, 1.0f);
            canvas.drawRect(0.0f, f, this.mContentBounds.height() - f2 * 2.0f, - this.mCornerRadius, this.mEdgeShadowPaint);
        }
        canvas.restoreToCount(n2);
        n2 = canvas.save();
        canvas.translate(this.mContentBounds.right - f2, this.mContentBounds.top + f2);
        canvas.scale(f3, f5);
        canvas.rotate(90.0f);
        canvas.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
        if (bl) {
            canvas.scale(1.0f / f5, 1.0f);
            canvas.drawRect(0.0f, f, this.mContentBounds.height() - 2.0f * f2, - this.mCornerRadius, this.mEdgeShadowPaint);
        }
        canvas.restoreToCount(n2);
        canvas.restoreToCount(n);
    }

    private static int toEven(float f) {
        int n = Math.round(f);
        if (n % 2 == 1) {
            return n - 1;
        }
        return n;
    }

    @Override
    public void draw(Canvas canvas) {
        if (this.mDirty) {
            this.buildComponents(this.getBounds());
            this.mDirty = false;
        }
        this.drawShadow(canvas);
        super.draw(canvas);
    }

    public float getCornerRadius() {
        return this.mCornerRadius;
    }

    public float getMaxShadowSize() {
        return this.mRawMaxShadowSize;
    }

    public float getMinHeight() {
        float f = this.mRawMaxShadowSize;
        f = Math.max(f, this.mCornerRadius + f * 1.5f / 2.0f);
        return this.mRawMaxShadowSize * 1.5f * 2.0f + f * 2.0f;
    }

    public float getMinWidth() {
        float f = this.mRawMaxShadowSize;
        f = Math.max(f, this.mCornerRadius + f / 2.0f);
        return this.mRawMaxShadowSize * 2.0f + f * 2.0f;
    }

    @Override
    public int getOpacity() {
        return -3;
    }

    @Override
    public boolean getPadding(Rect rect) {
        int n = (int)Math.ceil(ShadowDrawableWrapper.calculateVerticalPadding(this.mRawMaxShadowSize, this.mCornerRadius, this.mAddPaddingForCorners));
        int n2 = (int)Math.ceil(ShadowDrawableWrapper.calculateHorizontalPadding(this.mRawMaxShadowSize, this.mCornerRadius, this.mAddPaddingForCorners));
        rect.set(n2, n, n2, n);
        return true;
    }

    public float getShadowSize() {
        return this.mRawShadowSize;
    }

    @Override
    protected void onBoundsChange(Rect rect) {
        this.mDirty = true;
    }

    public void setAddPaddingForCorners(boolean bl) {
        this.mAddPaddingForCorners = bl;
        this.invalidateSelf();
    }

    @Override
    public void setAlpha(int n) {
        super.setAlpha(n);
        this.mCornerShadowPaint.setAlpha(n);
        this.mEdgeShadowPaint.setAlpha(n);
    }

    public void setCornerRadius(float f) {
        if (this.mCornerRadius == (f = (float)Math.round(f))) {
            return;
        }
        this.mCornerRadius = f;
        this.mDirty = true;
        this.invalidateSelf();
    }

    public void setMaxShadowSize(float f) {
        this.setShadowSize(this.mRawShadowSize, f);
    }

    final void setRotation(float f) {
        if (this.mRotation != f) {
            this.mRotation = f;
            this.invalidateSelf();
            return;
        }
    }

    public void setShadowSize(float f) {
        this.setShadowSize(f, this.mRawMaxShadowSize);
    }

    void setShadowSize(float f, float f2) {
        if (f >= 0.0f && f2 >= 0.0f) {
            if ((f = (float)ShadowDrawableWrapper.toEven(f)) > (f2 = (float)ShadowDrawableWrapper.toEven(f2))) {
                f = f2;
                if (!this.mPrintedShadowClipWarning) {
                    this.mPrintedShadowClipWarning = true;
                }
            }
            if (this.mRawShadowSize == f && this.mRawMaxShadowSize == f2) {
                return;
            }
            this.mRawShadowSize = f;
            this.mRawMaxShadowSize = f2;
            this.mShadowSize = Math.round(1.5f * f);
            this.mMaxShadowSize = f2;
            this.mDirty = true;
            this.invalidateSelf();
            return;
        }
        throw new IllegalArgumentException("invalid shadow size");
    }
}

