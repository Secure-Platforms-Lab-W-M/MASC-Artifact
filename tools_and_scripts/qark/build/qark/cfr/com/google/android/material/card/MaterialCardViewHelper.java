/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.InsetDrawable
 *  android.graphics.drawable.LayerDrawable
 *  android.graphics.drawable.RippleDrawable
 *  android.graphics.drawable.StateListDrawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.view.View
 *  com.google.android.material.R
 *  com.google.android.material.R$attr
 *  com.google.android.material.R$dimen
 *  com.google.android.material.R$id
 *  com.google.android.material.R$style
 *  com.google.android.material.R$styleable
 */
package com.google.android.material.card;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.color.MaterialColors;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.shape.CornerTreatment;
import com.google.android.material.shape.CutCornerTreatment;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.RoundedCornerTreatment;
import com.google.android.material.shape.ShapeAppearanceModel;

class MaterialCardViewHelper {
    private static final float CARD_VIEW_SHADOW_MULTIPLIER = 1.5f;
    private static final int CHECKED_ICON_LAYER_INDEX = 2;
    private static final int[] CHECKED_STATE_SET = new int[]{16842912};
    private static final double COS_45 = Math.cos(Math.toRadians(45.0));
    private static final int DEFAULT_STROKE_VALUE = -1;
    private final MaterialShapeDrawable bgDrawable;
    private boolean checkable;
    private Drawable checkedIcon;
    private final int checkedIconMargin;
    private final int checkedIconSize;
    private ColorStateList checkedIconTint;
    private LayerDrawable clickableForegroundDrawable;
    private MaterialShapeDrawable compatRippleDrawable;
    private Drawable fgDrawable;
    private final MaterialShapeDrawable foregroundContentDrawable;
    private MaterialShapeDrawable foregroundShapeDrawable;
    private boolean isBackgroundOverwritten = false;
    private final MaterialCardView materialCardView;
    private ColorStateList rippleColor;
    private Drawable rippleDrawable;
    private ShapeAppearanceModel shapeAppearanceModel;
    private ColorStateList strokeColor;
    private int strokeWidth;
    private final Rect userContentPadding = new Rect();

    public MaterialCardViewHelper(MaterialCardView materialCardView, AttributeSet attributeSet, int n, int n2) {
        this.materialCardView = materialCardView;
        Object object = new MaterialShapeDrawable(materialCardView.getContext(), attributeSet, n, n2);
        this.bgDrawable = object;
        object.initializeElevationOverlay(materialCardView.getContext());
        this.bgDrawable.setShadowColor(-12303292);
        object = this.bgDrawable.getShapeAppearanceModel().toBuilder();
        attributeSet = materialCardView.getContext().obtainStyledAttributes(attributeSet, R.styleable.CardView, n, R.style.CardView);
        if (attributeSet.hasValue(R.styleable.CardView_cardCornerRadius)) {
            object.setAllCornerSizes(attributeSet.getDimension(R.styleable.CardView_cardCornerRadius, 0.0f));
        }
        this.foregroundContentDrawable = new MaterialShapeDrawable();
        this.setShapeAppearanceModel(object.build());
        materialCardView = materialCardView.getResources();
        this.checkedIconMargin = materialCardView.getDimensionPixelSize(R.dimen.mtrl_card_checked_icon_margin);
        this.checkedIconSize = materialCardView.getDimensionPixelSize(R.dimen.mtrl_card_checked_icon_size);
        attributeSet.recycle();
    }

    private float calculateActualCornerPadding() {
        return Math.max(Math.max(this.calculateCornerPaddingForCornerTreatment(this.shapeAppearanceModel.getTopLeftCorner(), this.bgDrawable.getTopLeftCornerResolvedSize()), this.calculateCornerPaddingForCornerTreatment(this.shapeAppearanceModel.getTopRightCorner(), this.bgDrawable.getTopRightCornerResolvedSize())), Math.max(this.calculateCornerPaddingForCornerTreatment(this.shapeAppearanceModel.getBottomRightCorner(), this.bgDrawable.getBottomRightCornerResolvedSize()), this.calculateCornerPaddingForCornerTreatment(this.shapeAppearanceModel.getBottomLeftCorner(), this.bgDrawable.getBottomLeftCornerResolvedSize())));
    }

    private float calculateCornerPaddingForCornerTreatment(CornerTreatment cornerTreatment, float f) {
        if (cornerTreatment instanceof RoundedCornerTreatment) {
            return (float)((1.0 - COS_45) * (double)f);
        }
        if (cornerTreatment instanceof CutCornerTreatment) {
            return f / 2.0f;
        }
        return 0.0f;
    }

    private float calculateHorizontalBackgroundPadding() {
        float f = this.materialCardView.getMaxCardElevation();
        float f2 = this.shouldAddCornerPaddingOutsideCardBackground() ? this.calculateActualCornerPadding() : 0.0f;
        return f + f2;
    }

    private float calculateVerticalBackgroundPadding() {
        float f = this.materialCardView.getMaxCardElevation();
        float f2 = this.shouldAddCornerPaddingOutsideCardBackground() ? this.calculateActualCornerPadding() : 0.0f;
        return f * 1.5f + f2;
    }

    private boolean canClipToOutline() {
        if (Build.VERSION.SDK_INT >= 21 && this.bgDrawable.isRoundRect()) {
            return true;
        }
        return false;
    }

    private Drawable createCheckedIconLayer() {
        StateListDrawable stateListDrawable = new StateListDrawable();
        Drawable drawable2 = this.checkedIcon;
        if (drawable2 != null) {
            stateListDrawable.addState(CHECKED_STATE_SET, drawable2);
        }
        return stateListDrawable;
    }

    private Drawable createCompatRippleDrawable() {
        MaterialShapeDrawable materialShapeDrawable;
        StateListDrawable stateListDrawable = new StateListDrawable();
        this.compatRippleDrawable = materialShapeDrawable = this.createForegroundShapeDrawable();
        materialShapeDrawable.setFillColor(this.rippleColor);
        materialShapeDrawable = this.compatRippleDrawable;
        stateListDrawable.addState(new int[]{16842919}, (Drawable)materialShapeDrawable);
        return stateListDrawable;
    }

    private Drawable createForegroundRippleDrawable() {
        if (RippleUtils.USE_FRAMEWORK_RIPPLE) {
            this.foregroundShapeDrawable = this.createForegroundShapeDrawable();
            return new RippleDrawable(this.rippleColor, null, (Drawable)this.foregroundShapeDrawable);
        }
        return this.createCompatRippleDrawable();
    }

    private MaterialShapeDrawable createForegroundShapeDrawable() {
        return new MaterialShapeDrawable(this.shapeAppearanceModel);
    }

    private Drawable getClickableForeground() {
        if (this.rippleDrawable == null) {
            this.rippleDrawable = this.createForegroundRippleDrawable();
        }
        if (this.clickableForegroundDrawable == null) {
            Drawable drawable2 = this.createCheckedIconLayer();
            drawable2 = new LayerDrawable(new Drawable[]{this.rippleDrawable, this.foregroundContentDrawable, drawable2});
            this.clickableForegroundDrawable = drawable2;
            drawable2.setId(2, R.id.mtrl_card_checked_layer_id);
        }
        return this.clickableForegroundDrawable;
    }

    private float getParentCardViewCalculatedCornerPadding() {
        if (this.materialCardView.getPreventCornerOverlap() && (Build.VERSION.SDK_INT < 21 || this.materialCardView.getUseCompatPadding())) {
            return (float)((1.0 - COS_45) * (double)this.materialCardView.getCardViewRadius());
        }
        return 0.0f;
    }

    private Drawable insetDrawable(Drawable drawable2) {
        int n;
        int n2;
        block3 : {
            block2 : {
                int n3 = 0;
                n = 0;
                n2 = Build.VERSION.SDK_INT < 21 ? 1 : 0;
                if (n2 != 0) break block2;
                n2 = n3;
                if (!this.materialCardView.getUseCompatPadding()) break block3;
            }
            n2 = (int)Math.ceil(this.calculateVerticalBackgroundPadding());
            n = (int)Math.ceil(this.calculateHorizontalBackgroundPadding());
        }
        return new InsetDrawable(drawable2, n, n2, n, n2){

            public boolean getPadding(Rect rect) {
                return false;
            }
        };
    }

    private boolean shouldAddCornerPaddingInsideCardBackground() {
        if (this.materialCardView.getPreventCornerOverlap() && !this.canClipToOutline()) {
            return true;
        }
        return false;
    }

    private boolean shouldAddCornerPaddingOutsideCardBackground() {
        if (this.materialCardView.getPreventCornerOverlap() && this.canClipToOutline() && this.materialCardView.getUseCompatPadding()) {
            return true;
        }
        return false;
    }

    private void updateInsetForeground(Drawable drawable2) {
        if (Build.VERSION.SDK_INT >= 23 && this.materialCardView.getForeground() instanceof InsetDrawable) {
            ((InsetDrawable)this.materialCardView.getForeground()).setDrawable(drawable2);
            return;
        }
        this.materialCardView.setForeground(this.insetDrawable(drawable2));
    }

    private void updateRippleColor() {
        Drawable drawable2;
        if (RippleUtils.USE_FRAMEWORK_RIPPLE && (drawable2 = this.rippleDrawable) != null) {
            ((RippleDrawable)drawable2).setColor(this.rippleColor);
            return;
        }
        drawable2 = this.compatRippleDrawable;
        if (drawable2 != null) {
            drawable2.setFillColor(this.rippleColor);
        }
    }

    void forceRippleRedraw() {
        Drawable drawable2 = this.rippleDrawable;
        if (drawable2 != null) {
            drawable2 = drawable2.getBounds();
            int n = drawable2.bottom;
            this.rippleDrawable.setBounds(drawable2.left, drawable2.top, drawable2.right, n - 1);
            this.rippleDrawable.setBounds(drawable2.left, drawable2.top, drawable2.right, n);
        }
    }

    MaterialShapeDrawable getBackground() {
        return this.bgDrawable;
    }

    ColorStateList getCardBackgroundColor() {
        return this.bgDrawable.getFillColor();
    }

    Drawable getCheckedIcon() {
        return this.checkedIcon;
    }

    ColorStateList getCheckedIconTint() {
        return this.checkedIconTint;
    }

    float getCornerRadius() {
        return this.bgDrawable.getTopLeftCornerResolvedSize();
    }

    float getProgress() {
        return this.bgDrawable.getInterpolation();
    }

    ColorStateList getRippleColor() {
        return this.rippleColor;
    }

    ShapeAppearanceModel getShapeAppearanceModel() {
        return this.shapeAppearanceModel;
    }

    int getStrokeColor() {
        ColorStateList colorStateList = this.strokeColor;
        if (colorStateList == null) {
            return -1;
        }
        return colorStateList.getDefaultColor();
    }

    ColorStateList getStrokeColorStateList() {
        return this.strokeColor;
    }

    int getStrokeWidth() {
        return this.strokeWidth;
    }

    Rect getUserContentPadding() {
        return this.userContentPadding;
    }

    boolean isBackgroundOverwritten() {
        return this.isBackgroundOverwritten;
    }

    boolean isCheckable() {
        return this.checkable;
    }

    void loadFromAttributes(TypedArray object) {
        boolean bl;
        Object object2 = MaterialResources.getColorStateList(this.materialCardView.getContext(), (TypedArray)object, R.styleable.MaterialCardView_strokeColor);
        this.strokeColor = object2;
        if (object2 == null) {
            this.strokeColor = ColorStateList.valueOf((int)-1);
        }
        this.strokeWidth = object.getDimensionPixelSize(R.styleable.MaterialCardView_strokeWidth, 0);
        this.checkable = bl = object.getBoolean(R.styleable.MaterialCardView_android_checkable, false);
        this.materialCardView.setLongClickable(bl);
        this.checkedIconTint = MaterialResources.getColorStateList(this.materialCardView.getContext(), (TypedArray)object, R.styleable.MaterialCardView_checkedIconTint);
        this.setCheckedIcon(MaterialResources.getDrawable(this.materialCardView.getContext(), (TypedArray)object, R.styleable.MaterialCardView_checkedIcon));
        object2 = MaterialResources.getColorStateList(this.materialCardView.getContext(), (TypedArray)object, R.styleable.MaterialCardView_rippleColor);
        this.rippleColor = object2;
        if (object2 == null) {
            this.rippleColor = ColorStateList.valueOf((int)MaterialColors.getColor((View)this.materialCardView, R.attr.colorControlHighlight));
        }
        object = MaterialResources.getColorStateList(this.materialCardView.getContext(), (TypedArray)object, R.styleable.MaterialCardView_cardForegroundColor);
        object2 = this.foregroundContentDrawable;
        if (object == null) {
            object = ColorStateList.valueOf((int)0);
        }
        object2.setFillColor((ColorStateList)object);
        this.updateRippleColor();
        this.updateElevation();
        this.updateStroke();
        this.materialCardView.setBackgroundInternal(this.insetDrawable(this.bgDrawable));
        object = this.materialCardView.isClickable() ? this.getClickableForeground() : this.foregroundContentDrawable;
        this.fgDrawable = object;
        this.materialCardView.setForeground(this.insetDrawable((Drawable)object));
    }

    void onMeasure(int n, int n2) {
        if (this.clickableForegroundDrawable != null) {
            int n3 = this.checkedIconMargin;
            int n4 = this.checkedIconSize;
            int n5 = n - n3 - n4;
            n = this.checkedIconMargin;
            int n6 = n5;
            int n7 = n;
            if (ViewCompat.getLayoutDirection((View)this.materialCardView) == 1) {
                n7 = n5;
                n6 = n;
            }
            this.clickableForegroundDrawable.setLayerInset(2, n6, this.checkedIconMargin, n7, n2 - n3 - n4);
        }
    }

    void setBackgroundOverwritten(boolean bl) {
        this.isBackgroundOverwritten = bl;
    }

    void setCardBackgroundColor(ColorStateList colorStateList) {
        this.bgDrawable.setFillColor(colorStateList);
    }

    void setCheckable(boolean bl) {
        this.checkable = bl;
    }

    void setCheckedIcon(Drawable drawable2) {
        this.checkedIcon = drawable2;
        if (drawable2 != null) {
            this.checkedIcon = drawable2 = DrawableCompat.wrap(drawable2.mutate());
            DrawableCompat.setTintList(drawable2, this.checkedIconTint);
        }
        if (this.clickableForegroundDrawable != null) {
            drawable2 = this.createCheckedIconLayer();
            this.clickableForegroundDrawable.setDrawableByLayerId(R.id.mtrl_card_checked_layer_id, drawable2);
        }
    }

    void setCheckedIconTint(ColorStateList colorStateList) {
        this.checkedIconTint = colorStateList;
        Drawable drawable2 = this.checkedIcon;
        if (drawable2 != null) {
            DrawableCompat.setTintList(drawable2, colorStateList);
        }
    }

    void setCornerRadius(float f) {
        this.setShapeAppearanceModel(this.shapeAppearanceModel.withCornerSize(f));
        this.fgDrawable.invalidateSelf();
        if (this.shouldAddCornerPaddingOutsideCardBackground() || this.shouldAddCornerPaddingInsideCardBackground()) {
            this.updateContentPadding();
        }
        if (this.shouldAddCornerPaddingOutsideCardBackground()) {
            this.updateInsets();
        }
    }

    void setProgress(float f) {
        this.bgDrawable.setInterpolation(f);
        MaterialShapeDrawable materialShapeDrawable = this.foregroundContentDrawable;
        if (materialShapeDrawable != null) {
            materialShapeDrawable.setInterpolation(f);
        }
        if ((materialShapeDrawable = this.foregroundShapeDrawable) != null) {
            materialShapeDrawable.setInterpolation(f);
        }
    }

    void setRippleColor(ColorStateList colorStateList) {
        this.rippleColor = colorStateList;
        this.updateRippleColor();
    }

    void setShapeAppearanceModel(ShapeAppearanceModel shapeAppearanceModel) {
        this.shapeAppearanceModel = shapeAppearanceModel;
        this.bgDrawable.setShapeAppearanceModel(shapeAppearanceModel);
        MaterialShapeDrawable materialShapeDrawable = this.foregroundContentDrawable;
        if (materialShapeDrawable != null) {
            materialShapeDrawable.setShapeAppearanceModel(shapeAppearanceModel);
        }
        if ((materialShapeDrawable = this.foregroundShapeDrawable) != null) {
            materialShapeDrawable.setShapeAppearanceModel(shapeAppearanceModel);
        }
        if ((materialShapeDrawable = this.compatRippleDrawable) != null) {
            materialShapeDrawable.setShapeAppearanceModel(shapeAppearanceModel);
        }
    }

    void setStrokeColor(ColorStateList colorStateList) {
        if (this.strokeColor == colorStateList) {
            return;
        }
        this.strokeColor = colorStateList;
        this.updateStroke();
    }

    void setStrokeWidth(int n) {
        if (n == this.strokeWidth) {
            return;
        }
        this.strokeWidth = n;
        this.updateStroke();
    }

    void setUserContentPadding(int n, int n2, int n3, int n4) {
        this.userContentPadding.set(n, n2, n3, n4);
        this.updateContentPadding();
    }

    void updateClickable() {
        Drawable drawable2 = this.fgDrawable;
        Drawable drawable3 = this.materialCardView.isClickable() ? this.getClickableForeground() : this.foregroundContentDrawable;
        this.fgDrawable = drawable3;
        if (drawable2 != drawable3) {
            this.updateInsetForeground(drawable3);
        }
    }

    void updateContentPadding() {
        int n = !this.shouldAddCornerPaddingInsideCardBackground() && !this.shouldAddCornerPaddingOutsideCardBackground() ? 0 : 1;
        float f = n != 0 ? this.calculateActualCornerPadding() : 0.0f;
        n = (int)(f - this.getParentCardViewCalculatedCornerPadding());
        this.materialCardView.setAncestorContentPadding(this.userContentPadding.left + n, this.userContentPadding.top + n, this.userContentPadding.right + n, this.userContentPadding.bottom + n);
    }

    void updateElevation() {
        this.bgDrawable.setElevation(this.materialCardView.getCardElevation());
    }

    void updateInsets() {
        if (!this.isBackgroundOverwritten()) {
            this.materialCardView.setBackgroundInternal(this.insetDrawable(this.bgDrawable));
        }
        this.materialCardView.setForeground(this.insetDrawable(this.fgDrawable));
    }

    void updateStroke() {
        this.foregroundContentDrawable.setStroke((float)this.strokeWidth, this.strokeColor);
    }

}

