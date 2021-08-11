/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.graphics.Canvas
 *  android.graphics.ColorFilter
 *  android.graphics.Outline
 *  android.graphics.Paint
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.PorterDuffColorFilter
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  android.graphics.drawable.Drawable
 */
package androidx.cardview.widget;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import androidx.cardview.widget.RoundRectDrawableWithShadow;

class RoundRectDrawable
extends Drawable {
    private ColorStateList mBackground;
    private final RectF mBoundsF;
    private final Rect mBoundsI;
    private boolean mInsetForPadding = false;
    private boolean mInsetForRadius = true;
    private float mPadding;
    private final Paint mPaint;
    private float mRadius;
    private ColorStateList mTint;
    private PorterDuffColorFilter mTintFilter;
    private PorterDuff.Mode mTintMode = PorterDuff.Mode.SRC_IN;

    RoundRectDrawable(ColorStateList colorStateList, float f) {
        this.mRadius = f;
        this.mPaint = new Paint(5);
        this.setBackground(colorStateList);
        this.mBoundsF = new RectF();
        this.mBoundsI = new Rect();
    }

    private PorterDuffColorFilter createTintFilter(ColorStateList colorStateList, PorterDuff.Mode mode) {
        if (colorStateList != null && mode != null) {
            return new PorterDuffColorFilter(colorStateList.getColorForState(this.getState(), 0), mode);
        }
        return null;
    }

    private void setBackground(ColorStateList colorStateList) {
        if (colorStateList == null) {
            colorStateList = ColorStateList.valueOf((int)0);
        }
        this.mBackground = colorStateList;
        this.mPaint.setColor(colorStateList.getColorForState(this.getState(), this.mBackground.getDefaultColor()));
    }

    private void updateBounds(Rect rect) {
        Rect rect2 = rect;
        if (rect == null) {
            rect2 = this.getBounds();
        }
        this.mBoundsF.set((float)rect2.left, (float)rect2.top, (float)rect2.right, (float)rect2.bottom);
        this.mBoundsI.set(rect2);
        if (this.mInsetForPadding) {
            float f = RoundRectDrawableWithShadow.calculateVerticalPadding(this.mPadding, this.mRadius, this.mInsetForRadius);
            float f2 = RoundRectDrawableWithShadow.calculateHorizontalPadding(this.mPadding, this.mRadius, this.mInsetForRadius);
            this.mBoundsI.inset((int)Math.ceil(f2), (int)Math.ceil(f));
            this.mBoundsF.set(this.mBoundsI);
        }
    }

    public void draw(Canvas canvas) {
        boolean bl;
        Paint paint = this.mPaint;
        if (this.mTintFilter != null && paint.getColorFilter() == null) {
            paint.setColorFilter((ColorFilter)this.mTintFilter);
            bl = true;
        } else {
            bl = false;
        }
        RectF rectF = this.mBoundsF;
        float f = this.mRadius;
        canvas.drawRoundRect(rectF, f, f, paint);
        if (bl) {
            paint.setColorFilter(null);
        }
    }

    public ColorStateList getColor() {
        return this.mBackground;
    }

    public int getOpacity() {
        return -3;
    }

    public void getOutline(Outline outline) {
        outline.setRoundRect(this.mBoundsI, this.mRadius);
    }

    float getPadding() {
        return this.mPadding;
    }

    public float getRadius() {
        return this.mRadius;
    }

    public boolean isStateful() {
        ColorStateList colorStateList = this.mTint;
        if (colorStateList != null && colorStateList.isStateful() || (colorStateList = this.mBackground) != null && colorStateList.isStateful() || super.isStateful()) {
            return true;
        }
        return false;
    }

    protected void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        this.updateBounds(rect);
    }

    protected boolean onStateChange(int[] colorStateList) {
        ColorStateList colorStateList2 = this.mBackground;
        int n = colorStateList2.getColorForState((int[])colorStateList, colorStateList2.getDefaultColor());
        boolean bl = n != this.mPaint.getColor();
        if (bl) {
            this.mPaint.setColor(n);
        }
        if ((colorStateList = this.mTint) != null && (colorStateList2 = this.mTintMode) != null) {
            this.mTintFilter = this.createTintFilter(colorStateList, (PorterDuff.Mode)colorStateList2);
            return true;
        }
        return bl;
    }

    public void setAlpha(int n) {
        this.mPaint.setAlpha(n);
    }

    public void setColor(ColorStateList colorStateList) {
        this.setBackground(colorStateList);
        this.invalidateSelf();
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }

    void setPadding(float f, boolean bl, boolean bl2) {
        if (f == this.mPadding && this.mInsetForPadding == bl && this.mInsetForRadius == bl2) {
            return;
        }
        this.mPadding = f;
        this.mInsetForPadding = bl;
        this.mInsetForRadius = bl2;
        this.updateBounds(null);
        this.invalidateSelf();
    }

    void setRadius(float f) {
        if (f == this.mRadius) {
            return;
        }
        this.mRadius = f;
        this.updateBounds(null);
        this.invalidateSelf();
    }

    public void setTintList(ColorStateList colorStateList) {
        this.mTint = colorStateList;
        this.mTintFilter = this.createTintFilter(colorStateList, this.mTintMode);
        this.invalidateSelf();
    }

    public void setTintMode(PorterDuff.Mode mode) {
        this.mTintMode = mode;
        this.mTintFilter = this.createTintFilter(this.mTint, mode);
        this.invalidateSelf();
    }
}

