/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.TypedArray
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.InsetDrawable
 *  android.graphics.drawable.LayerDrawable
 *  android.graphics.drawable.RippleDrawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.View
 *  com.google.android.material.R
 *  com.google.android.material.R$attr
 *  com.google.android.material.R$styleable
 */
package com.google.android.material.button;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.view.View;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.ripple.RippleDrawableCompat;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.Shapeable;

class MaterialButtonHelper {
    private static final boolean IS_LOLLIPOP;
    private boolean backgroundOverwritten = false;
    private ColorStateList backgroundTint;
    private PorterDuff.Mode backgroundTintMode;
    private boolean checkable;
    private int cornerRadius;
    private boolean cornerRadiusSet = false;
    private int insetBottom;
    private int insetLeft;
    private int insetRight;
    private int insetTop;
    private Drawable maskDrawable;
    private final MaterialButton materialButton;
    private ColorStateList rippleColor;
    private LayerDrawable rippleDrawable;
    private ShapeAppearanceModel shapeAppearanceModel;
    private boolean shouldDrawSurfaceColorStroke = false;
    private ColorStateList strokeColor;
    private int strokeWidth;

    static {
        boolean bl = Build.VERSION.SDK_INT >= 21;
        IS_LOLLIPOP = bl;
    }

    MaterialButtonHelper(MaterialButton materialButton, ShapeAppearanceModel shapeAppearanceModel) {
        this.materialButton = materialButton;
        this.shapeAppearanceModel = shapeAppearanceModel;
    }

    private Drawable createBackground() {
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable(this.shapeAppearanceModel);
        materialShapeDrawable.initializeElevationOverlay(this.materialButton.getContext());
        DrawableCompat.setTintList(materialShapeDrawable, this.backgroundTint);
        Object object = this.backgroundTintMode;
        if (object != null) {
            DrawableCompat.setTintMode(materialShapeDrawable, (PorterDuff.Mode)object);
        }
        materialShapeDrawable.setStroke((float)this.strokeWidth, this.strokeColor);
        object = new MaterialShapeDrawable(this.shapeAppearanceModel);
        object.setTint(0);
        float f = this.strokeWidth;
        int n = this.shouldDrawSurfaceColorStroke ? MaterialColors.getColor((View)this.materialButton, R.attr.colorSurface) : 0;
        object.setStroke(f, n);
        if (IS_LOLLIPOP) {
            MaterialShapeDrawable materialShapeDrawable2 = new MaterialShapeDrawable(this.shapeAppearanceModel);
            this.maskDrawable = materialShapeDrawable2;
            DrawableCompat.setTint(materialShapeDrawable2, -1);
            materialShapeDrawable = new RippleDrawable(RippleUtils.sanitizeRippleDrawableColor(this.rippleColor), (Drawable)this.wrapDrawableWithInset((Drawable)new LayerDrawable(new Drawable[]{object, materialShapeDrawable})), this.maskDrawable);
            this.rippleDrawable = materialShapeDrawable;
            return materialShapeDrawable;
        }
        RippleDrawableCompat rippleDrawableCompat = new RippleDrawableCompat(this.shapeAppearanceModel);
        this.maskDrawable = rippleDrawableCompat;
        DrawableCompat.setTintList(rippleDrawableCompat, RippleUtils.sanitizeRippleDrawableColor(this.rippleColor));
        materialShapeDrawable = new LayerDrawable(new Drawable[]{object, materialShapeDrawable, this.maskDrawable});
        this.rippleDrawable = materialShapeDrawable;
        return this.wrapDrawableWithInset(materialShapeDrawable);
    }

    private MaterialShapeDrawable getMaterialShapeDrawable(boolean bl) {
        LayerDrawable layerDrawable = this.rippleDrawable;
        if (layerDrawable != null && layerDrawable.getNumberOfLayers() > 0) {
            if (IS_LOLLIPOP) {
                return (MaterialShapeDrawable)((LayerDrawable)((InsetDrawable)this.rippleDrawable.getDrawable(0)).getDrawable()).getDrawable(bl ^ true);
            }
            return (MaterialShapeDrawable)this.rippleDrawable.getDrawable(bl ^ true);
        }
        return null;
    }

    private MaterialShapeDrawable getSurfaceColorStrokeDrawable() {
        return this.getMaterialShapeDrawable(true);
    }

    private void updateButtonShape(ShapeAppearanceModel shapeAppearanceModel) {
        if (this.getMaterialShapeDrawable() != null) {
            this.getMaterialShapeDrawable().setShapeAppearanceModel(shapeAppearanceModel);
        }
        if (this.getSurfaceColorStrokeDrawable() != null) {
            this.getSurfaceColorStrokeDrawable().setShapeAppearanceModel(shapeAppearanceModel);
        }
        if (this.getMaskDrawable() != null) {
            this.getMaskDrawable().setShapeAppearanceModel(shapeAppearanceModel);
        }
    }

    private void updateStroke() {
        MaterialShapeDrawable materialShapeDrawable = this.getMaterialShapeDrawable();
        MaterialShapeDrawable materialShapeDrawable2 = this.getSurfaceColorStrokeDrawable();
        if (materialShapeDrawable != null) {
            materialShapeDrawable.setStroke((float)this.strokeWidth, this.strokeColor);
            if (materialShapeDrawable2 != null) {
                float f = this.strokeWidth;
                int n = this.shouldDrawSurfaceColorStroke ? MaterialColors.getColor((View)this.materialButton, R.attr.colorSurface) : 0;
                materialShapeDrawable2.setStroke(f, n);
            }
        }
    }

    private InsetDrawable wrapDrawableWithInset(Drawable drawable2) {
        return new InsetDrawable(drawable2, this.insetLeft, this.insetTop, this.insetRight, this.insetBottom);
    }

    int getCornerRadius() {
        return this.cornerRadius;
    }

    public Shapeable getMaskDrawable() {
        LayerDrawable layerDrawable = this.rippleDrawable;
        if (layerDrawable != null && layerDrawable.getNumberOfLayers() > 1) {
            if (this.rippleDrawable.getNumberOfLayers() > 2) {
                return (Shapeable)this.rippleDrawable.getDrawable(2);
            }
            return (Shapeable)this.rippleDrawable.getDrawable(1);
        }
        return null;
    }

    MaterialShapeDrawable getMaterialShapeDrawable() {
        return this.getMaterialShapeDrawable(false);
    }

    ColorStateList getRippleColor() {
        return this.rippleColor;
    }

    ShapeAppearanceModel getShapeAppearanceModel() {
        return this.shapeAppearanceModel;
    }

    ColorStateList getStrokeColor() {
        return this.strokeColor;
    }

    int getStrokeWidth() {
        return this.strokeWidth;
    }

    ColorStateList getSupportBackgroundTintList() {
        return this.backgroundTint;
    }

    PorterDuff.Mode getSupportBackgroundTintMode() {
        return this.backgroundTintMode;
    }

    boolean isBackgroundOverwritten() {
        return this.backgroundOverwritten;
    }

    boolean isCheckable() {
        return this.checkable;
    }

    void loadFromAttributes(TypedArray object) {
        int n;
        this.insetLeft = object.getDimensionPixelOffset(R.styleable.MaterialButton_android_insetLeft, 0);
        this.insetRight = object.getDimensionPixelOffset(R.styleable.MaterialButton_android_insetRight, 0);
        this.insetTop = object.getDimensionPixelOffset(R.styleable.MaterialButton_android_insetTop, 0);
        this.insetBottom = object.getDimensionPixelOffset(R.styleable.MaterialButton_android_insetBottom, 0);
        if (object.hasValue(R.styleable.MaterialButton_cornerRadius)) {
            this.cornerRadius = n = object.getDimensionPixelSize(R.styleable.MaterialButton_cornerRadius, -1);
            this.setShapeAppearanceModel(this.shapeAppearanceModel.withCornerSize(n));
            this.cornerRadiusSet = true;
        }
        this.strokeWidth = object.getDimensionPixelSize(R.styleable.MaterialButton_strokeWidth, 0);
        this.backgroundTintMode = ViewUtils.parseTintMode(object.getInt(R.styleable.MaterialButton_backgroundTintMode, -1), PorterDuff.Mode.SRC_IN);
        this.backgroundTint = MaterialResources.getColorStateList(this.materialButton.getContext(), (TypedArray)object, R.styleable.MaterialButton_backgroundTint);
        this.strokeColor = MaterialResources.getColorStateList(this.materialButton.getContext(), (TypedArray)object, R.styleable.MaterialButton_strokeColor);
        this.rippleColor = MaterialResources.getColorStateList(this.materialButton.getContext(), (TypedArray)object, R.styleable.MaterialButton_rippleColor);
        this.checkable = object.getBoolean(R.styleable.MaterialButton_android_checkable, false);
        n = object.getDimensionPixelSize(R.styleable.MaterialButton_elevation, 0);
        int n2 = ViewCompat.getPaddingStart((View)this.materialButton);
        int n3 = this.materialButton.getPaddingTop();
        int n4 = ViewCompat.getPaddingEnd((View)this.materialButton);
        int n5 = this.materialButton.getPaddingBottom();
        this.materialButton.setInternalBackground(this.createBackground());
        object = this.getMaterialShapeDrawable();
        if (object != null) {
            object.setElevation(n);
        }
        ViewCompat.setPaddingRelative((View)this.materialButton, this.insetLeft + n2, this.insetTop + n3, this.insetRight + n4, this.insetBottom + n5);
    }

    void setBackgroundColor(int n) {
        if (this.getMaterialShapeDrawable() != null) {
            this.getMaterialShapeDrawable().setTint(n);
        }
    }

    void setBackgroundOverwritten() {
        this.backgroundOverwritten = true;
        this.materialButton.setSupportBackgroundTintList(this.backgroundTint);
        this.materialButton.setSupportBackgroundTintMode(this.backgroundTintMode);
    }

    void setCheckable(boolean bl) {
        this.checkable = bl;
    }

    void setCornerRadius(int n) {
        if (!this.cornerRadiusSet || this.cornerRadius != n) {
            this.cornerRadius = n;
            this.cornerRadiusSet = true;
            this.setShapeAppearanceModel(this.shapeAppearanceModel.withCornerSize(n));
        }
    }

    void setRippleColor(ColorStateList colorStateList) {
        if (this.rippleColor != colorStateList) {
            this.rippleColor = colorStateList;
            if (IS_LOLLIPOP && this.materialButton.getBackground() instanceof RippleDrawable) {
                ((RippleDrawable)this.materialButton.getBackground()).setColor(RippleUtils.sanitizeRippleDrawableColor(colorStateList));
                return;
            }
            if (!IS_LOLLIPOP && this.materialButton.getBackground() instanceof RippleDrawableCompat) {
                ((RippleDrawableCompat)this.materialButton.getBackground()).setTintList(RippleUtils.sanitizeRippleDrawableColor(colorStateList));
            }
        }
    }

    void setShapeAppearanceModel(ShapeAppearanceModel shapeAppearanceModel) {
        this.shapeAppearanceModel = shapeAppearanceModel;
        this.updateButtonShape(shapeAppearanceModel);
    }

    void setShouldDrawSurfaceColorStroke(boolean bl) {
        this.shouldDrawSurfaceColorStroke = bl;
        this.updateStroke();
    }

    void setStrokeColor(ColorStateList colorStateList) {
        if (this.strokeColor != colorStateList) {
            this.strokeColor = colorStateList;
            this.updateStroke();
        }
    }

    void setStrokeWidth(int n) {
        if (this.strokeWidth != n) {
            this.strokeWidth = n;
            this.updateStroke();
        }
    }

    void setSupportBackgroundTintList(ColorStateList colorStateList) {
        if (this.backgroundTint != colorStateList) {
            this.backgroundTint = colorStateList;
            if (this.getMaterialShapeDrawable() != null) {
                DrawableCompat.setTintList(this.getMaterialShapeDrawable(), this.backgroundTint);
            }
        }
    }

    void setSupportBackgroundTintMode(PorterDuff.Mode mode) {
        if (this.backgroundTintMode != mode) {
            this.backgroundTintMode = mode;
            if (this.getMaterialShapeDrawable() != null && this.backgroundTintMode != null) {
                DrawableCompat.setTintMode(this.getMaterialShapeDrawable(), this.backgroundTintMode);
            }
        }
    }

    void updateMaskBounds(int n, int n2) {
        Drawable drawable2 = this.maskDrawable;
        if (drawable2 != null) {
            drawable2.setBounds(this.insetLeft, this.insetTop, n2 - this.insetRight, n - this.insetBottom);
        }
    }
}

