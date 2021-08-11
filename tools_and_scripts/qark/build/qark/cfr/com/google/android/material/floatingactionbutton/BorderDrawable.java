/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.graphics.Canvas
 *  android.graphics.ColorFilter
 *  android.graphics.LinearGradient
 *  android.graphics.Outline
 *  android.graphics.Paint
 *  android.graphics.Paint$Style
 *  android.graphics.Path
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  android.graphics.Shader
 *  android.graphics.Shader$TileMode
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$ConstantState
 */
package com.google.android.material.floatingactionbutton;

import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import androidx.core.graphics.ColorUtils;
import com.google.android.material.shape.CornerSize;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.ShapeAppearancePathProvider;

class BorderDrawable
extends Drawable {
    private static final float DRAW_STROKE_WIDTH_MULTIPLE = 1.3333f;
    private ColorStateList borderTint;
    float borderWidth;
    private int bottomInnerStrokeColor;
    private int bottomOuterStrokeColor;
    private final RectF boundsRectF = new RectF();
    private int currentBorderTintColor;
    private boolean invalidateShader;
    private final Paint paint;
    private final ShapeAppearancePathProvider pathProvider = new ShapeAppearancePathProvider();
    private final Rect rect = new Rect();
    private final RectF rectF = new RectF();
    private ShapeAppearanceModel shapeAppearanceModel;
    private final Path shapePath = new Path();
    private final BorderState state;
    private int topInnerStrokeColor;
    private int topOuterStrokeColor;

    BorderDrawable(ShapeAppearanceModel shapeAppearanceModel) {
        this.state = new BorderState();
        this.invalidateShader = true;
        this.shapeAppearanceModel = shapeAppearanceModel;
        shapeAppearanceModel = new Paint(1);
        this.paint = shapeAppearanceModel;
        shapeAppearanceModel.setStyle(Paint.Style.STROKE);
    }

    private Shader createGradientShader() {
        Rect rect = this.rect;
        this.copyBounds(rect);
        float f = this.borderWidth / (float)rect.height();
        int n = ColorUtils.compositeColors(this.topOuterStrokeColor, this.currentBorderTintColor);
        int n2 = ColorUtils.compositeColors(this.topInnerStrokeColor, this.currentBorderTintColor);
        int n3 = ColorUtils.compositeColors(ColorUtils.setAlphaComponent(this.topInnerStrokeColor, 0), this.currentBorderTintColor);
        int n4 = ColorUtils.compositeColors(ColorUtils.setAlphaComponent(this.bottomInnerStrokeColor, 0), this.currentBorderTintColor);
        int n5 = ColorUtils.compositeColors(this.bottomInnerStrokeColor, this.currentBorderTintColor);
        int n6 = ColorUtils.compositeColors(this.bottomOuterStrokeColor, this.currentBorderTintColor);
        float f2 = rect.top;
        float f3 = rect.bottom;
        rect = Shader.TileMode.CLAMP;
        return new LinearGradient(0.0f, f2, 0.0f, f3, new int[]{n, n2, n3, n4, n5, n6}, new float[]{0.0f, f, 0.5f, 0.5f, 1.0f - f, 1.0f}, (Shader.TileMode)rect);
    }

    public void draw(Canvas canvas) {
        if (this.invalidateShader) {
            this.paint.setShader(this.createGradientShader());
            this.invalidateShader = false;
        }
        float f = this.paint.getStrokeWidth() / 2.0f;
        this.copyBounds(this.rect);
        this.rectF.set(this.rect);
        float f2 = Math.min(this.shapeAppearanceModel.getTopLeftCornerSize().getCornerSize(this.getBoundsAsRectF()), this.rectF.width() / 2.0f);
        if (this.shapeAppearanceModel.isRoundRect(this.getBoundsAsRectF())) {
            this.rectF.inset(f, f);
            canvas.drawRoundRect(this.rectF, f2, f2, this.paint);
        }
    }

    protected RectF getBoundsAsRectF() {
        this.boundsRectF.set(this.getBounds());
        return this.boundsRectF;
    }

    public Drawable.ConstantState getConstantState() {
        return this.state;
    }

    public int getOpacity() {
        if (this.borderWidth > 0.0f) {
            return -3;
        }
        return -2;
    }

    public void getOutline(Outline outline) {
        if (this.shapeAppearanceModel.isRoundRect(this.getBoundsAsRectF())) {
            float f = this.shapeAppearanceModel.getTopLeftCornerSize().getCornerSize(this.getBoundsAsRectF());
            outline.setRoundRect(this.getBounds(), f);
            return;
        }
        this.copyBounds(this.rect);
        this.rectF.set(this.rect);
        this.pathProvider.calculatePath(this.shapeAppearanceModel, 1.0f, this.rectF, this.shapePath);
        if (this.shapePath.isConvex()) {
            outline.setConvexPath(this.shapePath);
        }
    }

    public boolean getPadding(Rect rect) {
        if (this.shapeAppearanceModel.isRoundRect(this.getBoundsAsRectF())) {
            int n = Math.round(this.borderWidth);
            rect.set(n, n, n, n);
        }
        return true;
    }

    public ShapeAppearanceModel getShapeAppearanceModel() {
        return this.shapeAppearanceModel;
    }

    public boolean isStateful() {
        ColorStateList colorStateList = this.borderTint;
        if (colorStateList != null && colorStateList.isStateful() || super.isStateful()) {
            return true;
        }
        return false;
    }

    protected void onBoundsChange(Rect rect) {
        this.invalidateShader = true;
    }

    protected boolean onStateChange(int[] arrn) {
        int n;
        ColorStateList colorStateList = this.borderTint;
        if (colorStateList != null && (n = colorStateList.getColorForState(arrn, this.currentBorderTintColor)) != this.currentBorderTintColor) {
            this.invalidateShader = true;
            this.currentBorderTintColor = n;
        }
        if (this.invalidateShader) {
            this.invalidateSelf();
        }
        return this.invalidateShader;
    }

    public void setAlpha(int n) {
        this.paint.setAlpha(n);
        this.invalidateSelf();
    }

    void setBorderTint(ColorStateList colorStateList) {
        if (colorStateList != null) {
            this.currentBorderTintColor = colorStateList.getColorForState(this.getState(), this.currentBorderTintColor);
        }
        this.borderTint = colorStateList;
        this.invalidateShader = true;
        this.invalidateSelf();
    }

    public void setBorderWidth(float f) {
        if (this.borderWidth != f) {
            this.borderWidth = f;
            this.paint.setStrokeWidth(1.3333f * f);
            this.invalidateShader = true;
            this.invalidateSelf();
        }
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.paint.setColorFilter(colorFilter);
        this.invalidateSelf();
    }

    void setGradientColors(int n, int n2, int n3, int n4) {
        this.topOuterStrokeColor = n;
        this.topInnerStrokeColor = n2;
        this.bottomOuterStrokeColor = n3;
        this.bottomInnerStrokeColor = n4;
    }

    public void setShapeAppearanceModel(ShapeAppearanceModel shapeAppearanceModel) {
        this.shapeAppearanceModel = shapeAppearanceModel;
        this.invalidateSelf();
    }

    private class BorderState
    extends Drawable.ConstantState {
        private BorderState() {
        }

        public int getChangingConfigurations() {
            return 0;
        }

        public Drawable newDrawable() {
            return BorderDrawable.this;
        }
    }

}

