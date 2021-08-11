// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.design.widget;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.RadialGradient;
import android.graphics.Shader$TileMode;
import android.graphics.Path$FillType;
import android.graphics.Rect;
import android.graphics.Paint$Style;
import android.support.v4.content.ContextCompat;
import android.support.design.R;
import android.graphics.drawable.Drawable;
import android.content.Context;
import android.graphics.Path;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.graphics.drawable.DrawableWrapper;

class ShadowDrawableWrapper extends DrawableWrapper
{
    static final double COS_45;
    static final float SHADOW_BOTTOM_SCALE = 1.0f;
    static final float SHADOW_HORIZ_SCALE = 0.5f;
    static final float SHADOW_MULTIPLIER = 1.5f;
    static final float SHADOW_TOP_SCALE = 0.25f;
    private boolean mAddPaddingForCorners;
    final RectF mContentBounds;
    float mCornerRadius;
    final Paint mCornerShadowPaint;
    Path mCornerShadowPath;
    private boolean mDirty;
    final Paint mEdgeShadowPaint;
    float mMaxShadowSize;
    private boolean mPrintedShadowClipWarning;
    float mRawMaxShadowSize;
    float mRawShadowSize;
    private float mRotation;
    private final int mShadowEndColor;
    private final int mShadowMiddleColor;
    float mShadowSize;
    private final int mShadowStartColor;
    
    static {
        COS_45 = Math.cos(Math.toRadians(45.0));
    }
    
    public ShadowDrawableWrapper(final Context context, final Drawable drawable, final float n, final float n2, final float n3) {
        super(drawable);
        this.mDirty = true;
        this.mAddPaddingForCorners = true;
        this.mPrintedShadowClipWarning = false;
        this.mShadowStartColor = ContextCompat.getColor(context, R.color.design_fab_shadow_start_color);
        this.mShadowMiddleColor = ContextCompat.getColor(context, R.color.design_fab_shadow_mid_color);
        this.mShadowEndColor = ContextCompat.getColor(context, R.color.design_fab_shadow_end_color);
        (this.mCornerShadowPaint = new Paint(5)).setStyle(Paint$Style.FILL);
        this.mCornerRadius = (float)Math.round(n);
        this.mContentBounds = new RectF();
        (this.mEdgeShadowPaint = new Paint(this.mCornerShadowPaint)).setAntiAlias(false);
        this.setShadowSize(n2, n3);
    }
    
    private void buildComponents(final Rect rect) {
        final float n = this.mRawMaxShadowSize * 1.5f;
        this.mContentBounds.set(rect.left + this.mRawMaxShadowSize, rect.top + n, rect.right - this.mRawMaxShadowSize, rect.bottom - n);
        this.getWrappedDrawable().setBounds((int)this.mContentBounds.left, (int)this.mContentBounds.top, (int)this.mContentBounds.right, (int)this.mContentBounds.bottom);
        this.buildShadowCorners();
    }
    
    private void buildShadowCorners() {
        final float mCornerRadius = this.mCornerRadius;
        final RectF rectF = new RectF(-mCornerRadius, -mCornerRadius, mCornerRadius, mCornerRadius);
        final RectF rectF2 = new RectF(rectF);
        final float mShadowSize = this.mShadowSize;
        rectF2.inset(-mShadowSize, -mShadowSize);
        final Path mCornerShadowPath = this.mCornerShadowPath;
        if (mCornerShadowPath == null) {
            this.mCornerShadowPath = new Path();
        }
        else {
            mCornerShadowPath.reset();
        }
        this.mCornerShadowPath.setFillType(Path$FillType.EVEN_ODD);
        this.mCornerShadowPath.moveTo(-this.mCornerRadius, 0.0f);
        this.mCornerShadowPath.rLineTo(-this.mShadowSize, 0.0f);
        this.mCornerShadowPath.arcTo(rectF2, 180.0f, 90.0f, false);
        this.mCornerShadowPath.arcTo(rectF, 270.0f, -90.0f, false);
        this.mCornerShadowPath.close();
        final float n = -rectF2.top;
        if (n > 0.0f) {
            final float n2 = this.mCornerRadius / n;
            this.mCornerShadowPaint.setShader((Shader)new RadialGradient(0.0f, 0.0f, n, new int[] { 0, this.mShadowStartColor, this.mShadowMiddleColor, this.mShadowEndColor }, new float[] { 0.0f, n2, n2 + (1.0f - n2) / 2.0f, 1.0f }, Shader$TileMode.CLAMP));
        }
        this.mEdgeShadowPaint.setShader((Shader)new LinearGradient(0.0f, rectF.top, 0.0f, rectF2.top, new int[] { this.mShadowStartColor, this.mShadowMiddleColor, this.mShadowEndColor }, new float[] { 0.0f, 0.5f, 1.0f }, Shader$TileMode.CLAMP));
        this.mEdgeShadowPaint.setAntiAlias(false);
    }
    
    public static float calculateHorizontalPadding(final float n, final float n2, final boolean b) {
        if (b) {
            final double n3 = n;
            final double cos_45 = ShadowDrawableWrapper.COS_45;
            final double n4 = n2;
            Double.isNaN(n4);
            Double.isNaN(n3);
            return (float)(n3 + (1.0 - cos_45) * n4);
        }
        return n;
    }
    
    public static float calculateVerticalPadding(final float n, final float n2, final boolean b) {
        if (b) {
            final double n3 = 1.5f * n;
            final double cos_45 = ShadowDrawableWrapper.COS_45;
            final double n4 = n2;
            Double.isNaN(n4);
            Double.isNaN(n3);
            return (float)(n3 + (1.0 - cos_45) * n4);
        }
        return 1.5f * n;
    }
    
    private void drawShadow(final Canvas canvas) {
        final int save = canvas.save();
        canvas.rotate(this.mRotation, this.mContentBounds.centerX(), this.mContentBounds.centerY());
        final float n = -this.mCornerRadius - this.mShadowSize;
        final float mCornerRadius = this.mCornerRadius;
        final float width = this.mContentBounds.width();
        boolean b = true;
        final boolean b2 = width - mCornerRadius * 2.0f > 0.0f;
        if (this.mContentBounds.height() - mCornerRadius * 2.0f <= 0.0f) {
            b = false;
        }
        final float mRawShadowSize = this.mRawShadowSize;
        final float n2 = mCornerRadius / (mCornerRadius + (mRawShadowSize - 0.5f * mRawShadowSize));
        final float n3 = mCornerRadius / (mCornerRadius + (mRawShadowSize - 0.25f * mRawShadowSize));
        final float n4 = mCornerRadius / (mCornerRadius + (mRawShadowSize - mRawShadowSize * 1.0f));
        final int save2 = canvas.save();
        canvas.translate(this.mContentBounds.left + mCornerRadius, this.mContentBounds.top + mCornerRadius);
        canvas.scale(n2, n3);
        canvas.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
        if (b2) {
            canvas.scale(1.0f / n2, 1.0f);
            canvas.drawRect(0.0f, n, this.mContentBounds.width() - mCornerRadius * 2.0f, -this.mCornerRadius, this.mEdgeShadowPaint);
        }
        canvas.restoreToCount(save2);
        final int save3 = canvas.save();
        canvas.translate(this.mContentBounds.right - mCornerRadius, this.mContentBounds.bottom - mCornerRadius);
        canvas.scale(n2, n4);
        canvas.rotate(180.0f);
        canvas.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
        if (b2) {
            canvas.scale(1.0f / n2, 1.0f);
            canvas.drawRect(0.0f, n, this.mContentBounds.width() - mCornerRadius * 2.0f, -this.mCornerRadius + this.mShadowSize, this.mEdgeShadowPaint);
        }
        canvas.restoreToCount(save3);
        final int save4 = canvas.save();
        canvas.translate(this.mContentBounds.left + mCornerRadius, this.mContentBounds.bottom - mCornerRadius);
        canvas.scale(n2, n4);
        canvas.rotate(270.0f);
        canvas.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
        if (b) {
            canvas.scale(1.0f / n4, 1.0f);
            canvas.drawRect(0.0f, n, this.mContentBounds.height() - mCornerRadius * 2.0f, -this.mCornerRadius, this.mEdgeShadowPaint);
        }
        canvas.restoreToCount(save4);
        final int save5 = canvas.save();
        canvas.translate(this.mContentBounds.right - mCornerRadius, this.mContentBounds.top + mCornerRadius);
        canvas.scale(n2, n3);
        canvas.rotate(90.0f);
        canvas.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
        if (b) {
            canvas.scale(1.0f / n3, 1.0f);
            canvas.drawRect(0.0f, n, this.mContentBounds.height() - 2.0f * mCornerRadius, -this.mCornerRadius, this.mEdgeShadowPaint);
        }
        canvas.restoreToCount(save5);
        canvas.restoreToCount(save);
    }
    
    private static int toEven(final float n) {
        final int round = Math.round(n);
        if (round % 2 == 1) {
            return round - 1;
        }
        return round;
    }
    
    @Override
    public void draw(final Canvas canvas) {
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
        final float mRawMaxShadowSize = this.mRawMaxShadowSize;
        return this.mRawMaxShadowSize * 1.5f * 2.0f + Math.max(mRawMaxShadowSize, this.mCornerRadius + mRawMaxShadowSize * 1.5f / 2.0f) * 2.0f;
    }
    
    public float getMinWidth() {
        final float mRawMaxShadowSize = this.mRawMaxShadowSize;
        return this.mRawMaxShadowSize * 2.0f + Math.max(mRawMaxShadowSize, this.mCornerRadius + mRawMaxShadowSize / 2.0f) * 2.0f;
    }
    
    @Override
    public int getOpacity() {
        return -3;
    }
    
    @Override
    public boolean getPadding(final Rect rect) {
        final int n = (int)Math.ceil(calculateVerticalPadding(this.mRawMaxShadowSize, this.mCornerRadius, this.mAddPaddingForCorners));
        final int n2 = (int)Math.ceil(calculateHorizontalPadding(this.mRawMaxShadowSize, this.mCornerRadius, this.mAddPaddingForCorners));
        rect.set(n2, n, n2, n);
        return true;
    }
    
    public float getShadowSize() {
        return this.mRawShadowSize;
    }
    
    @Override
    protected void onBoundsChange(final Rect rect) {
        this.mDirty = true;
    }
    
    public void setAddPaddingForCorners(final boolean mAddPaddingForCorners) {
        this.mAddPaddingForCorners = mAddPaddingForCorners;
        this.invalidateSelf();
    }
    
    @Override
    public void setAlpha(final int alpha) {
        super.setAlpha(alpha);
        this.mCornerShadowPaint.setAlpha(alpha);
        this.mEdgeShadowPaint.setAlpha(alpha);
    }
    
    public void setCornerRadius(float mCornerRadius) {
        mCornerRadius = (float)Math.round(mCornerRadius);
        if (this.mCornerRadius == mCornerRadius) {
            return;
        }
        this.mCornerRadius = mCornerRadius;
        this.mDirty = true;
        this.invalidateSelf();
    }
    
    public void setMaxShadowSize(final float n) {
        this.setShadowSize(this.mRawShadowSize, n);
    }
    
    final void setRotation(final float mRotation) {
        if (this.mRotation != mRotation) {
            this.mRotation = mRotation;
            this.invalidateSelf();
        }
    }
    
    public void setShadowSize(final float n) {
        this.setShadowSize(n, this.mRawMaxShadowSize);
    }
    
    void setShadowSize(float mRawShadowSize, float n) {
        if (mRawShadowSize < 0.0f || n < 0.0f) {
            throw new IllegalArgumentException("invalid shadow size");
        }
        mRawShadowSize = (float)toEven(mRawShadowSize);
        n = (float)toEven(n);
        if (mRawShadowSize > n) {
            mRawShadowSize = n;
            if (!this.mPrintedShadowClipWarning) {
                this.mPrintedShadowClipWarning = true;
            }
        }
        if (this.mRawShadowSize == mRawShadowSize && this.mRawMaxShadowSize == n) {
            return;
        }
        this.mRawShadowSize = mRawShadowSize;
        this.mRawMaxShadowSize = n;
        this.mShadowSize = (float)Math.round(1.5f * mRawShadowSize);
        this.mMaxShadowSize = n;
        this.mDirty = true;
        this.invalidateSelf();
    }
}
