// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.cardview.widget;

import android.graphics.ColorFilter;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.RadialGradient;
import android.graphics.Shader$TileMode;
import android.graphics.Path$FillType;
import android.graphics.Rect;
import android.graphics.Paint$Style;
import androidx.cardview.R$dimen;
import androidx.cardview.R$color;
import android.content.res.Resources;
import android.graphics.Path;
import android.graphics.Paint;
import android.graphics.RectF;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;

class RoundRectDrawableWithShadow extends Drawable
{
    private static final double COS_45;
    private static final float SHADOW_MULTIPLIER = 1.5f;
    static RoundRectHelper sRoundRectHelper;
    private boolean mAddPaddingForCorners;
    private ColorStateList mBackground;
    private final RectF mCardBounds;
    private float mCornerRadius;
    private Paint mCornerShadowPaint;
    private Path mCornerShadowPath;
    private boolean mDirty;
    private Paint mEdgeShadowPaint;
    private final int mInsetShadow;
    private Paint mPaint;
    private boolean mPrintedShadowClipWarning;
    private float mRawMaxShadowSize;
    private float mRawShadowSize;
    private final int mShadowEndColor;
    private float mShadowSize;
    private final int mShadowStartColor;
    
    static {
        COS_45 = Math.cos(Math.toRadians(45.0));
    }
    
    RoundRectDrawableWithShadow(final Resources resources, final ColorStateList background, final float n, final float n2, final float n3) {
        this.mDirty = true;
        this.mAddPaddingForCorners = true;
        this.mPrintedShadowClipWarning = false;
        this.mShadowStartColor = resources.getColor(R$color.cardview_shadow_start_color);
        this.mShadowEndColor = resources.getColor(R$color.cardview_shadow_end_color);
        this.mInsetShadow = resources.getDimensionPixelSize(R$dimen.cardview_compat_inset_shadow);
        this.mPaint = new Paint(5);
        this.setBackground(background);
        (this.mCornerShadowPaint = new Paint(5)).setStyle(Paint$Style.FILL);
        this.mCornerRadius = (float)(int)(0.5f + n);
        this.mCardBounds = new RectF();
        (this.mEdgeShadowPaint = new Paint(this.mCornerShadowPaint)).setAntiAlias(false);
        this.setShadowSize(n2, n3);
    }
    
    private void buildComponents(final Rect rect) {
        final float n = this.mRawMaxShadowSize * 1.5f;
        this.mCardBounds.set(rect.left + this.mRawMaxShadowSize, rect.top + n, rect.right - this.mRawMaxShadowSize, rect.bottom - n);
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
        final float mCornerRadius2 = this.mCornerRadius;
        final float n = mCornerRadius2 / (this.mShadowSize + mCornerRadius2);
        final Paint mCornerShadowPaint = this.mCornerShadowPaint;
        final float mCornerRadius3 = this.mCornerRadius;
        final float mShadowSize2 = this.mShadowSize;
        final int mShadowStartColor = this.mShadowStartColor;
        mCornerShadowPaint.setShader((Shader)new RadialGradient(0.0f, 0.0f, mShadowSize2 + mCornerRadius3, new int[] { mShadowStartColor, mShadowStartColor, this.mShadowEndColor }, new float[] { 0.0f, n, 1.0f }, Shader$TileMode.CLAMP));
        final Paint mEdgeShadowPaint = this.mEdgeShadowPaint;
        final float mCornerRadius4 = this.mCornerRadius;
        final float n2 = -mCornerRadius4;
        final float mShadowSize3 = this.mShadowSize;
        final float n3 = -mCornerRadius4;
        final int mShadowStartColor2 = this.mShadowStartColor;
        mEdgeShadowPaint.setShader((Shader)new LinearGradient(0.0f, n2 + mShadowSize3, 0.0f, n3 - mShadowSize3, new int[] { mShadowStartColor2, mShadowStartColor2, this.mShadowEndColor }, new float[] { 0.0f, 0.5f, 1.0f }, Shader$TileMode.CLAMP));
        this.mEdgeShadowPaint.setAntiAlias(false);
    }
    
    static float calculateHorizontalPadding(final float n, final float n2, final boolean b) {
        if (b) {
            return (float)(n + (1.0 - RoundRectDrawableWithShadow.COS_45) * n2);
        }
        return n;
    }
    
    static float calculateVerticalPadding(final float n, final float n2, final boolean b) {
        if (b) {
            return (float)(1.5f * n + (1.0 - RoundRectDrawableWithShadow.COS_45) * n2);
        }
        return 1.5f * n;
    }
    
    private void drawShadow(final Canvas canvas) {
        final float mCornerRadius = this.mCornerRadius;
        final float n = -mCornerRadius - this.mShadowSize;
        final float n2 = mCornerRadius + this.mInsetShadow + this.mRawShadowSize / 2.0f;
        final float width = this.mCardBounds.width();
        boolean b = true;
        final boolean b2 = width - n2 * 2.0f > 0.0f;
        if (this.mCardBounds.height() - n2 * 2.0f <= 0.0f) {
            b = false;
        }
        final int save = canvas.save();
        canvas.translate(this.mCardBounds.left + n2, this.mCardBounds.top + n2);
        canvas.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
        if (b2) {
            canvas.drawRect(0.0f, n, this.mCardBounds.width() - n2 * 2.0f, -this.mCornerRadius, this.mEdgeShadowPaint);
        }
        canvas.restoreToCount(save);
        final int save2 = canvas.save();
        canvas.translate(this.mCardBounds.right - n2, this.mCardBounds.bottom - n2);
        canvas.rotate(180.0f);
        canvas.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
        if (b2) {
            canvas.drawRect(0.0f, n, this.mCardBounds.width() - n2 * 2.0f, -this.mCornerRadius + this.mShadowSize, this.mEdgeShadowPaint);
        }
        canvas.restoreToCount(save2);
        final int save3 = canvas.save();
        canvas.translate(this.mCardBounds.left + n2, this.mCardBounds.bottom - n2);
        canvas.rotate(270.0f);
        canvas.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
        if (b) {
            canvas.drawRect(0.0f, n, this.mCardBounds.height() - n2 * 2.0f, -this.mCornerRadius, this.mEdgeShadowPaint);
        }
        canvas.restoreToCount(save3);
        final int save4 = canvas.save();
        canvas.translate(this.mCardBounds.right - n2, this.mCardBounds.top + n2);
        canvas.rotate(90.0f);
        canvas.drawPath(this.mCornerShadowPath, this.mCornerShadowPaint);
        if (b) {
            canvas.drawRect(0.0f, n, this.mCardBounds.height() - 2.0f * n2, -this.mCornerRadius, this.mEdgeShadowPaint);
        }
        canvas.restoreToCount(save4);
    }
    
    private void setBackground(ColorStateList value) {
        if (value == null) {
            value = ColorStateList.valueOf(0);
        }
        this.mBackground = value;
        this.mPaint.setColor(value.getColorForState(this.getState(), this.mBackground.getDefaultColor()));
    }
    
    private void setShadowSize(float mRawShadowSize, float n) {
        if (mRawShadowSize < 0.0f) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid shadow size ");
            sb.append(mRawShadowSize);
            sb.append(". Must be >= 0");
            throw new IllegalArgumentException(sb.toString());
        }
        if (n < 0.0f) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Invalid max shadow size ");
            sb2.append(n);
            sb2.append(". Must be >= 0");
            throw new IllegalArgumentException(sb2.toString());
        }
        final float n2 = (float)this.toEven(mRawShadowSize);
        final float mRawMaxShadowSize = (float)this.toEven(n);
        mRawShadowSize = n2;
        if (n2 > mRawMaxShadowSize) {
            n = (mRawShadowSize = mRawMaxShadowSize);
            if (!this.mPrintedShadowClipWarning) {
                this.mPrintedShadowClipWarning = true;
                mRawShadowSize = n;
            }
        }
        if (this.mRawShadowSize == mRawShadowSize && this.mRawMaxShadowSize == mRawMaxShadowSize) {
            return;
        }
        this.mRawShadowSize = mRawShadowSize;
        this.mRawMaxShadowSize = mRawMaxShadowSize;
        this.mShadowSize = (float)(int)(1.5f * mRawShadowSize + this.mInsetShadow + 0.5f);
        this.mDirty = true;
        this.invalidateSelf();
    }
    
    private int toEven(final float n) {
        final int n2 = (int)(0.5f + n);
        if (n2 % 2 == 1) {
            return n2 - 1;
        }
        return n2;
    }
    
    public void draw(final Canvas canvas) {
        if (this.mDirty) {
            this.buildComponents(this.getBounds());
            this.mDirty = false;
        }
        canvas.translate(0.0f, this.mRawShadowSize / 2.0f);
        this.drawShadow(canvas);
        canvas.translate(0.0f, -this.mRawShadowSize / 2.0f);
        RoundRectDrawableWithShadow.sRoundRectHelper.drawRoundRect(canvas, this.mCardBounds, this.mCornerRadius, this.mPaint);
    }
    
    ColorStateList getColor() {
        return this.mBackground;
    }
    
    float getCornerRadius() {
        return this.mCornerRadius;
    }
    
    void getMaxShadowAndCornerPadding(final Rect rect) {
        this.getPadding(rect);
    }
    
    float getMaxShadowSize() {
        return this.mRawMaxShadowSize;
    }
    
    float getMinHeight() {
        final float mRawMaxShadowSize = this.mRawMaxShadowSize;
        return (this.mRawMaxShadowSize * 1.5f + this.mInsetShadow) * 2.0f + Math.max(mRawMaxShadowSize, this.mCornerRadius + this.mInsetShadow + mRawMaxShadowSize * 1.5f / 2.0f) * 2.0f;
    }
    
    float getMinWidth() {
        final float mRawMaxShadowSize = this.mRawMaxShadowSize;
        return (this.mRawMaxShadowSize + this.mInsetShadow) * 2.0f + Math.max(mRawMaxShadowSize, this.mCornerRadius + this.mInsetShadow + mRawMaxShadowSize / 2.0f) * 2.0f;
    }
    
    public int getOpacity() {
        return -3;
    }
    
    public boolean getPadding(final Rect rect) {
        final int n = (int)Math.ceil(calculateVerticalPadding(this.mRawMaxShadowSize, this.mCornerRadius, this.mAddPaddingForCorners));
        final int n2 = (int)Math.ceil(calculateHorizontalPadding(this.mRawMaxShadowSize, this.mCornerRadius, this.mAddPaddingForCorners));
        rect.set(n2, n, n2, n);
        return true;
    }
    
    float getShadowSize() {
        return this.mRawShadowSize;
    }
    
    public boolean isStateful() {
        final ColorStateList mBackground = this.mBackground;
        return (mBackground != null && mBackground.isStateful()) || super.isStateful();
    }
    
    protected void onBoundsChange(final Rect rect) {
        super.onBoundsChange(rect);
        this.mDirty = true;
    }
    
    protected boolean onStateChange(final int[] array) {
        final ColorStateList mBackground = this.mBackground;
        final int colorForState = mBackground.getColorForState(array, mBackground.getDefaultColor());
        if (this.mPaint.getColor() == colorForState) {
            return false;
        }
        this.mPaint.setColor(colorForState);
        this.mDirty = true;
        this.invalidateSelf();
        return true;
    }
    
    void setAddPaddingForCorners(final boolean mAddPaddingForCorners) {
        this.mAddPaddingForCorners = mAddPaddingForCorners;
        this.invalidateSelf();
    }
    
    public void setAlpha(final int alpha) {
        this.mPaint.setAlpha(alpha);
        this.mCornerShadowPaint.setAlpha(alpha);
        this.mEdgeShadowPaint.setAlpha(alpha);
    }
    
    void setColor(final ColorStateList background) {
        this.setBackground(background);
        this.invalidateSelf();
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }
    
    void setCornerRadius(float mCornerRadius) {
        if (mCornerRadius < 0.0f) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid radius ");
            sb.append(mCornerRadius);
            sb.append(". Must be >= 0");
            throw new IllegalArgumentException(sb.toString());
        }
        mCornerRadius = (float)(int)(0.5f + mCornerRadius);
        if (this.mCornerRadius == mCornerRadius) {
            return;
        }
        this.mCornerRadius = mCornerRadius;
        this.mDirty = true;
        this.invalidateSelf();
    }
    
    void setMaxShadowSize(final float n) {
        this.setShadowSize(this.mRawShadowSize, n);
    }
    
    void setShadowSize(final float n) {
        this.setShadowSize(n, this.mRawMaxShadowSize);
    }
    
    interface RoundRectHelper
    {
        void drawRoundRect(final Canvas p0, final RectF p1, final float p2, final Paint p3);
    }
}
